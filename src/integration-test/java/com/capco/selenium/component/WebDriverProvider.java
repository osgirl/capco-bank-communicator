package com.capco.selenium.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by SBRJ on 14. 3. 2017.
 */
public class WebDriverProvider {
    private static WebDriver webDriver;

    private WebDriverProvider(){}

    public static WebDriver getInstance() throws RuntimeException {

        if(webDriver == null) {
            BrowserType browserType = Config.getConfig().getBrowser();
            webDriver = createWebDriverInstance(browserType);
            webDriver.manage().timeouts().implicitlyWait(Config.getConfig().getTimeout(), TimeUnit.SECONDS);
        }
        return webDriver;
    }

    public static void closeWebdriver(){
        if(webDriver != null) {
            webDriver.close();
            webDriver = null;
        }
    }

    private static WebDriver createWebDriverInstance(BrowserType browserType) throws RuntimeException {
        switch (browserType){
            case Firefox: return new FirefoxDriver();
            case Chrome: return new ChromeDriver();
            default: throw new RuntimeException("Required browser is not applicable for test!");
        }
    }

}
