package com.capco.communicator.view.page;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = DashboardView.VIEW_NAME)
public class DashboardView extends Panel implements View{

    public static final String VIEW_NAME = "dashboard";

    private VerticalLayout root = new VerticalLayout();

    @PostConstruct
    void init(){
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());
        root.addComponent(buildBody());
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        Label titleLabel = new Label("Dashboard");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildBody(){
        VerticalLayout body = new VerticalLayout();

        return body;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
