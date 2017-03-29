package com.capco.communicator.service;

import com.capco.communicator.Application;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;


@Component
public class JmsReceiver {

    @JmsListener(destination = Application.JMS_QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("---------- Received message: <" + message + ">");
    }
}
