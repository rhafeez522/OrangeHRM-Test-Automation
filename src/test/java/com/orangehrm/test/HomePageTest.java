package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void initializePages() {
        // Initialize Page Objects before the test runs
        loginPage = new LoginPage();
        homePage = new HomePage();
    }

    @Test
    public void verifyOrangeHRMLogo() throws IOException {
        ExtentManager.logstep("Test Started: Verify OrangeHRM Logo on Dashboard");

        // Step 1: Login using "Golden Credentials" from config.properties
        // We use config file here because we need 100% valid login to test the Dashboard UI.
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        ExtentManager.logstep("Attempting to login with valid credentials: " + username);

        // Perform login
        loginPage.login(username, password);

        // Step 2: Verification
        ExtentManager.logstep("Login successful. Verifying OrangeHRM logo is displayed...");

        // Call the verification method from HomePage class
        boolean isLogoDisplayed = homePage.verifyOrangeHRMLogo();

        // Step 3: Assertion
        if (isLogoDisplayed) {
            ExtentManager.logstep("Validation Successful: OrangeHRM Logo is visible.");
            Assert.assertTrue(true);
        } else {
            ExtentManager.logstep("Validation Failed: OrangeHRM Logo is NOT visible.");
            Assert.fail("OrangeHRM Logo was not found on the Dashboard.");
        }
    }
}