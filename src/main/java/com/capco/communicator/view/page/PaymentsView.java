package com.capco.communicator.view.page;

import com.capco.communicator.repository.PaymentRepository;
import com.capco.communicator.schema.Account;
import com.capco.communicator.schema.Bank;
import com.capco.communicator.schema.Payment;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = PaymentsView.VIEW_NAME)
public class PaymentsView extends Panel implements View{

    public static final String VIEW_NAME = "payments";

    @Autowired
    private PaymentRepository repo;

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
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Payments");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        HorizontalLayout tools = new HorizontalLayout(buildSearch());
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component buildBody(){
        table = new Table() {

            @Override
            protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
                if("bank".equals(colId)){
                    return ((Bank)property.getValue()).getCode();
                }

                if("account".equals(colId)){
                    return ((Account)property.getValue()).getCode();
                }

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

        listPayments(null);
        table.setVisibleColumns("id", "account", "bank" ,"credit", "debit");
        table.setColumnHeaders("id", "account", "bank" ,"credit", "debit");

        table.setFooterVisible(true);
        table.setImmediate(true);
        table.setFooterVisible(false);

        VerticalLayout body = new VerticalLayout(table);
        body.setExpandRatio(table, 1);
        body.setStyleName(ValoTheme.PANEL_BORDERLESS);
        return body;
    }

    private Component buildSearch() {
        TextField filter = new TextField();
        filter.setInputPrompt("Filter by ???");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        filter.addTextChangeListener(e -> listPayments(e.getText()));
        return filter;
    }

    void listPayments(String text) {
        if (StringUtils.isEmpty(text)) {
            table.setContainerDataSource(new BeanItemContainer(
                    Payment.class, IteratorUtils.toList(repo.findAll().iterator())));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        listPayments(null);
    }
}
