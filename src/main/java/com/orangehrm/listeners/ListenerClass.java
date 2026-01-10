package com.orangehrm.listeners;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class ListenerClass implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().pass("Test Passed Successfully");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // 1. Try to get Driver
        WebDriver driver = null;
        try {
            driver = BaseClass.getDriver();
        } catch (Exception e) {
            // Driver might not be initialized if test failed in DataProvider
        }

        // 2. Print REAL Error to Console (Critical for debugging)
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("TEST FAILURE: " + result.getName());
        System.out.println("ERROR MESSAGE: " + result.getThrowable().getMessage());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        // 3. Log to Report (Only if Report exists)
        if (ExtentManager.getTest() != null) {
            try {
                if (driver != null) {
                    ExtentManager.logFailure(driver, "Test Failed: " + result.getThrowable().getMessage(), "FailureScreenshot");
                } else {
                    ExtentManager.getTest().fail("Test Failed (No Driver): " + result.getThrowable().getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().skip("Test Skipped");
        }
    }

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.cleanScreenshotsDirectory();
        ExtentManager.getReporter();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.endTest();
    }
}