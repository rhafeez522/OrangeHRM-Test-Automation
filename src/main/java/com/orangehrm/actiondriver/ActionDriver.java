package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        String waitProp = BaseClass.getProp().getProperty("explicitWait");
        int explicitWait = (waitProp != null) ? Integer.parseInt(waitProp) : 30;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("ActionDriver initialized.");
    }

    // ================================================================
    //  SECTION 1: NEW METHODS FOR WEBELEMENT (@FindBy Support)
    // ================================================================

    /**
     * Click method for WebElement.
     * Handles exceptions internally to keep Page classes clean.
     */
// Updated Click Method with JavaScript Fallback
    public void click(WebElement element) throws IOException {
        try {
            // 1. Try Normal Click first
            waitForElementToBeClickable(element);
            element.click();
            logger.info("Clicked on element: " + element.toString());
            ExtentManager.logstep("Clicked on element");
        } catch (Exception e) {
            // 2. If Normal Click fails, try JavaScript "Force Click"
            logger.info("Standard click failed, attempting JavaScript click...");
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
                logger.info("JavaScript click successful on: " + element.toString());
                ExtentManager.logstep("JS Clicked on element");
            } catch (Exception jsException) {
                // 3. If both fail, log the error
                logger.error("All click attempts failed: " + jsException.getMessage());
                ExtentManager.logFailure(driver, "Click Failed", jsException.getMessage());
            }
        }
    }

    public void type(WebElement element, String text) {
        try {
            waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text: " + text);
            ExtentManager.logstep("Entered text: " + text);
        } catch (Exception e) {
            logger.error("Unable to enter text: " + e.getMessage());
        }
    }

    public boolean isDisplayed(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForElementToBeClickable(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable: " + e.getMessage());
        }
    }

    public void waitForElementToBeVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible: " + e.getMessage());
        }
    }

    public String getElementDescription(WebElement element) {
        try {
            return element.toString();
        } catch (Exception e) {
            return "Unknown Element";
        }
    }

    // ================================================================
    //  SECTION 2: OLD METHODS FOR BY LOCATORS (Legacy Support)
    // ================================================================

    public void click(By by) throws IOException {
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logstep("Clicked on element");
            logger.info("Clicked on element");
        } catch (Exception e) {
            logger.error("Unable to click: " + e.getMessage());
        }
    }

    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Entered text: " + value);
        } catch (Exception e) {
            logger.error("Unable to enter text: " + e.getMessage());
        }
    }

    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            return expectedText.equals(actualText);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollToElement(By by){
        try{
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            logger.error("Unable to scroll: " + e.getMessage());
        }
    }

    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("Element not clickable: " + e.getMessage());
        }
    }

    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element not visible: " + e.getMessage());
        }
    }

    public String getElementDescription(By locator){
        return locator.toString();
    }
}