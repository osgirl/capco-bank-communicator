package com.capco.selenium.test;

import com.capco.selenium.component.HomePage;
import com.capco.selenium.component.LoginPage;
import com.capco.selenium.component.MyRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by SBRJ on 14. 3. 2017.
 */
@RunWith(MyRunner.class)
public class LoginTest extends BaseTest{

    @Test
    public void appearanceOfLoginPageTest() {
        LoginPage loginPage = LoginPage.navigateToLoginPage(driver);

        Assert.assertTrue(loginPage.isUsernameInputDisplayed());
        Assert.assertTrue(loginPage.isPasswordInputDisplayed());
        Assert.assertTrue(loginPage.isEnterButtonDisplayed());
    }

    @Test
    public void givenWrongCredentialsWhenLoginThenNotLoggedIn() {
        LoginPage loginPage = LoginPage.navigateToLoginPage(driver);
        loginPage.wrongLogin("wrong_name", "wrong_password");

        Assert.assertTrue(loginPage.isEnterButtonDisplayed());
    }

    @Test
    public void givenCorrectCredentialsWhenLoginDashboardIsDisplayed() {
        LoginPage loginPage = LoginPage.navigateToLoginPage(driver);
        HomePage homePage = loginPage.successfulLogin("test", "123456");

        Assert.assertTrue(homePage.isHomeButtonPresent());
    }

}
