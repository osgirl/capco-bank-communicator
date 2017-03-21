package com.capco.selenium.test;

import com.capco.selenium.component.WebDriverFactory;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;

/**
 * Created by SBRJ on 20. 3. 2017.
 */
public abstract class BaseTest {
    protected WebDriver driver;

    @Before
    public void setUp(){
        driver = WebDriverFactory.createWebdriver();
    }

    @After
    public void tearDown(){
        WebDriverFactory.closeWebdriver(driver);
        driver = null;
    }
}
