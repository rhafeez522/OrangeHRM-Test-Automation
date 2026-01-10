package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.annotations.Test;

public class DummyClass2 extends BaseClass {
    @Test
    public void DummyTest_2() {

        //ExtentManager.satrtTest("Dummy Test 2 - Title Verification");

        String title = getDriver().getTitle();

        ExtentManager.logstep("Verifying the title");

        assert title.equals("OrangeHRM"): "Test Failed - Title does not match!";
        System.out.println("Test passed. Title is Matching:");
        ExtentManager.logstep("Validation Successful");

    }
}
