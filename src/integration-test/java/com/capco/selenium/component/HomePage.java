package com.capco.selenium.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by SBRJ on 16. 3. 2017.
 */
public class HomePage extends BasePage {

    @FindBy(linkText = "Home")
    private WebElement homeButton;

    HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void baseLoadConditionForPage() {
        webDriverWait.until(ExpectedConditions.visibilityOf(homeButton));
    }

    public boolean isHomeButtonPresent(){
        return homeButton.isDisplayed();
    }
}
