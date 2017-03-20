package com.capco.communicator.view.page;

import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import com.vaadin.data.Property;
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

    public static final String VIEW_NAME = "payment-context";

    @Autowired
    private PaymentContextRepository repo;

    private Table table;

    @PostConstruct
    void init(){
        VerticalLayout root = new VerticalLayout();

        root.addStyleName(ValoTheme.PANEL_BORDERLESS);
        root.addStyleName("transactions");

        root.setSizeFull();
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
        table = new Table() {

            @Override
            protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSortAscending(false);

        listPaymentContexts();
        table.setVisibleColumns("id", "resource", "state", "createdAt", "channel");
        table.setColumnHeaders("id", "resource", "state", "createdAt", "channel");

        table.setFooterVisible(true);
        table.setColumnFooter("time", "Total");

        table.setImmediate(true);
        table.setFooterVisible(false);

        //Initialize listing
        listPaymentContexts();

        VerticalLayout body = new VerticalLayout(table);
        body.setExpandRatio(table, 1);
        body.setStyleName(ValoTheme.PANEL_BORDERLESS);
        return body;
    }

    void listPaymentContexts() {
        table.setContainerDataSource(new BeanItemContainer(PaymentContext.class, repo.findAll()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        listPaymentContexts();
    }
}
