package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory; // Import this!

// Import the static action variable
import static com.orangehrm.base.BaseClass.action;

import java.io.IOException;

public class HomePage {

    // Legacy support for your old tests
    private ActionDriver actionDriver;

    // --- Constructor ---
    public HomePage() {
        // 1. Initialize PageFactory elements (CRITICAL for @FindBy to work)
        PageFactory.initElements(BaseClass.getDriver(), this);

        // 2. Initialize legacy driver support
        this.actionDriver = BaseClass.getActionDriver();
    }

    // --- Locators (Old Style: By) ---
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']/img");

    // --- Locators (New Style: WebElement) ---
    @FindBy(xpath = "//span[text()='PIM']")
    WebElement pimTab;

    @FindBy(xpath = "//a[text()='Add Employee']")
    WebElement addEmployeeLink;

    // ... existing locators ...

    @FindBy(xpath = "//a[text()='Employee List']")
    WebElement employeeListTab;


    // --- Methods (Old Style) ---
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

    // --- Methods (New Style) ---
    public AddEmployeePage clickOnPIM() throws IOException {
        // CORRECT: Using the static 'action' variable with WebElement
        action.click(pimTab);
        action.click(addEmployeeLink);
        return new AddEmployeePage();
    }

    // ... existing methods ...

    public EmployeeListPage clickOnEmployeeList() throws IOException {
        action.click(pimTab);
        action.click(employeeListTab);
        return new EmployeeListPage(); // We will create this class next!
    }
}