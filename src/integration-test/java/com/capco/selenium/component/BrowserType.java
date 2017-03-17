package com.capco.selenium.component;

/**
 * Created by SBRJ on 14. 3. 2017.
 */
public enum BrowserType {
    Firefox("Firefox"), Chrome("Chrome"), IE("IE"), Safari("Safari"), Opera("Opera");

    private final String browserValue;

    private BrowserType(String browserStackValue) {
        this.browserValue = browserStackValue;
    }

    public String getBrowserValue() {
        return browserValue;
    }
}
