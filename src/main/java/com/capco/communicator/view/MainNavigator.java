package com.capco.communicator.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@UIScope
public class MainNavigator extends Navigator{

    public MainNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);
        initViewChangeListener();
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // Since there's no conditions in switching between the views
                // we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {}
        });
    }
}
