package com.capco.communicator.service;

/**
 * Simple authentication service interface
 * */
public interface AuthService {

    boolean authenticate(String login, String password);

    boolean isUserLogged(String login);

    void logout(String login);
}
