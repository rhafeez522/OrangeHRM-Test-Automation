package com.orangehrm.utilities;

import java.io.File;

public class TestExcel {

    public static void main(String[] args) {

        // 1. Construct the path
        String projectPath = System.getProperty("user.dir");
        String relativePath = "src/test/resources/testdata/TestData.xlsx";

        // Fix path for Windows
        if (File.separator.equals("\\")) {
            relativePath = relativePath.replace("/", "\\");
        }

        String fullPath = projectPath + File.separator + relativePath;

        System.out.println("---------------------------------------");
        System.out.println("DEBUGGING EXCEL PATH...");
        System.out.println("Checking path: " + fullPath);

        // 2. Check if file exists
        File file = new File(fullPath);

        if (file.exists()) {
            System.out.println("PASS: File FOUND on computer!");
            System.out.println("File Size: " + file.length() + " bytes");

            // 3. Try to read it using ExcelUtil
            try {
                ExcelUtil excel = new ExcelUtil(fullPath, "Sheet1");
                int rows = excel.getRowCount();
                System.out.println("PASS: Excel opened successfully.");
                System.out.println("Total Rows Found: " + rows);

                if (rows > 1) {
                    System.out.println("Data Check (Row 1, Col 0): " + excel.getCellData(1, 0));
                    System.out.println("RESULT: EVERYTHING IS WORKING FINE!");
                } else {
                    System.out.println("FAIL: File found but 0 rows data. Please save the file.");
                }

            } catch (Exception e) {
                System.out.println("FAIL: File exists but code crashed while reading.");
                e.printStackTrace();
            }

        } else {
            System.out.println("FAIL: File does NOT exist at this path.");
            System.out.println("Please check if 'testdata' folder is inside 'src/test/resources'.");
        }
        System.out.println("---------------------------------------");
    }
}