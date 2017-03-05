package com.capco.communicator.service;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.schema.Account;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class AuthServiceImpl implements AuthService{

    private List<String> loggedUsers = new ArrayList<>();

    @Autowired
    AccountRepository accountRepository;

    @Override
    public boolean authenticate(String login, String password) {
        if(login == null || login.isEmpty()) return false;
        if(password == null || password.isEmpty()) return false;

        Account account = accountRepository.findByLogin(login);

        if(account == null){
            return false;
        }

        if(login.equals(account.getLogin()) && password.equals(account.getPassword())){
            loggedUsers.add(login);
            return true;
        }

        return false;
    }

    @Override
    public boolean isUserLogged(String login) {
        return loggedUsers.contains(login);
    }

    @Override
    public void logout(String login) {
        loggedUsers.remove(login);
    }
}
