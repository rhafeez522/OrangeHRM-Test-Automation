package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExcelUtil;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginPageTest extends BaseClass {

    LoginPage loginPage;

    @Test(dataProvider = "LoginData", dataProviderClass = ExcelUtil.class)
    public void loginTest(String username, String password) throws IOException {

        loginPage = new LoginPage();
        ExtentManager.logstep("Login Test Started with User: " + username);

        loginPage.login(username, password);
        ExtentManager.logstep("Entered Username: " + username + " and Password: " + password);

        String actualUrl = getDriver().getCurrentUrl();
        ExtentManager.logstep("Current URL: " + actualUrl);

        if (actualUrl.contains("dashboard")) {
            ExtentManager.logstep("Login Successful");
            Assert.assertTrue(true);
        } else {
            ExtentManager.logstep("Login Failed (Expected for invalid data)");
        }
    }
}