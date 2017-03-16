package com.capco.communicator.view.component;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.schema.Account;
import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.ApplicationUI;
import com.capco.communicator.view.page.ViewType;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import static com.capco.communicator.view.ApplicationUI.SESSION_PARAM_USER;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@UIScope
public class NavigationMenu extends CustomComponent {

    private AccountRepository accountRepository;
    private AuthService authService;

    private static final String ID = "dashboard-menu";

    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private static final String STYLE_SELECTED = "selected";

    private CssLayout menuItemsLayout;

    public NavigationMenu(AccountRepository accountRepository, AuthService authService) {
        this.accountRepository = accountRepository;
        this.authService = authService;

        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("Capco <strong>Bank Communicator</strong>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private Account getCurrentAccount() {
        String login = (String)VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER);
        return accountRepository.findByLogin(login);
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");

        final Account account = getCurrentAccount();

        MenuBar.MenuItem settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
        settingsItem.setText(account.getFirstName() + " " + account.getLastName());
        settingsItem.addItem("Sign Out", new MenuBar.Command() {

            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                authService.logout((String) VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER));
                ((ApplicationUI)getUI()).updateContent();
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new Button.ClickListener() {

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        for (final ViewType view : ViewType.values()) {
            Component menuItemComponent = new ValoMenuItemButton(view);

            if(view.equals(ViewType.DASHBOARD)){
                menuItemComponent.addStyleName(STYLE_SELECTED);
            }

            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;

    }

    @Override
    public void attach() {
        super.attach();
    }

    public final class ValoMenuItemButton extends Button {

        private final ViewType view;

        ValoMenuItemButton(final ViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));

            addClickListener(new ClickListener() {

                @Override
                public void buttonClick(final ClickEvent event) {
                    for (Component button : menuItemsLayout) {
                        button.removeStyleName(STYLE_SELECTED);
                    }

                    UI.getCurrent().getNavigator().navigateTo(view.getViewName());

                    if (getUI().getNavigator().getCurrentView().getClass().equals(view.getViewClass())) {
                        addStyleName(STYLE_SELECTED);
                    }
                }
            });
        }
    }
}
