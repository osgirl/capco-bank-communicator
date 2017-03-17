package com.capco.selenium.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by SBRJ on 14. 3. 2017.
 */
public abstract class BasePage implements Assertable {

    protected static WebDriver driver;
    protected WebDriverWait webDriverWait;
    protected static final String baseUrl = Config.getConfig().getBaseUrl();


    BasePage() {
        driver = WebDriverProvider.getInstance();
        webDriverWait = new WebDriverWait(driver, 6);
        PageFactory.initElements(driver, this);
    }

}
