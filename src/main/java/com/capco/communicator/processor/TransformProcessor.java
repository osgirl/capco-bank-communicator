package com.capco.communicator.processor;

import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

@Service
public class TransformProcessor extends PaymentProcessor {

    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";

    private static final String xsltPath = "src/main/resources/xslt/paymentA.xslt";

    private TransformerFactory factory = TransformerFactory.newInstance();


    @Override
    public void process(PaymentContext paymentContext) {

        Source xslt = new StreamSource(new File(xsltPath));

        try {
            Transformer transformer = factory.newTransformer(xslt);
            Source xmlInput = new StreamSource(new StringReader(paymentContext.getResource()));

            StreamResult xmlOutput = new StreamResult(new StringWriter());

            transformer.transform(xmlInput, xmlOutput);

            paymentContext.setPayload(xmlOutput.getWriter().toString().getBytes(DEFAULT_CHAR_ENCODING));
            paymentContext.setState(State.DUPLICATE_CHECK);

        } catch (TransformerException | UnsupportedEncodingException e) {
            paymentContext.setState(State.TRANSFORM_ERROR);
            paymentContext.addErrorLog("Context XSLT transformation failed. State: " + paymentContext.getState() + ", Error: " + e.getMessage());
//            e.printStackTrace();
        }

        if(paymentContext.getPayload() == null || paymentContext.getPayload().length == 0){
            paymentContext.setState(State.TRANSFORM_ERROR);
            paymentContext.addErrorLog("Context XSLT transformation failed. State: " + paymentContext.getState() + ", Error: no payload after XSLT transformation.");
        }

        paymentContextRepository.save(paymentContext);
    }

}
