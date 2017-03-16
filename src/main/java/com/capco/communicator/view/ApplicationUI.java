package com.capco.communicator.view;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.UI;


@SpringUI
@Theme("valo")
@Title("Capco Bank Communicator")
@SpringComponent
@SpringViewDisplay
@PreserveOnRefresh
public class ApplicationUI extends UI{

    public static final String SESSION_PARAM_USER = "user";

    @Override
    public void init(VaadinRequest vaadinRequest) {

    }
}
