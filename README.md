# 🍊 OrangeHRM E2E Automation Framework

A robust, modular, and data-driven test automation framework built to perform End-to-End (E2E) UI testing on the OrangeHRM web application. 

This project demonstrates "Senior-Level" automation practices, including full CRUD (Create, Read, Update, Delete) test cycles, dynamic web table handling, and strict explicit wait strategies.

## 🚀 Key Features

* **Hybrid Architecture:** Combines the **Page Object Model (POM)** with **PageFactory** for clean, maintainable, and readable code.
* **Data-Driven Testing (DDT):** Seamlessly integrates with Apache POI to feed test data dynamically via an Excel workbook (`TestData.xlsx`).
* **Dynamic Web Table Handling:** Includes complex logic to search, iterate, and interact with dynamic table elements (e.g., finding a specific user and clicking their unique delete icon).
* **Smart Wait Strategy:** **Zero `Thread.sleep()`**. Utilizes a custom `ActionDriver` class equipped with Explicit and Fluent waits to handle AJAX calls, loading spinners, and dynamic element rendering.
* **Test Suite Execution:** Orchestrates test flow sequentially using `testng.xml` to ensure proper data creation and cleanup.
* **Rich Reporting:** Generates beautiful HTML execution reports using **ExtentReports**, capturing step-by-step logs and execution status.

## 🛠️ Tech Stack

* **Language:** Java (JDK 11+)
* **Automation Tool:** Selenium WebDriver (v4.x)
* **Testing Framework:** TestNG
* **Build Tool:** Maven
* **Data Provider:** Apache POI (Excel)
* **Reporting:** ExtentReports

## 📂 Project Structure

```text
├── src/main/java/com/orangehrm
│   ├── actiondriver    # Reusable Selenium wrappers & wait mechanisms
│   ├── base            # BaseClass (WebDriver initialization, properties)
│   ├── pages           # Page Object classes (Locators & Page actions)
│   ├── utilities       # ExtentManager, ExcelUtil, LoggerManager
├── src/test/java/com/orangehrm/test
│   ├── AddEmployeeTest.java      # Test: Creates users from Excel data
│   ├── SearchEmployeeTest.java   # Test: Validates user exists in Web Table
│   ├── DeleteEmployeeTest.java   # Test: Cleans up created users
├── src/test/resources
│   ├── testdata
│   │   └── TestData.xlsx         # Test data source
│   ├── testng.xml                # Test suite configuration runner
├── pom.xml                       # Maven dependencies

⚙️ Prerequisites
To run this project locally, ensure you have the following installed:

Java Development Kit (JDK) 11 or higher.

Apache Maven.

An IDE like IntelliJ IDEA or Eclipse.

Google Chrome (or update the browser string in config.properties).

▶️ How to Run the Tests
Option 1: Using IDE (IntelliJ / Eclipse)
Clone this repository: git clone https://github.com/YourUsername/OrangeHRM-Automation.git

Open the project in your IDE and allow Maven to download all dependencies.

Navigate to src/test/resources/testng.xml.

Right-click testng.xml and select Run '...testng.xml'.

Option 2: Using Maven Command Line
Open your terminal at the project root and run:

Bash
mvn clean test
📝 Test Scenarios Covered (The CRUD Cycle)
This framework executes a chained sequence of tests to ensure data integrity and proper environment cleanup:

AddEmployeeTest: Logs in, navigates to the PIM module, and dynamically creates employees based on rows provided in the Excel sheet.

SearchEmployeeTest: Navigates to the Employee List, searches for the newly created employee, and validates their presence in the results table.

DeleteEmployeeTest: Locates the specified employee in the dynamic table, handles the delete confirmation popup, and verifies the record is completely removed from the system.

📊 Reporting
After a successful (or failed) test run, navigate to the test-output/ExtentReport/ directory. Open the generated .html file in any browser to view detailed execution logs.

Developed by Hafeez Ashraf
