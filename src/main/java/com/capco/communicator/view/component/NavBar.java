package com.capco.communicator.view.component;

import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.page.AccountsPage;
import com.capco.communicator.view.page.BanksPage;
import com.capco.communicator.view.page.DashboardPage;
import com.capco.communicator.view.page.PaymentContextsPage;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringComponent
@UIScope
public class NavBar extends CssLayout{

    private final AuthService authService;

//    private TestIcon testIcon = new TestIcon(100);

//    private CssLayout menu = new CssLayout();
    private CssLayout menuItemsLayout = new CssLayout();
    private HashMap<String, String> menuItems = new LinkedHashMap<>();

    @Autowired
    public NavBar(AuthService authService){
        this.authService = authService;

        initLayout();
    }

    private void initLayout(){
        buildMenu();
//        addComponent(buildMenu());

//        getUI().getNavigator().addViewChangeListener(new ViewChangeListener() {
//
//            @Override
//            public boolean beforeViewChange(ViewChangeEvent event) {
//                return true;
//            }
//
//            @Override
//            public void afterViewChange(ViewChangeEvent event) {
//                for (Component menuItem : menuItemsLayout) {
//                    menuItem.removeStyleName("selected");
//                }
//
//                for (Map.Entry<String, String> item : menuItems.entrySet()) {
//                    if (event.getViewName().equals(item.getKey())) {
//                        for (Component c : menuItemsLayout) {
//                            if (c.getCaption() != null && c.getCaption().startsWith(item.getValue())) {
//                                c.addStyleName("selected");
//                                break;
//                            }
//                        }
//                        break;
//                    }
//                }
//                menu.removeStyleName("valo-menu-visible");
//            }
//        });
    }

    private void buildMenu() {
        // Add menu items
        menuItems.put(DashboardPage.VIEW_NAME, "Main Dashboard");
        menuItems.put(BanksPage.VIEW_NAME, "Banks");
        menuItems.put(AccountsPage.VIEW_NAME, "Accounts");
        menuItems.put(PaymentContextsPage.VIEW_NAME, "Payment Contexts");

        //Build top layout
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        addComponent(top);

        Button showMenu = new Button("Menu", (Button.ClickListener) event -> {
            if (getStyleName().contains("valo-menu-visible")) {
                removeStyleName("valo-menu-visible");
            } else {
                addStyleName("valo-menu-visible");
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName("valo-menu-toggle");
        showMenu.setIcon(FontAwesome.LIST);
        addComponent(showMenu);

        Label title = new Label("<h3>Capco <strong>Bank Communicator</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);

        MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        MenuBar.MenuItem settingsItem = settings.addItem("aa" + "ab" + "cc",
                new ClassResource("/img/profile-pic-300px.jpg"),
                null);
        settingsItem.addItem("Edit Profile", null);
        settingsItem.addItem("Preferences", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", null);
        addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        addComponent(menuItemsLayout);

//        Label label = null;
//        int count = -1;

        for (final Map.Entry<String, String> item : menuItems.entrySet()) {
            Button b = new Button(item.getValue(), new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    getUI().getNavigator().navigateTo(item.getKey());
                }
            });

            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
//            b.setIcon(testIcon.get());
            menuItemsLayout.addComponent(b);
        }

//        return menu;
    }

//    private Button createLogoutButton(){
//        Button logout = new Button("logout");
//        logout.addStyleName(ValoTheme.BUTTON_SMALL);
//
//        logout.addClickListener((Button.ClickListener) clickEvent -> {
//            authService.logout((String) VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER));
//            getUI().getNavigator().navigateTo(LoginPage.VIEW_NAME);
//        });
//
//        return logout;
//    }
}
