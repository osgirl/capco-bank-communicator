package com.capco.communicator.view.page;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.schema.Account;
import com.capco.communicator.view.component.NavBar;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = AccountsPage.VIEW_NAME)
public class AccountsPage extends VerticalLayout implements View {

    public static final String VIEW_NAME = "accounts";

    @Autowired
    private AccountRepository repo;

    @Autowired
    private NavBar navBar;

    private Grid grid;
    private TextField filter;

    @PostConstruct
    void init(){
        this.grid = new Grid();
        this.filter = new TextField();

        addComponent(navBar);
        initLayout();
    }

    private void initLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid);
        addComponent(mainLayout);

        //Configure layouts and components
        actions.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "login");

        //Init the filter
        filter.setInputPrompt("Filter by login");
        filter.addTextChangeListener(e -> listBanks(e.getText()));

        //Initialize listing
        listBanks(null);
    }

    void listBanks(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(
                    new BeanItemContainer(Account.class, repo.findAll()));
        }
        else {
            grid.setContainerDataSource(new BeanItemContainer(Account.class,
                    repo.findByLoginStartsWithIgnoreCase(text)));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
