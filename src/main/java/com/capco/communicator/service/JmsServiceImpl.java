package com.capco.communicator.service;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

@SpringComponent
public class JmsServiceImpl implements JmsService{

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Override
    public void send(String message) {
        jmsTemplate.convertAndSend(queue, message);
    }
}
