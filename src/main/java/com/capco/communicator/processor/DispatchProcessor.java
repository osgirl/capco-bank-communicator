package com.capco.communicator.processor;

import com.capco.communicator.schema.Payment;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DispatchProcessor extends PaymentProcessor {


    @Override
    public void process(PaymentContext paymentContext) {

        try {

            File file = new File(getOutputFilePath(paymentContext.getPayment().getBank().getOutputChannel()));
            JAXBContext jaxbContext = JAXBContext.newInstance(Payment.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(paymentContext.getPayment(), file);
            paymentContext.setState(State.DONE);

        } catch (JAXBException e) {
            paymentContext.setState(State.DISPATCH_ERROR);
            paymentContext.addErrorLog("Dispatching failed. State: " + paymentContext.getState() + ", Error: " + e.getMessage());
        }

        paymentContextRepository.save(paymentContext);
    }

    private String getOutputFilePath(String outputChannel) {
        return Paths.get("").toAbsolutePath() + File.separator
                + outputChannel
                + File.separator + UUID.randomUUID().toString() + ".xml";
    }

}
