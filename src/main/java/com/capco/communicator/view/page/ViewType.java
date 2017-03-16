package com.capco.communicator.view.page;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ViewType {

    DASHBOARD(DashboardView.VIEW_NAME, DashboardView.class, FontAwesome.HOME),
    BANKS(BanksView.VIEW_NAME, BanksView.class, FontAwesome.BAR_CHART_O),
    ACCOUNTS(AccountsView.VIEW_NAME, AccountsView.class, FontAwesome.TABLE),
    PAYMENT_CONTEXT(PaymentContextsView.VIEW_NAME, PaymentContextsView.class, FontAwesome.FILE_TEXT_O);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;

    ViewType(final String viewName, final Class<? extends View> viewClass,
                     final Resource icon) {

        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static ViewType getByViewName(final String viewName) {
        ViewType result = null;
        for (ViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
}
