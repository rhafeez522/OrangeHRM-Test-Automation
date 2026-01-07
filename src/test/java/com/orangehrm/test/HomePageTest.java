package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.HomePage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class HomePageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }
    @Test
    public void verifyOrangeHRMLogo() throws IOException {
        ExtentManager.satrtTest("Home Page verify logo Test");
        ExtentManager.logstep("Navigating to Login Page");
        loginPage.login("admin","admin123");
        ExtentManager.logstep("verifying OrangeHRM logo on Home Page");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"logo is not displayed");
        ExtentManager.logstep("Validation Successful");
        ExtentManager.logstep("Logout successful");
    }


}
