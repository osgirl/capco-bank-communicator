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

/**
 * ValidateProcessor receives a paymentContext and performs
 * validation of the XML message against correct XSD schema. If the process is
 * successful, next step (Transformation) can be initiated
 * */
@Service
public class ValidateProcessor extends PaymentProcessor {

    private SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    @Override
    public void process(PaymentContext paymentContext) {

        try {
            executeSchemaValidation(paymentContext);
            paymentContext.setState(State.VALIDATE_ERROR);

        } catch (PaymentProcessingException | IOException | SAXException e) {

            paymentContext.setState(State.VALIDATE_ERROR);
            paymentContext.addErrorLog("Context validation failed. State: " + paymentContext.getState() + ", Error: " + e.getMessage());
        }

        paymentContextRepository.save(paymentContext);
    }

    private void executeSchemaValidation(PaymentContext paymentContext) throws PaymentProcessingException, SAXException, IOException {
        // Validate resource in process context against valid xsd schema depending on paymentFormat in this method:
        // a) Load the schema (use Schema instance)
        // b) Create Source from XML String (use StreamSource instance)
        // c) Validate the XML against the schema Note: check also PaymentUtil.java for schema location (use Validator)
    }
}
