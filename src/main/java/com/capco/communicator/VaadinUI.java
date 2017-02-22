package com.capco.communicator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

	private final BankRepository repo;

	private final BankEditor editor;

	final Grid grid;

	final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public VaadinUI(BankRepository repo, BankEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("New bank", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "code", "name");

		filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listBanks(e.getText()));

		// Connect selected Bank to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editBank((Bank) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Bank
		// the new button is clicked
		addNewBtn.addClickListener(e -> editor.editBank(new Bank("", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listBanks(filter.getValue());
		});

		// Initialize listing
		listBanks(null);
	}

	// tag::listBanks[]
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
	// end::listBanks[]

}
