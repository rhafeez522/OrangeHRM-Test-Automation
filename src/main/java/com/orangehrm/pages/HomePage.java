package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class HomePage {
    // HomePage implementation goes here
    private ActionDriver actionDriver;


    //Locators for HomePage elements
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']/img");
    //Constructor
    /*public HomePage(WebDriver driver) {
        this.actionDriver =new ActionDriver(driver);
    }*/

    public HomePage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();

    }

    //Methods to interact with HomePage elements
    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }
    public boolean verifyOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMLogo);
    }
    public void logout() throws IOException {
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);
    }

}
