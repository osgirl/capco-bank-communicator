package com.capco.communicator.view;

import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.page.AccountsPage;
import com.capco.communicator.view.page.BanksPage;
import com.capco.communicator.view.page.DashboardPage;
import com.capco.communicator.view.page.LoginPage;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;


@SpringUI
@Theme("valo")
@SpringViewDisplay
public class ApplicationUI extends UI implements ViewDisplay{

    public static final String SESSION_PARAM_USER = "user";

    @Autowired
    private AuthService authService;

    private Panel springViewDisplay;
    private static CssLayout navigationBar;

    @Override
    public void init(VaadinRequest vaadinRequest) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        navigationBar = new CssLayout();

        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        navigationBar.addComponent(createNavigationButton("Home",
                DashboardPage.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Banks",
                BanksPage.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Accounts",
                AccountsPage.VIEW_NAME));
        navigationBar.addComponent(createLogoutButton());

        setNavBarVisible(authService.isUserLogged((String)VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER)));

        root.addComponent(navigationBar);

        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);
    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);

        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    private Button createLogoutButton(){
        Button logout = new Button("logout");
        logout.addStyleName(ValoTheme.BUTTON_SMALL);

        logout.addClickListener((Button.ClickListener) clickEvent -> {
            authService.logout((String)VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER));
            getUI().getNavigator().navigateTo(LoginPage.VIEW_NAME);
            setNavBarVisible(false);
        });
        
        return logout;
    }

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }

    public static void setNavBarVisible(boolean visible){
        navigationBar.setVisible(visible);
    }
}
