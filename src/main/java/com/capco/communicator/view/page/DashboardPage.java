package com.capco.communicator.view.page;

import com.capco.communicator.view.ApplicationUI;
import com.capco.communicator.view.component.NavBar;
import com.capco.communicator.view.component.ValoMenuLayout;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = DashboardPage.VIEW_NAME)
public class DashboardPage extends VerticalLayout implements View{

    public static final String VIEW_NAME = "home";

    private ValoMenuLayout root = new ValoMenuLayout();
    private ComponentContainer viewDisplay = root.getContentContainer();

    @Autowired
    private NavBar navBar;

    @PostConstruct
    void init(){
        addComponent(root);
        root.setWidth("100%");
        root.setHeight("100%");
        root.addMenu(navBar);
        addStyleName(ValoTheme.UI_WITH_MENU);

        viewDisplay.addComponent(new Label("Welcome to this super Banking application"));
    }

//    private void initLayout() {
//        addComponent(new Label("Welcome to this super Banking application"));
//    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
