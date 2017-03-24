package com.capco.communicator.view.page;

import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.schema.Bank;
import com.capco.communicator.view.component.BankEditor;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = BanksView.VIEW_NAME)
public class BanksView extends Panel implements View{

    public static final String VIEW_NAME = "banks";

    @Autowired
    private BankRepository repo;

    @Autowired
    private BankEditor editor;

    private Table table;
    private TextField filter;

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

        HorizontalLayout tools = new HorizontalLayout(buildFilter(), buildNewBankButton());
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

        listBanks(null);
        table.setVisibleColumns("id", "code", "name");
        table.setColumnHeaders("id", "code", "name");

        table.setFooterVisible(true);
        table.setColumnFooter("time", "Total");

        table.addValueChangeListener(e -> {
            Bank selected = (Bank)e.getProperty().getValue();
            if (selected == null || selected.getId() == null) {
                editor.setVisible(false);
            }else {
                editor.editBank(selected);
            }
        });

        table.setImmediate(true);
        table.setFooterVisible(false);

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listBanks(filter.getValue());
        });

        listBanks(null);

        VerticalLayout body = new VerticalLayout(table, editor);
        body.setExpandRatio(table, 1);;
        body.setStyleName(ValoTheme.PANEL_BORDERLESS);
        return body;
    }

    private Button buildNewBankButton() {
        Button addNewBank = new Button("New Bank", FontAwesome.PLUS);
        addNewBank.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                editor.editBank(new Bank("", ""));
            }
        });
        addNewBank.setEnabled(true);
        return addNewBank;
    }

    private Component buildFilter() {
        filter = new TextField();
        filter.setInputPrompt("Filter by last name");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        filter.addTextChangeListener(e -> listBanks(e.getText()));
        return filter;
    }

    void listBanks(String text) {
        if (StringUtils.isEmpty(text)) {
            table.setContainerDataSource(new BeanItemContainer(Bank.class, repo.findAll()));
        } else {
            table.setContainerDataSource(new BeanItemContainer(Bank.class,
                    repo.findByCodeStartsWithIgnoreCase(text)));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
