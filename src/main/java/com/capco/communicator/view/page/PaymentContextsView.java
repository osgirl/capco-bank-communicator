package com.capco.communicator.view.page;

import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
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
                if(PaymentContext.F_PAYLOAD.equals(colId)){
                    return "";
                }

                if(PaymentContext.F_RESOURCE.equals(colId)){
                    return "";
                }

                if(PaymentContext.F_PAYMENT.equals(colId)){
                    return "";
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
        table.setSortAscending(false);

        listPaymentContexts();
        table.setVisibleColumns(
                PaymentContext.F_ID,
                PaymentContext.F_STATE,
                PaymentContext.F_CREATED_AT,
                PaymentContext.F_CHANNEL);

        table.setColumnHeaders(
                PaymentContext.F_ID,
                PaymentContext.F_STATE,
                PaymentContext.F_CREATED_AT,
                PaymentContext.F_CHANNEL);

        table.addGeneratedColumn("Change State", new Table.ColumnGenerator() {

            @Override
            public Object generateCell(Table source, Object item, Object columnId) {
                ComboBox stateSelect = new ComboBox("Select new State");
                stateSelect.setNewItemsAllowed(false);
                stateSelect.addStyleName(ValoTheme.COMBOBOX_SMALL);
                stateSelect.addItems(State.values());
                stateSelect.setValue(((PaymentContext)item).getState());

                stateSelect.addValueChangeListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        ((PaymentContext)item).setState((State)event.getProperty().getValue());
                        repo.save((PaymentContext) item);
                        listPaymentContexts();

                    }
                });

                return stateSelect;
            }
        });

        table.addGeneratedColumn("Action", (Table.ColumnGenerator) (source, itemId, columnId) -> {
            Button btn = new Button("View Payments");
            btn.addStyleName(ValoTheme.BUTTON_SMALL);
            btn.addClickListener((Button.ClickListener) event -> {
                //TODO - redirect to payments view with filtered payments of this context
            });
            return btn;
        });

//        table.setFooterVisible(true);
//        table.setColumnFooter("time", "Total");

        table.setImmediate(true);
        table.setFooterVisible(false);

        //Initialize listing
//        listPaymentContexts();

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
