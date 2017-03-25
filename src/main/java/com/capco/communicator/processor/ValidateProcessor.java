package com.capco.communicator.processor;

import com.capco.communicator.PaymentProcessingException;
import com.capco.communicator.PaymentUtil;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

@Service
public class ValidateProcessor extends PaymentProcessor {

    private SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    @Override
    public void process(PaymentContext paymentContext) {

        try {
            //Load the schema
            Schema schema = factory.newSchema(new File(PaymentUtil.getSchemaLocation(paymentContext.getPaymentFormat())));

            //Create Source from XML String
            Source xmlSource = new StreamSource(new StringReader(paymentContext.getResource()));

            //Validate the XML against the schema
            Validator validator = schema.newValidator();
            validator.validate(xmlSource);
            paymentContext.setState(State.TRANSFORM);

        } catch (PaymentProcessingException | SAXException | IOException e) {
            paymentContext.setState(State.VALIDATE_ERROR);
            paymentContext.addErrorLog("Context validation failed. State: " + paymentContext.getState() + ", Error: " + e.getMessage());
        }

        paymentContextRepository.save(paymentContext);
    }

}
