package com.capco.communicator.view;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.service.AuthService;
import com.capco.communicator.view.page.DashboardView;
import com.capco.communicator.view.page.LoginView;
import com.capco.communicator.view.page.MainView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;


@SpringUI
@Theme("dashboard")
@Title("Capco Bank Communicator")
@SpringComponent
@SpringViewDisplay
@PreserveOnRefresh
public class ApplicationUI extends UI{

    private final SpringViewProvider viewProvider;

    @Autowired
    public ApplicationUI(SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountRepository accountRepository;

    public static final String SESSION_PARAM_USER = "user";

    @Override
    public void init(VaadinRequest vaadinRequest) {
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    public void updateContent() {
        String login = (String)VaadinSession.getCurrent().getAttribute(SESSION_PARAM_USER);
        if (login != null && authService.isUserLogged(login)) {
            setContent(new MainView(accountRepository, authService));
            getNavigator().addProvider(viewProvider);
            getNavigator().navigateTo(DashboardView.VIEW_NAME);
            removeStyleName("loginview");
        } else {
            setContent(new LoginView(authService));
            addStyleName("loginview");
        }
    }
}
