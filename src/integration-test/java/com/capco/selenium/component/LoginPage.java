package com.capco.selenium.component;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;


/**
 * Created by SBRJ on 14. 3. 2017.
 */
public class LoginPage extends BasePage {

    @FindBy(id = "usernameinput")
    private WebElement usernameInput;

    @FindBy(id = "passwordinput")
    private WebElement passwordInput;

    @FindBy(id = "loginbutton")
    private WebElement enterButton;

    @Override
    public void baseLoadConditionForPage() {
        webDriverWait.until(ExpectedConditions.visibilityOf(usernameInput));
    }

    public static LoginPage navigateToLoginPage(){
        LoginPage loginPage = new LoginPage();
        loginPage.driver.navigate().to(baseUrl);
        return loginPage;
    }

    public boolean isUsernameInputDisplayed(){
        return usernameInput.isDisplayed();
    }

    public boolean isPasswordInputDisplayed() {
        return passwordInput.isDisplayed();
    }

    public boolean isEnterButtonDisplayed() {
        return enterButton.isDisplayed();
    }

    public HomePage successfulLogin(String username, String password){
        login(username, password);
        return new HomePage();
    }

    public LoginPage wronLogin(String username, String password){
        login(username, password);
        return new LoginPage();
    }

    private void login(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        enterButton.click();
    }
}
