package com.orangehrm.pages;

import com.orangehrm.base.BaseClass;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// Import static action so we can use it directly
import java.io.IOException;

import static com.orangehrm.base.BaseClass.action;

public class AddEmployeePage {

    // --- Locators ---
    @FindBy(name = "firstName")
    WebElement firstName;

    @FindBy(name = "lastName")
    WebElement lastName;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement saveBtn;

    // FIX: Use 'contains' and 'normalize-space' to ignore extra spaces/padding
    @FindBy(xpath = "//h6[contains(normalize-space(), 'Personal Details')]")
    WebElement personalDetailsHeader;

    // --- Constructor (CRITICAL) ---
    public AddEmployeePage() {
        // Without this line, the elements above are NULL and the test fails immediately
        PageFactory.initElements(BaseClass.getDriver(), this);
    }

    // --- Actions ---
    public void addEmployee(String fName, String lName) throws IOException {
        action.type(firstName, fName);
        action.type(lastName, lName);
        action.click(saveBtn);
    }

    /**
     * Verifies if the user is redirected to the Personal Details page.
     */
    public boolean validateEmployeeAdded() {
        return action.isDisplayed(personalDetailsHeader);
    }
}