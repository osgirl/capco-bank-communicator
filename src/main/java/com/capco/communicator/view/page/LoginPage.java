package com.capco.communicator.view.page;


import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.ApplicationUI;
import com.capco.communicator.view.component.NavBar;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = LoginPage.VIEW_NAME)
public class LoginPage extends VerticalLayout implements View{

    public static final String VIEW_NAME = "";

    @Autowired
    AuthService authService;

//    @Autowired
//    NavBar navBar;

//    public LoginPage(){
//        init();
//        this.authService = ((ApplicationUI)getUI()).authService;
//    }

    @PostConstruct
    void init() {
        Panel panel = new Panel("Login");
        panel.setSizeUndefined();
        panel.setId("loginpanel");
        addComponent(panel);
        FormLayout content = new FormLayout();

        TextField username = new TextField("Username");
        username.setId("usernameinput");
        content.addComponent(username);
        PasswordField password = new PasswordField("Password");
        password.setId("passwordinput");
        content.addComponent(password);

        Button send = new Button("Enter");
        send.setId("loginbutton");
        send.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(authService.authenticate(username.getValue(), password.getValue())){
                    VaadinSession.getCurrent().setAttribute(ApplicationUI.SESSION_PARAM_USER, username.getValue());

//                    ApplicationUI.setNavBarVisible(true);
                    getUI().getNavigator().navigateTo(DashboardPage.VIEW_NAME);

                }else{
                    Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        content.addComponent(send);
        content.setSizeUndefined();
        content.setMargin(true);
        panel.setContent(content);
        setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        
    }
}
