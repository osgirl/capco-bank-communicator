package com.capco.communicator.view.component;

import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.page.*;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import static com.capco.communicator.view.ApplicationUI.SESSION_PARAM_USER;

@SpringComponent
@UIScope
public class NavBar extends CssLayout{

    private final AuthService authService;

    @Autowired
    public NavBar(AuthService authService){
        this.authService = authService;

        initLayout();
    }

    private void initLayout(){
        addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        addComponent(createNavigationButton("Home",
                DashboardPage.VIEW_NAME));
        addComponent(createNavigationButton("Banks",
                BanksPage.VIEW_NAME));
        addComponent(createNavigationButton("Accounts",
                AccountsPage.VIEW_NAME));
        addComponent(createNavigationButton("PaymentContexts",
                PaymentContextsPage.VIEW_NAME));
        addComponent(createLogoutButton());
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
            authService.logout((String) VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER));
            getUI().getNavigator().navigateTo(LoginPage.VIEW_NAME);
        });

        return logout;
    }
}
