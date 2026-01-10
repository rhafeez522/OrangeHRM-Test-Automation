package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.AddEmployeePage;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExcelUtil; // Import ExcelUtil
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddEmployeeTest extends BaseClass {

    LoginPage loginPage;
    HomePage homePage;
    AddEmployeePage addEmpPage;

    @BeforeMethod
    public void initialize() {
        loginPage = new LoginPage();
        homePage = new HomePage();
        addEmpPage = new AddEmployeePage();
    }

    // CONNECTING TO EXCEL HERE:
    @Test(dataProvider = "AddEmployeeData", dataProviderClass = ExcelUtil.class)
    public void addEmployeeTest(String fName, String lName) throws IOException { // Add Arguments

        ExtentManager.logstep("Test Started for: " + fName + " " + lName);

        // 1. Login
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

        // 2. Navigate
        addEmpPage = homePage.clickOnPIM();

        // 3. Use Excel Data
        addEmpPage.addEmployee(fName, lName);
        ExtentManager.logstep("Entered Employee Details: " + fName + " " + lName);

        // 4. Validate
        boolean isProfilePageLoaded = addEmpPage.validateEmployeeAdded();

        if (isProfilePageLoaded) {
            ExtentManager.logstep("Success: Employee " + fName + " created.");
            Assert.assertTrue(true);
        } else {
            ExtentManager.logstep("Failure: Could not create " + fName);
            Assert.fail("Test Failed for user: " + fName);
        }
    }
}