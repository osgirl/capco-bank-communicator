package com.capco.selenium.test;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;

/**
 * Created by SBRJ on 27. 3. 2017.
 */
public class PureWebdriverTest {

    @Test
    public void googlePageTest() throws IOException {
        System.setProperty("webdriver.chrome.driver",
                new File(".").getCanonicalPath() + "\\src\\drivers\\chromedriver.exe");
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
