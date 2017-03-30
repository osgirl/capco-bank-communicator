package com.capco.communicator.view.page;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.schema.Account;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
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
@SpringView(name = AccountsView.VIEW_NAME)
public class AccountsView extends Panel implements View{

    public static final String VIEW_NAME = "accounts";

    @Autowired
    private AccountRepository repo;

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

        Label titleLabel = new Label("Banks");
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

        listAccounts(null);
        table.setVisibleColumns("id", "code", "login", "firstName", "lastName");
        table.setColumnHeaders("id", "code", "login", "firstName", "lastName");

        table.setFooterVisible(true);
//        table.setColumnFooter("time", "Total");

//        table.addValueChangeListener(e -> {
//            Account selected = (Account)e.getProperty().getValue();
//            if (selected == null || selected.getId() == null) {
//                editor.setVisible(false);
//            }else {
//                editor.editBank(selected);
//            }
//        });

        table.setImmediate(true);
        table.setFooterVisible(false);


//        editor.setChangeHandler(() -> {
//            editor.setVisible(false);
//            listBanks(filter.getValue());
//        });

        listAccounts(null);

        VerticalLayout body = new VerticalLayout(table);
        body.setExpandRatio(table, 1);
        body.setStyleName(ValoTheme.PANEL_BORDERLESS);
        return body;
    }

    private Component buildSearch() {
        TextField filter = new TextField();
        filter.setInputPrompt("Filter by login");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        filter.addTextChangeListener(e -> listAccounts(e.getText()));
        return filter;
    }

    void listAccounts(String text) {
        if (StringUtils.isEmpty(text)) {
            table.setContainerDataSource(new BeanItemContainer(
                    Account.class, IteratorUtils.toList(repo.findAll().iterator())));
        } else {
            table.setContainerDataSource(new BeanItemContainer(Account.class,
                    repo.findByLoginStartsWithIgnoreCase(text)));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
