package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop;

    // ThreadLocal for WebDriver (Thread Safe)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // ThreadLocal for ActionDriver (Legacy Support)
    private static ThreadLocal<ActionDriver> actionDriverRepo = new ThreadLocal<>();

    // NEW STATIC VARIABLE (For PageFactory Pages like AddEmployeePage)
    public static ActionDriver action;

    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void loadConfig() {
        try {
            prop = new Properties();
            String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config.properties";

            FileInputStream fis = new FileInputStream(path);
            prop.load(fis);
            logger.info("Configuration properties loaded successfully from: " + path);
            ExtentManager.getReporter();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to load config.properties.");
        }
    }

    @BeforeMethod
    public synchronized void setup() throws IOException {
        logger.info("Setting up WebDriver for class: " + this.getClass().getSimpleName());

        launchBrowser();
        configureBrowser();
        staticWait(2);

        // --- CRITICAL FIX: Initialize BOTH variables ---

        // 1. Initialize the object
        ActionDriver act = new ActionDriver(getDriver());

        // 2. Assign to Static Variable (For New PageFactory tests)
        action = act;

        // 3. Assign to ThreadLocal (For Old Legacy tests)
        actionDriverRepo.set(act);

        logger.info("ActionDriver initialized for Thread ID: " + Thread.currentThread().getId());
    }

    private synchronized void launchBrowser() {
        if (prop == null) throw new RuntimeException("Config properties not loaded.");
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            driver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver.set(new FirefoxDriver());
        } else if (browser.equalsIgnoreCase("edge")) {
            driver.set(new EdgeDriver());
        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        ExtentManager.registerDriver(getDriver());
    }

    private void configureBrowser() {
        try {
            String waitProp = prop.getProperty("implicitWait");
            int implicitWait = (waitProp != null) ? Integer.parseInt(waitProp) : 10;
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            getDriver().manage().window().maximize();
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            logger.error("Error during browser configuration: " + e.getMessage());
        }
    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                logger.error("Error while quitting: " + e.getMessage());
            }
        }
        driver.remove();
        actionDriverRepo.remove();
        ExtentManager.endTest();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    // OLD Getter (Kept for Legacy Tests)
    public static ActionDriver getActionDriver() {
        return actionDriverRepo.get();
    }

    public static Properties getProp() {
        return prop;
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}