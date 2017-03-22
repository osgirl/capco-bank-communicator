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

@Service
public class TransformProcessor extends PaymentProcessor {

    private static final String xsltPath = "xslt/paymentA.xslt";

    private TransformerFactory factory = TransformerFactory.newInstance();


    @Override
    public void process(PaymentContext paymentContext) {

        Source xslt = new StreamSource(new File(xsltPath));

        try {
            Transformer transformer = factory.newTransformer(xslt);
            Source xmlInput = new StreamSource(new StringReader(paymentContext.getResource()));

            StreamResult xmlOutput = new StreamResult(new StringWriter());

            transformer.transform(xmlInput, xmlOutput);

            paymentContext.setResource(xmlOutput.getWriter().toString());

        } catch (TransformerException e) {
            //TODO - error handling
            e.printStackTrace();
        }

        paymentContext.setState(State.DUPLICATE_CHECK);
        paymentContextRepository.save(paymentContext);
    }

}
