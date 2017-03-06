package com.capco.communicator.view.page;

import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = PaymentContextsPage.VIEW_NAME)
public class PaymentContextsPage extends VerticalLayout implements View {

    public static final String VIEW_NAME = "paymentContexts";

    @Autowired
    private PaymentContextRepository repo;

    private Grid grid;

    @PostConstruct
    void init() {
        this.grid = new Grid();

        initLayout();
    }

    private void initLayout() {
        VerticalLayout mainLayout = new VerticalLayout(grid);
        addComponent(mainLayout);

        //Configure layouts and components
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "state");

        //Initialize listing
        listPaymentContexts();
    }

    void listPaymentContexts() {
        grid.setContainerDataSource(
                new BeanItemContainer(PaymentContext.class, repo.findAll()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
