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

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {
    protected static Properties prop;
//    protected static WebDriver driver;
//    private static ActionDriver actionDriver;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);



    @BeforeSuite
    public void loadConfig() throws IOException {
        // Load config properties
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        logger.info("Configuration properties loaded successfully.");

        // Start the extent report
        ExtentManager.getReporter();

    }
    @BeforeMethod
    // Initialize WebDriver based on the browser specified in properties
    public synchronized void setup() throws IOException {
        System.out.println("Setting Up WebDriver for: " +this.getClass().getSimpleName());
        lauchBrowser();
        cofigureBrowser();
        staticwait(2);

        logger.info("WebDriver Instance Created");
        logger.trace("Trace Message)");
        logger.error("Error Message");
        logger.debug("debug Message");
        logger.fatal("fatal Message");
        logger.warn("Warn Message");
/*
        //Initialize the actionDriver only once
        if (actionDriver == null) {
            actionDriver = new ActionDriver(driver);
            logger.info("ActionDriver instance is created. "+Thread.currentThread().getId());
        }*/

    //initialize ActionDriver for the current Thread
    actionDriver.set(new ActionDriver(getDriver()));
    logger.info("ActionDriver instance is created for Thread: " + Thread.currentThread().getId());

    }

    // Launch browser based on configuration
    private synchronized void lauchBrowser() {
        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            //driver = new ChromeDriver();
            driver.set(new ChromeDriver());
            ExtentManager.registerDriver(getDriver());
            logger.info("Chrome Browser Launched");
        } else if (browser.equalsIgnoreCase("firefox")) {
            //driver = new FirefoxDriver();
            driver.set(new FirefoxDriver());
            ExtentManager.registerDriver(getDriver());
            logger.info("Firefox Browser Launched");
        } else if (browser.equalsIgnoreCase("edge")) {
            //driver = new EdgeDriver();
            driver.set(new EdgeDriver());
            ExtentManager.registerDriver(getDriver());
            logger.info("Edge Browser Launched");
        } else {
            throw new IllegalArgumentException("Browser not supported");
        }

    }


    //configuring browser methods
    private void cofigureBrowser() throws IOException {
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        // Maximize window
        driver.get().manage().window().maximize();
        // Navigate to URL
        try{
        driver.get().get(prop.getProperty("url"));
        }catch (Exception e){
            System.out.println("Unable to navigate to the URL: "+ e.getMessage());
        }
    }



    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                logger.info("Error while quitting the driver: " + e.getMessage());
            }
        }
        logger.info("WebDriver isntance closed.");
        driver.remove();
        actionDriver.remove();
//        driver = null;
//        actionDriver = null;
        ExtentManager.endTest();
    }
    /*

    //Driver getter method
    public WebDriver getDriver() {
        return driver;
    }*/

    //Getter method for Webdriver
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            throw new IllegalStateException("WebDriver has not been initialized. Call setup() method first.");
        }
        return driver.get();
    }

    //Getter method for ActionDriver
    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            logger.info("ActionDriver is not initialized");
            throw new IllegalStateException("WebDriver has not been initialized. Call setup() method first.");
        }
        return actionDriver.get();
    }

    //Getter Method for prop
    public static Properties getProp(){
        return prop;
    }
    //Driver Setter Method
    public void setDriver(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
    }

    public void staticwait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }


    }
