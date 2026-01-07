package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap= new HashMap<>();
    //initialize ExtentReports instance
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            // Path fix (slash add kiya hai start main)
            String reportPath = System.getProperty("user.dir") + File.separator + "src/test/resources/ExtentReports/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Report"); // Spelling fix
            spark.config().setDocumentTitle("OrangeHRM Test Results");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();

            // --- YEH LINE MISSING THI ---
            extent.attachReporter(spark);
            // ----------------------------

            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java version", System.getProperty("java.version"));
            extent.setSystemInfo("Tester Name", System.getProperty("user.name"));
        }
        return extent;
    }

    //start the test

    public synchronized static ExtentTest satrtTest(String testName){
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    //end a test
    public synchronized static void endTest(){
        getReporter().flush();
    }

    //get current thread test
    public synchronized static ExtentTest getTest(){
        return test.get();
    }

    //method to get the name of the current test
    public static String getTestName(){
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            return currentTest.getModel().getName();
        }
        else {
            return "No Test Name Found";
        }
    }

    // log a step
    public static void logstep(String logMessage)
    {
        getTest().info("logMessage");
    }

    //log step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotmessage) throws IOException {
        getTest().pass(logMessage);
        //screenshot message
        attachScreenshot(driver, screenShotmessage);

    }

    //logFailure
    public static void logFailure (WebDriver driver, String logMessage, String screenShotmessage) throws IOException {

        getTest().fail(logMessage);
        //screenshot message
        attachScreenshot(driver, screenShotmessage);

    }
    //log a skip
    public static void logSkip(String logMessage){
        getTest().skip(logMessage);
    }
    //Take screenshot with date and time
    public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //format date and time for file name
        String timeStapmp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        //Saving the screenshot to a file
        String destPath = System.getProperty("user.dir") + "src/test/resources/screenshots"+screenshotName +"_"+timeStapmp+".png";
        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert screensht to base64 for embedding in extent report
        String base64Format = converToBase64(src);
        return base64Format;
    }
    //convert screenshot to base64
    public static String converToBase64(File screenShotFile) throws IOException {
        String base64Format = "";
        //Read the file content into a byte array
        try {
            byte[] filecontent =  FileUtils.readFileToByteArray(screenShotFile);
            base64Format = Base64.getEncoder().encodeToString(filecontent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Format;

    }
    //Attach scrreenshot to report
    public synchronized static void attachScreenshot(WebDriver driver, String message) throws IOException {
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestName());
            getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (IOException e) {
            getTest().fail("Failed to attach screenshot: " + message);
            e.printStackTrace();

        }

    }

    //Register webDriver for current Thread
    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(),driver);
    }

}
