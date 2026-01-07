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
        int explicitwait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        logger.info("WebDriver instance is created in ActionDriver");
    }

    //Method to click an Element
    public void click(By by) throws IOException {
        String elementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logstep("Clicked on element: " + elementDescription);
            logger.info("Clicked on element"+ elementDescription);
        } catch (Exception e) {
            System.out.println("Unable to click on element: " + e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:", elementDescription+"Unable to click on element: " + e.getMessage());
            logger.error("Unable to click on element: " + e.getMessage());
        }
    }

    //method to enter text in the textfield
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            //driver.findElement(by).clear();
            //driver.findElement(by).sendKeys(value);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Entered text: " +getElementDescription(by) +" "+value);
        } catch (Exception e) {
            logger.error("Unable to enter text in element: " + e.getMessage());
        }
    }

    //Method to get text from input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get text from element: " + e.getMessage());
            return "";
        }
    }

    //Method to compare two strings
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                logger.info("Text matches: " + actualText);
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Compare Text", "Text Verified Successfully! "+actualText + " equals " + expectedText);
                
                return true;
            } else {
                logger.error("Text does not match. Expected: " + expectedText + ", Actual: " + actualText);
                ExtentManager.logFailure(BaseClass.getDriver(),"Compare Text", "Text Comparison Failed "+actualText + "Not equals " + expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("Unable to compare text: " + e.getMessage());
        }
    return false;
    }

    //Method to check if an element is displayed

    /*
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);

            boolean isDesplayed = driver.findElement(by).isDisplayed();
            if (isDesplayed) {
                System.out.println("Element is displayed");
            } else {
                System.out.println("Element is not displayed");
            }
        } catch (Exception e) {
            System.out.println("Unable to determine if element is displayed: " + e.getMessage());
            return false;
        }

        return false;
    }
     */
    //simple isDisplayed method
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            logger.info("Element is displayed: " + getElementDescription(by));
            ExtentManager.logstep("Element is displayed: " + getElementDescription(by));
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            logger.error("Element not displayed: " + e.getMessage());
            //ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed: " , "Element is not displayed: " + getElementDescription(by));
            return false;
        }
    }


    //wait fo page to load
    public void waitForPageToLoad(int timeOutInSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver).executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully");
        }
        catch (Exception e){
            logger.error("Page did not load within " + timeOutInSec + " seconds: " + e.getMessage());
        }
    }
    //scroll to element
    public void scrollToElement(By by){
        try{
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(by);
        js.executeScript("arguments[0], scrollIntoView(true);", element);
        } catch (Exception e) {
            logger.error("Unable to scroll to element: " + e.getMessage());
        }
    }
    //wait for element to be clickable
    private void waitForElementToBeClickable(By by) {

        try {
        wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("Element not clickable: " + e.getMessage());
        }
    }
    //wait for element to be visible
    private void waitForElementToBeVisible(By by) {

        try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element not visible: " + e.getMessage());
        }
    }

    //Method to get the description of an element
    public String getElementDescription(By locator){
        //check for null driver or locator to avoid NullPointer exception
        if (driver == null)
            return "Driver is null";
        if (locator == null)
                return "Locator is null";
        //find the element using locator
        try {
            WebElement element = driver.findElement(locator);

            //get element description
            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String className = element.getDomAttribute("class");
            String text = element.getDomAttribute("text");
            String placeHolder = element.getDomAttribute("placeholder");

            //return the description based on element attributes
            if (isNotEmpty(name)){
                return "Element with name:"+name;
            }
            else if(isNotEmpty(id)){
                return "Element with id:"+id;
            }
            else if(isNotEmpty(className)){
                return "Element with class:"+className;
            }
            else if(isNotEmpty(text)){
                return "Element with text:"+truncate(text,30);
            }
            else if(isNotEmpty(placeHolder)){
                return "Element with placeholder:"+placeHolder;

            }
        } catch (Exception e) {
            logger.error("Unable to get element description: " + e.getMessage());
        }
        return "Unalbe to describe element";
    }
    //Utililty method to check the string is not null or empty
    private boolean isNotEmpty(String value){
        return value!=null && !value.isEmpty();

    }
    //utility method to truncate long string
    private String truncate(String value, int maxlength){
        if (value==null||value.length()<=maxlength){
            return value;
        }
        return value.substring(0,maxlength)+"...";
    }



}
