package com.capco.communicator.view.page;

import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.schema.Bank;
import com.capco.communicator.view.component.BankEditor;
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
@SpringView(name = BanksPage.VIEW_NAME)
public class BanksPage extends VerticalLayout implements View {

    public static final String VIEW_NAME = "banks";

    @Autowired
    private BankRepository repo;

    @Autowired
    private BankEditor editor;

    @Autowired
    private NavBar navBar;

	public Grid grid;
    public TextField filter;
    private Button addNewBtn;

    @PostConstruct
    void init() {
        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = new Button("New bank", FontAwesome.PLUS);

		addComponent(navBar);
        initLayout();
    }

	public void initLayout() {
        //build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
        addComponent(mainLayout);

        //Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "code", "name");

		filter.setInputPrompt("Filter by last name");

        //Hook logic to components
        //Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listBanks(e.getText()));

        //Connect selected Bank to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editBank((Bank) grid.getSelectedRow());
			}
		});

        //Instantiate and edit new Bank
        //the new button is clicked
		addNewBtn.addClickListener(e -> editor.editBank(new Bank("", "")));

        //Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listBanks(filter.getValue());
		});

        //Initialize listing
		listBanks(null);
	}


	void listBanks(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(
					new BeanItemContainer(Bank.class, repo.findAll()));
		}
		else {
			grid.setContainerDataSource(new BeanItemContainer(Bank.class,
					repo.findByCodeStartsWithIgnoreCase(text)));
		}
	}


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
