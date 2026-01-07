package com.orangehrm.listeners;

import com.orangehrm.utilities.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    // Jab koi bhi @Test method shuru hoga
    public void onTestStart(ITestResult result) {
        // Test ka naam utha kar report main start karega
        ExtentManager.satrtTest(result.getMethod().getMethodName());
    }

    // Jab Test Pass ho jaye
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Test Passed Successfully");
    }

    // Jab Test Fail ho jaye
    public void onTestFailure(ITestResult result) {
        // Fail log karein
        ExtentManager.getTest().fail("Test Failed: " + result.getThrowable());

        // Screenshot logic yahan call kar sakte hain agar driver ka access ho
        // Filhal sirf text log kar rahe hain taake complexity na barhay
    }

    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().skip("Test Skipped");
    }

    // Jab pori Suite start ho
    public void onStart(ITestContext context) {
        // Report setup initialize ho jayega
        ExtentManager.getReporter();
    }

    // Jab sab kuch khatam ho jaye (Sab se Zaroori step)
    public void onFinish(ITestContext context) {
        // Yeh report ko save karega (flush)
        ExtentManager.endTest();
    }
}