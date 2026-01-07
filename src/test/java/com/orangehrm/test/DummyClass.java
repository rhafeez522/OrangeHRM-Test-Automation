package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {
    @Test
    public void dummyTest() {
        ExtentManager.satrtTest("Dummy Test Started");

        String title = getDriver().getTitle();

        ExtentManager.satrtTest("Verifying Title of the Page");
        assert title.equals("OrangeHRM"): "Test Failed - Title does not match!";
        System.out.println("Test passed. Title is Matching:");
        throw new SkipException("Skipping this test deliberately");

    }
}
