package com.capco.selenium.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by SBRJ on 27. 3. 2017.
 */
public class PureWebdriverTest {

    @Test
    public void googlePageTest(){
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://www.google.com");
        WebElement searchWebElement = webDriver.findElement(By.name("q"));
        Assert.assertTrue(searchWebElement.isDisplayed());
        searchWebElement.sendKeys("Ahoj webdriver");
        searchWebElement.submit();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
