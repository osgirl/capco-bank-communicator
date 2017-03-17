package com.capco.selenium.component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by SBRJ on 14. 3. 2017.
 */
public class Config {

    private BrowserType browser;
    private String baseUrl;
    private InputStream inputStream;
    private long timeout;
    private static Config config;

    public long getTimeout() {
        return timeout;
    }

    public BrowserType getBrowser() {
        return browser;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private void loadProps() {

        try {
            inputStream = Config.class.getClass().getResourceAsStream("/config.properties");
            Properties prop7s = new Properties();
            prop7s.load(inputStream);

            prop7s.getProperty("baseUrl");
            browser = BrowserType.valueOf(prop7s.getProperty("browser"));
            baseUrl = prop7s.getProperty("baseUrl");
            timeout = Long.parseLong(prop7s.getProperty("pageTimeout"));

            inputStream.close();
        } catch(IOException ioe) {
            throw new RuntimeException("Properties are not loaded!");
        }

    }

    public static Config getConfig()  {
        if (config == null){
            config = new Config();
            config.loadProps();
        }
        return config;
    }

}
