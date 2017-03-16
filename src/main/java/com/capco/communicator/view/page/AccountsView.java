package com.capco.communicator.view.page;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.schema.Account;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = AccountsView.VIEW_NAME)
public class AccountsView extends Panel implements View{

    public static final String VIEW_NAME = "accounts";

    @Autowired
    private AccountRepository repo;

    private Grid grid;
    private TextField filter;

    @PostConstruct
    void init(){
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        VerticalLayout root = new VerticalLayout();
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

        Label titleLabel = new Label("Accounts");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildBody(){
        VerticalLayout body = new VerticalLayout();

        this.grid = new Grid();
        this.filter = new TextField();

        HorizontalLayout actions = new HorizontalLayout(filter);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid);
        body.addComponent(mainLayout);

        //Configure layouts and components
        actions.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "login", "firstName", "lastName");

        //Init the filter
        filter.setInputPrompt("Filter by login");
        filter.addTextChangeListener(e -> listAccounts(e.getText()));

        //Initialize listing
        listAccounts(null);

        return body;
    }

    void listAccounts(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(new BeanItemContainer(Account.class, repo.findAll()));
        }else {
            grid.setContainerDataSource(new BeanItemContainer(Account.class,
                    repo.findByLoginStartsWithIgnoreCase(text)));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
