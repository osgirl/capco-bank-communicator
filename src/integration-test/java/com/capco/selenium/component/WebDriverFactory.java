package com.capco.selenium.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by SBRJ on 14. 3. 2017.
 */
public class WebDriverFactory {

    private WebDriverFactory(){}

    public static WebDriver createWebdriver(){
        BrowserType browserType = Config.getConfig().getBrowser();
        WebDriver webDriver = createWebDriverByBrowserType(browserType);
        webDriver.manage().timeouts().implicitlyWait(Config.getConfig().getTimeout(), TimeUnit.SECONDS);
        return webDriver;
    }

    public static void closeWebdriver(WebDriver webDriver){
        if(webDriver != null) {
            webDriver.close();
        }
    }

    private static WebDriver createWebDriverByBrowserType(BrowserType browserType) throws RuntimeException {
        switch (browserType){
            case Firefox: return new FirefoxDriver();
            case Chrome: return new ChromeDriver();
            default: throw new RuntimeException("Required browser is not applicable for test!");
        }
    }

}
