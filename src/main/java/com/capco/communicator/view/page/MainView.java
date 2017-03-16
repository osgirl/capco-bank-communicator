package com.capco.communicator.view.page;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.MainNavigator;
import com.capco.communicator.view.component.NavigationMenu;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@UIScope
public class MainView extends HorizontalLayout{

    public MainView(AccountRepository accountRepository, AuthService authService) {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new NavigationMenu(accountRepository, authService));

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new MainNavigator(content);
    }
}
