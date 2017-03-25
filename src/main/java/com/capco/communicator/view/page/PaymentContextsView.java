package com.capco.communicator.view.page;

import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
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
    private Window paymentDetailWindow;

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
                PaymentContext.F_CREATED_AT,
                PaymentContext.F_CHANNEL);

        table.setColumnHeaders(
                PaymentContext.F_ID,
                PaymentContext.F_CREATED_AT,
                PaymentContext.F_CHANNEL);

        table.addGeneratedColumn("Change State", (Table.ColumnGenerator) (source, item, columnId) -> {
            ComboBox stateSelect = new ComboBox("Select new State");
            stateSelect.setNewItemsAllowed(false);
            stateSelect.addStyleName(ValoTheme.COMBOBOX_SMALL);
            stateSelect.addItems(State.values());
            stateSelect.setValue(((PaymentContext)item).getState());

            stateSelect.addValueChangeListener((Property.ValueChangeListener) event -> {
                ((PaymentContext)item).setState((State)event.getProperty().getValue());
                repo.save((PaymentContext) item);
                listPaymentContexts();

            });

            return stateSelect;
        });

        table.addGeneratedColumn("Payment Detail", (Table.ColumnGenerator) (source, item, columnId) -> {
            Button btn = new Button("Show");
            btn.addStyleName(ValoTheme.BUTTON_SMALL);

            btn.addClickListener((Button.ClickListener) event -> {
                openPaymentDetailPopup(event, (PaymentContext)item);
            });

            return btn;
        });

        table.setImmediate(true);
        table.setFooterVisible(false);

        VerticalLayout body = new VerticalLayout(table);
        body.setExpandRatio(table, 1);
        body.setStyleName(ValoTheme.PANEL_BORDERLESS);
        return body;
    }

    private void openPaymentDetailPopup(Button.ClickEvent event, PaymentContext paymentContext){
        VerticalLayout paymentDetailContentLayout = new VerticalLayout();
        paymentDetailContentLayout.setMargin(true);
        paymentDetailContentLayout.setSpacing(true);

        Label title = new Label("Payment ID' " + paymentContext.getPayment().getId() + "' detail:");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        paymentDetailContentLayout.addComponent(title);

        paymentDetailContentLayout.addComponent(new Label("Bank code: " + paymentContext.getPayment().getBank().getCode()));
        paymentDetailContentLayout.addComponent(new Label("Account code: " + paymentContext.getPayment().getAccount().getCode()));
        paymentDetailContentLayout.addComponent(new Label("Credit: " + paymentContext.getPayment().getCredit()));
        paymentDetailContentLayout.addComponent(new Label("Debit:  " + paymentContext.getPayment().getDebit()));
        paymentDetailContentLayout.addComponent(new Label("Iban:  " + paymentContext.getPayment().getIban()));
        paymentDetailContentLayout.addComponent(new Label("Notice:  " + paymentContext.getPayment().getNotice()));

        if (paymentDetailWindow == null) {
            paymentDetailWindow = new Window();
            paymentDetailWindow.setWidth(400.0f, Unit.PIXELS);
            paymentDetailWindow.addStyleName("notifications");
            paymentDetailWindow.setClosable(true);
            paymentDetailWindow.setResizable(true);
            paymentDetailWindow.setDraggable(false);
            paymentDetailWindow.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        }
        paymentDetailWindow.setContent(paymentDetailContentLayout);

        if (!paymentDetailWindow.isAttached()) {
            paymentDetailWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            getUI().addWindow(paymentDetailWindow);
            paymentDetailWindow.focus();
        } else {
            paymentDetailWindow.close();
        }
    }

    void listPaymentContexts() {
        table.setContainerDataSource(new BeanItemContainer(PaymentContext.class, repo.findAll()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        listPaymentContexts();
    }
}
