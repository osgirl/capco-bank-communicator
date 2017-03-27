package com.capco.communicator.processor;

import com.capco.communicator.PaymentProcessingException;
import com.capco.communicator.PaymentUtil;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * TransformProcessor receives a paymentContext and performs
 * transformation of the initial XML message to target XML format.
 * This is performed using correct XSLT transformation. If the process is
 * successful, next step (Duplicate check) can be initiated
 * */
@Service
public class TransformProcessor extends PaymentProcessor {

    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";
    private TransformerFactory factory = TransformerFactory.newInstance();


    @Override
    public void process(PaymentContext paymentContext) {

        try {
            Source xslt = new StreamSource(new File(PaymentUtil.getXsltPath(paymentContext.getPaymentFormat())));
            Transformer transformer = factory.newTransformer(xslt);
            Source xmlInput = new StreamSource(new StringReader(paymentContext.getResource()));

            StreamResult xmlOutput = new StreamResult(new StringWriter());

            transformer.transform(xmlInput, xmlOutput);

            paymentContext.setPayload(xmlOutput.getWriter().toString().getBytes(DEFAULT_CHAR_ENCODING));
            paymentContext.setState(State.DUPLICATE_CHECK);

        } catch (PaymentProcessingException | TransformerException | UnsupportedEncodingException e) {
            paymentContext.setState(State.TRANSFORM_ERROR);
            paymentContext.addErrorLog("Context XSLT transformation failed. State: " + paymentContext.getState() + ", Error: " + e.getMessage());
        }

        if (paymentContext.getPayload() == null || paymentContext.getPayload().length == 0) {
            paymentContext.setState(State.TRANSFORM_ERROR);
            paymentContext.addErrorLog("Context XSLT transformation failed. State: " + paymentContext.getState() + ", Error: no payload after XSLT transformation.");
        }

        paymentContextRepository.save(paymentContext);
    }

}
