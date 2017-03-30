package com.capco.communicator.processor;

import com.capco.communicator.schema.Channel;
import com.capco.communicator.schema.Payment;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import com.capco.communicator.service.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * DispatchProcessor receives a paymentContext and performs
 * a dispatch process on the payment. This means that the payment
 * is send for further processing (storage, archivation etc.) into
 * some other system (Ftp, JMS queue etc.). If this process
 * is successful, the payment processing is DONE.
 * */
@Service
public class DispatchProcessor extends PaymentProcessor {

    @Autowired
    private JmsService jmsService;

    @Override
    public void process(PaymentContext paymentContext) {

        try {

            if(Channel.FTP.equals(paymentContext.getChannel())) {
                File file = new File(getOutputFilePath(paymentContext.getPayment().getBank().getOutputChannel()));
                JAXBContext jaxbContext = JAXBContext.newInstance(Payment.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                jaxbMarshaller.marshal(paymentContext.getPayment(), file);
                paymentContext.setState(State.DONE);
            } else if(Channel.MQ.equals(paymentContext.getChannel())) {
                jmsService.send(paymentContext.getPayment().toString());
                paymentContext.setState(State.DONE);
            } else {
                paymentContext.setState(State.DISPATCH_ERROR);
            }

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
