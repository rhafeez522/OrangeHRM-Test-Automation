package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class LoginPage {

    private ActionDriver actionDriver;

    //Constructor to initialize ActionDriver
    /*public LoginPage(WebDriver driver) {
        this.actionDriver = new ActionDriver(driver);
    }*/

    public LoginPage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();

    }

    //Defining locators

    private By usernameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");



    public void login(String username, String password) throws IOException {
        actionDriver.enterText(usernameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }
    //Method to check if error message is displayed
    public boolean isErrorMessageDisplayed() {
        return actionDriver.isDisplayed(errorMessage);
    }

    //Method to get text from error message
    public String getErrorMessageText() {
        return actionDriver.getText(errorMessage);
    }

    //Verify error message correct or not
    public boolean verifyErrorMessage(String expectedError){
        return actionDriver.compareText(errorMessage, expectedError);

    }



}
