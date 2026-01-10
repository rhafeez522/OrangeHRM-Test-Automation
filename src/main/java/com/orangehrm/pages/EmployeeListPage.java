package com.orangehrm.pages;

import com.orangehrm.base.BaseClass;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.util.List;

import static com.orangehrm.base.BaseClass.action;

public class EmployeeListPage {

    // --- Locators ---
    @FindBy(xpath = "//label[text()='Employee Name']/parent::div/following-sibling::div//input")
    WebElement empNameInput;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement searchBtn;

    @FindBy(xpath = "//div[@class='oxd-table-card']")
    List<WebElement> tableRows;

    // Improved Locator: Specifically looks for the 3rd CELL (First Name column)
    @FindBy(xpath = "//div[@class='oxd-table-body']//div[@role='row'][1]/div[@role='cell'][3]")
    WebElement firstRowFirstName;

    // --- Constructor ---
    public EmployeeListPage() {
        PageFactory.initElements(BaseClass.getDriver(), this);
    }

    // --- Actions ---

    public void searchEmployee(String name) throws IOException {
        action.type(empNameInput, name);
        try { Thread.sleep(2000); } catch (InterruptedException e) {} // Wait for auto-suggest
        action.click(searchBtn);
    }

    public boolean isEmployeeFound(String expectedName) {
        // Wait for search results to load
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        // Check if rows exist
        if (tableRows.size() == 0) {
            System.out.println("DEBUG: No rows found in the table.");
            return false;
        }

        // Get actual text
        String actualName = firstRowFirstName.getText();

        // DEBUG LOG: This will print in your console so you know exactly what happened
        System.out.println("DEBUG: Expected Name: '" + expectedName + "'");
        System.out.println("DEBUG: Actual Name in Table: '" + actualName + "'");

        // FIX: Use contains() to handle "Raja Hafeez" matching "Raja"
        return actualName.toLowerCase().contains(expectedName.toLowerCase());
    }
}