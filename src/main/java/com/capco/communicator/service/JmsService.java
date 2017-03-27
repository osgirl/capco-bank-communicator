package com.capco.communicator.service;

/**
 * Simple JmsService with capability of sending messages
 * */
public interface JmsService {

    void send(String message);
}
