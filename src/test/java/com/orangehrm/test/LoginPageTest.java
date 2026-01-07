package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        // Initialize the LoginPage object
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }
    @Test

    public void verifyValidLoginTest() throws IOException {

        ExtentManager.satrtTest("Valid Login Test");
        System.out.println("Running testMedho1 on thread: " + Thread.currentThread().getId());
        ExtentManager.logstep("Navigating login page entering username and passwrod");
        loginPage.login("admin","admin123");
        ExtentManager.logstep("Verifying Admin tab is visible after login");
        assert homePage.isAdminTabVisible() : "Admin tab is not visible, login might have failed.";
        ExtentManager.logstep("Validation successful, Admin tab is visible");
        homePage.logout();
        ExtentManager.logstep("Logged out successfully");
        staticwait(3);
    }

    @Test

    public void invalidLoginTest() throws IOException {
        ExtentManager.satrtTest("Invalid Login Test");
        ExtentManager.logstep("Navigating login page entering invalid username and passwrod");
        loginPage.login("invalidUser","invalidPass");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Error message does not match expected.");
        ExtentManager.logstep("Validation successful, error message displayed as expected");
        ExtentManager.logstep("Logged out successfully");
    }
}
