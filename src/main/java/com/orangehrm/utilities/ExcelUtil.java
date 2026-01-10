package com.orangehrm.utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    /**
     * Constructor to initialize Excel workbook and sheet.
     */
    public ExcelUtil(String excelPath, String sheetName) {
        try {
            File file = new File(excelPath);
            if (!file.exists()) {
                throw new RuntimeException("CRITICAL ERROR: File not found at path: " + excelPath);
            }

            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("CRITICAL ERROR: Sheet '" + sheetName + "' not found. Check spelling.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FAILED to load Excel file: " + e.getMessage());
        }
    }

    public int getRowCount() {
        if (sheet == null) return 0;
        return sheet.getPhysicalNumberOfRows();
    }

    public int getColCount() {
        if (sheet == null) return 0;
        return sheet.getRow(0).getPhysicalNumberOfCells();
    }

    public String getCellData(int rowNum, int colNum) {
        DataFormatter formatter = new DataFormatter();
        try {
            return formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * DataProvider linked to LoginPageTest.
     * MUST BE STATIC because it is in a separate utility class.
     */
    @DataProvider(name = "LoginData")
    public static Object[][] getLoginData() { // <--- Added 'static' here
        System.out.println(">>> DataProvider 'getLoginData' has been called by TestNG <<<");

        // --- LOGIC FROM TestExcel.java ---
        String projectPath = System.getProperty("user.dir");
        String relativePath = "src/test/resources/testdata/TestData.xlsx";

        // Fix path separators for Windows
        if (File.separator.equals("\\")) {
            relativePath = relativePath.replace("/", "\\");
        }

        String fullPath = projectPath + File.separator + relativePath;
        System.out.println("ExcelUtil is loading file from: " + fullPath);

        // Initialize Excel (This creates the instance internally, so the method itself can be static)
        ExcelUtil config = new ExcelUtil(fullPath, "Sheet1");

        int rows = config.getRowCount();
        int cols = config.getColCount();

        System.out.println("Total Rows Found in DataProvider: " + rows);

        if (rows < 2) {
            throw new RuntimeException("ERROR: File found but 0 rows data found. Please CLOSE and SAVE the Excel file.");
        }

        Object[][] data = new Object[rows - 1][cols];

        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i - 1][j] = config.getCellData(i, j);
            }
        }
        return data;
    }
    /**
     * DataProvider for Add Employee Test.
     * Reads from the sheet named "AddEmployee"
     */
    @DataProvider(name = "AddEmployeeData")
    public static Object[][] getAddEmployeeData() {
        System.out.println(">>> DataProvider 'getAddEmployeeData' called <<<");

        String projectPath = System.getProperty("user.dir");
        String relativePath = "src/test/resources/testdata/TestData.xlsx";

        if (File.separator.equals("\\")) {
            relativePath = relativePath.replace("/", "\\");
        }

        String fullPath = projectPath + File.separator + relativePath;

        // --- CHANGE HERE: Read from "AddEmployee" sheet ---
        ExcelUtil config = new ExcelUtil(fullPath, "AddEmployee");

        int rows = config.getRowCount();
        int cols = config.getColCount();

        if (rows < 2) {
            throw new RuntimeException("ERROR: 'AddEmployee' sheet is empty or missing. Add data!");
        }

        Object[][] data = new Object[rows - 1][cols];

        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i - 1][j] = config.getCellData(i, j);
            }
        }
        return data;
    }
}