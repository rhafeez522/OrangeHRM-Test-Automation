package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.EmployeeListPage;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class SearchEmployeeTest extends BaseClass {

    LoginPage loginPage;
    HomePage homePage;
    EmployeeListPage employeeListPage;

    @BeforeMethod
    public void initialize() {
        loginPage = new LoginPage();
        homePage = new HomePage();
        // EmployeeListPage is initialized inside the test flow
    }

    @Test
    public void searchEmployeeTest() throws IOException {
        ExtentManager.logstep("Test Started: Search Employee");

        // 1. Login
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

        // 2. Navigate to Employee List
        employeeListPage = homePage.clickOnEmployeeList();
        ExtentManager.logstep("Navigated to Employee List");

        // 3. Search for an Existing Employee (e.g., "Raja")
        // NOTE: Make sure "Raja" actually exists in your system!
        String searchName = "Ahmed";
        employeeListPage.searchEmployee(searchName);
        ExtentManager.logstep("Searched for: " + searchName);

        // 4. Validate Result
        boolean isFound = employeeListPage.isEmployeeFound(searchName);

        if (isFound) {
            ExtentManager.logstep("Success: Employee found in table.");
            Assert.assertTrue(true);
        } else {
            ExtentManager.logstep("Failure: Employee NOT found.");
            Assert.fail("Employee not found in search results.");
        }
    }
}