package com.capco.selenium.component;

import org.openqa.selenium.WebDriver;
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

    LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public static LoginPage navigateToLoginPage(WebDriver driver){
        LoginPage loginPage = new LoginPage(driver);
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
        return new HomePage(driver);
    }

    public LoginPage wrongLogin(String username, String password){
        login(username, password);
        return new LoginPage(driver);
    }

    private void login(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        enterButton.click();
    }
}
