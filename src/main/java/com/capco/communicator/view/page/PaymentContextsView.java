package com.capco.communicator.view.page;

import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = PaymentContextsView.VIEW_NAME)
public class PaymentContextsView extends Panel implements View {

    @Autowired
    private PaymentContextRepository repo;

    public static final String VIEW_NAME = "payment-context";

    private VerticalLayout root = new VerticalLayout();
    private Label titleLabel;
    private Grid grid;

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

        Label titleLabel = new Label("Payment Contexts");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildBody(){
        VerticalLayout body = new VerticalLayout();

        grid = new Grid();
        VerticalLayout mainLayout = new VerticalLayout(grid);
        body.addComponent(mainLayout);

        //Configure layouts and components
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "state");

        //Initialize listing
        listPaymentContexts();

        return body;
    }

    void listPaymentContexts() {
        grid.setContainerDataSource(
                new BeanItemContainer(PaymentContext.class, repo.findAll()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
