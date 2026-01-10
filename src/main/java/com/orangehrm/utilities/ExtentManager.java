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

    public synchronized static ExtentTest startTest(String testName){
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
// log a step
    public static void logstep(String logMessage) {
        // Variable se quotes hata diye hain
        getTest().info(logMessage);
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

        // Date format
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        // FOLDER PATH (Safe Logic)
        // Pehle path banayein
        String directoryPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "screenshots";

        // Check karein folder hai ya nahi, agar nahi to banayein
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Yeh folder create kar dega
        }

        // Final File Path
        String destPath = directoryPath + File.separator + screenshotName + "_" + timeStamp + ".png";
        File finalPath = new File(destPath);

        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Base64 return karein
        return converToBase64(src);
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
    // --- NEW METHOD TO CLEAN SCREENSHOTS FOLDER ---
    public static void cleanScreenshotsDirectory() {
        try {
            String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "screenshots";
            File directory = new File(path);

            if (directory.exists()) {
                // Yeh folder ke andar ki tamam files delete kar dega
                FileUtils.cleanDirectory(directory);
                System.out.println("Screenshots folder cleaned successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to clean screenshots folder.");
        }
    }

    //Register webDriver for current Thread
    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(),driver);
    }

}
