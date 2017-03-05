package com.capco.communicator.service;

public interface AuthService {

    boolean authenticate(String login, String password);

    boolean isUserLogged(String login);

    void logout(String login);
}
