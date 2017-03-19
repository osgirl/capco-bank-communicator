package com.capco.communicator.view.component;

import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.schema.Bank;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A simple example to introduce building forms. As your real application is
 * probably much more complicated than this example, you could re-use this form in
 * multiple places. This example component is only used in VaadinUI.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX. See e.g. AbstractForm in Viritin
 * (https://vaadin.com/addon/viritin).
 */
@SpringComponent
@UIScope
public class BankEditor extends VerticalLayout {

	private final BankRepository repository;

	/**
	 * The currently edited bank
	 */
	private Bank bank;

	/* Fields to edit properties in bank entity */
	public TextField code = new TextField("Code");
	public TextField name = new TextField("Name");

	/* Action buttons */
	public Button save = new Button("Save", FontAwesome.SAVE);
	public Button cancel = new Button("Cancel");
	public Button delete = new Button("Delete", FontAwesome.TRASH_O);
	public CssLayout actions = new CssLayout(save, cancel, delete);

	@Autowired
	public BankEditor(BankRepository repository) {
		this.repository = repository;

		addComponents(code, name, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> repository.save(bank));
		delete.addClickListener(e -> repository.delete(bank));
		cancel.addClickListener(e -> editBank(bank));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editBank(Bank b) {
		final boolean persisted = b.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			bank = repository.findOne(b.getId());
		}
		else {
			bank = b;
		}
		cancel.setVisible(persisted);

		// Bind bank properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(bank, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in code field automatically
		code.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
		cancel.addClickListener(e -> h.onChange());
	}

}
