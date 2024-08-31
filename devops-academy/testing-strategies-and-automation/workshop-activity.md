---
icon: flask
cover: >-
  https://images.unsplash.com/photo-1607969892435-1063b49bc963?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw2fHx3b3Jrc2hvcHxlbnwwfHx8fDE3MjMwNDE1ODJ8MA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Workshop Activity

### Introduction

This workshop activity provides detailed steps for students to implement test automation, including unit testing, integration testing, and end-to-end testing. Follow the instructions carefully to ensure accurate implementation.

***

### **Unit Testing**

Unit testing involves testing individual units or components of a software application to ensure they function as intended. This section provides detailed steps to create and execute unit tests for the `AccountService` class, including writing and committing tests for the `saveAccount` and `getAllAccounts` methods.

**Change the State of the User Story**

1. **Change the State of the User Story:**
   * Change the state of the User Story “Write Unit Tests for Account Service” to “In Progress”.

**Create a Feature Branch**

2.  **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-unit-tests
    ```

**Write Unit Tests**

3. **Write Unit Tests:**
   * Use JUnit for writing unit tests for backend code. Here are the specific tests for the `AccountService` methods: `saveAccount` and `getAllAccounts`.

**Unit Test for saveAccount Method**

Create a test class `AccountServiceTest.java` in the `src/test/java/com/example/BankManagementSystem/service` package:

```java
package com.example.BankManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.BankManagementSystem.model.Account;
import com.example.BankManagementSystem.model.Customer;
import com.example.BankManagementSystem.repository.AccountRepository;
import com.example.BankManagementSystem.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test class for AccountService.
 */
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService; // Injecting mocks into accountService

    @Mock
    private AccountRepository accountRepository; // Mocking the AccountRepository

    @Mock
    private CustomerRepository customerRepository; // Mocking the CustomerRepository

    /**
     * Sets up the test environment before each test.
     * Initializes the mocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the saveAccount method in AccountService.
     * It verifies that the account is saved correctly.
     */
    @Test
    void saveAccount_shouldReturnSavedAccount() {
        // Creating a test customer
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        // Creating a test account
        Account account = new Account();
        account.setAccountNumber("123456");
        account.setAccountType("Savings");
        account.setBalance(1000);
        account.setOpeningDate(LocalDate.now());
        account.setCustomer(customer);

        // Mocking the behavior of customerRepository and accountRepository
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(accountRepository.save(account)).thenReturn(account);

        // Calling the saveAccount method
        Account savedAccount = accountService.saveAccount(account);

        // Asserting that the saved account is not null
        assertNotNull(savedAccount);
        // Asserting that the account number of the saved account is "123456"
        assertEquals("123456", savedAccount.getAccountNumber());
        // Asserting that the account type of the saved account is "Savings"
        assertEquals("Savings", savedAccount.getAccountType());
        // Asserting that the balance of the saved account is 1000
        assertEquals(1000, savedAccount.getBalance());
        // Asserting that the opening date of the saved account is today
        assertEquals(LocalDate.now(), savedAccount.getOpeningDate());
        // Asserting that the customer name of the saved account is "Test Customer"
        assertEquals("Test Customer", savedAccount.getCustomer().getName());

        // Verifying that the findById method of customerRepository was called exactly once with the correct customer ID
        verify(customerRepository, times(1)).findById(customer.getId());
        // Verifying that the save method of accountRepository was called exactly once with the correct account object
        verify(accountRepository, times(1)).save(account);
    }
}
```

**Explanation of Code:**

* **MockitoAnnotations.openMocks(this)**: Initializes the mocks for this test class.
* **@InjectMocks**: Injects the mocks into the instance of `AccountService` to test it.
* **@Mock**: Creates mock instances of `AccountRepository` and `CustomerRepository`.
* **saveAccount\_shouldReturnSavedAccount()**: Tests the `saveAccount` method to ensure it correctly saves an account and returns it.

**Commit the Unit Test for saveAccount Method**

1.  **Add the Test File to Git:**

    ```bash
    git add .
    ```
2.  **Commit the saveAccount Unit Test:**

    ```bash
    git commit -m "Add unit test for AccountService saveAccount method. Work Item #201"
    ```

**Unit Test for getAllAccounts Method**

Add the following test to `AccountServiceTest.java`:

```java
/**
 * Test case for the getAllAccounts method in AccountService.
 * It verifies that the method returns a list of accounts correctly.
 */
@Test
void getAllAccounts_shouldReturnListOfAccounts() {
    // Creating the first test customer
    Customer customer1 = new Customer();
    customer1.setId(1L);
    customer1.setName("Customer One");

    // Creating the second test customer
    Customer customer2 = new Customer();
    customer2.setId(2L);
    customer2.setName("Customer Two");

    // Creating the first test account
    Account account1 = new Account();
    account1.setAccountNumber("123456");
    account1.setAccountType("Savings");
    account1.setBalance(1000);
    account1.setOpeningDate(LocalDate.of(2023, 1, 1));
    account1.setCustomer(customer1);

    // Creating the second test account
    Account account2 = new Account();
    account2.setAccountNumber("654321");
    account2.setAccountType("Checking");
    account2.setBalance(2000);
    account2.setOpeningDate(LocalDate.of(2023, 1, 2));
    account2.setCustomer(customer2);

    // Creating a list of test accounts
    List<Account> accountList = Arrays.asList(account1, account2);

    // Mocking the behavior of accountRepository to return the list of test accounts
    when(accountRepository.findAll()).thenReturn(accountList);

    // Calling the getAllAccounts method
    List<Account> result = accountService.getAllAccounts();

    // Asserting that the result is not null
    assertNotNull(result);
    // Asserting that the size of the result list is 2
    assertEquals(2, result.size());

    // Verifying the details of the first account in the result list
    Account resultAccount1 = result.get(0);
    assertEquals("123456", resultAccount1.getAccountNumber());
    assertEquals("Savings", resultAccount1.getAccountType());
    assertEquals(1000, resultAccount1.getBalance());
    assertEquals(LocalDate.of(2023, 1, 1), resultAccount1.getOpeningDate());
    assertEquals("Customer One", resultAccount1.getCustomer().getName());

    // Verifying the details of the second account in the result list
    Account resultAccount2 = result.get(1);
    assertEquals("654321", resultAccount2.getAccountNumber());
    assertEquals("Checking", resultAccount2.getAccountType());
    assertEquals(2000, resultAccount2.getBalance());
    assertEquals(LocalDate.of(2023, 1, 2), resultAccount2.getOpeningDate());
    assertEquals("Customer Two", resultAccount2.getCustomer().getName());

    // Verifying that the findAll method of accountRepository was called exactly once
    verify(accountRepository, times(1)).findAll();
}
```

**Explanation of Code:**

* **getAllAccounts\_shouldReturnListOfAccounts()**: Tests the `getAllAccounts` method to ensure it returns a list of accounts.
* **when(accountRepository.findAll()).thenReturn(accountList)**: Mocks the behavior of `accountRepository.findAll()` to return a predefined list of accounts.
* **assertNotNull(result)**: Asserts that the result is not null.
* **assertEquals**: Verifies that the expected and actual values are equal.

**Commit the Unit Test for getAllAccounts Method**

1.  **Add the Changes to Git:**

    ```bash
    git add .
    ```
2.  **Commit the getAllAccounts Unit Test:**

    ```bash
    git commit -m "Add unit test for AccountService getAllAccounts method. Work Item #202"
    ```

**Push Changes to Azure DevOps**

1.  **Push the Changes to the Feature Branch:**

    ```bash
    git push origin feature/add-unit-tests
    ```

**Syncing Branches**

1. **Create a Pull Request to Merge `feature/add-unit-tests` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-unit-tests`
     * Target branch: `dev`
     * Title: "Add unit tests for AccountService methods"
     * Description: "This PR adds unit tests for the AccountService methods saveAccount and getAllAccounts."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `feature/add-unit-tests` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING MERGE.
2.  **Delete Locally the `feature/add-unit-tests` Branch:**

    ```bash
    git checkout dev
    git branch -D feature/add-unit-tests
    ```
3. **Merge to `dev`:**
   * Once the pull request is approved, merge it into the `dev` branch via Azure DevOps.
   * Update the status of the work items linked to this PR to "Done".
4.  **Pull `dev` Locally:**

    ```bash
    git checkout dev
    git pull origin dev
    ```
5. **Change the State of the Feature and User Story:**
   * Change the state of the Feature “Unit Testing Implementation” to “Done”.
   * Change the state of the User Story “Write Unit Tests for Account Service” to “Done”.
6. **Create a Pull Request to Merge `dev` into `staging`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `dev`
     * Target branch: `staging`
     * Title: "Merge dev into staging"
     * Description: "Merge the latest changes from dev into staging for testing."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `dev` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING REBASE AND FF.
7.  **Pull `staging` Locally:**

    ```bash
    git checkout staging
    git pull origin staging
    ```
8. **Create a Pull Request to Merge `staging` into `master`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `staging`
     * Target branch: `master`
     * Title: "Merge staging into master"
     * Description: "Merge the tested changes from staging into master for production deployment."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `staging` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING REBASE AND FF.
9.  **Pull `master` Locally:**

    ```bash
    git checkout master
    git pull origin master
    ```

***

### **Integration Testing**

Integration testing verifies that different modules or services used by your application work well together. This section details the steps to set up and execute integration tests for the `AccountController` class using RestAssured, focusing on the `addAccount` and `getAccounts` endpoints.

**Change the State of the User Story**

1. **Change the State of the User Story:**
   * Change the state of the User Story “Write Integration Tests for Account API” to “In Progress”.

**Create a Feature Branch**

2.  **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-integration-tests
    ```

**Add Dependencies**

3.  **Add Dependencies:**

    Ensure that the following dependencies are added to your `pom.xml` file for RestAssured:

    ```xml
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>
    ```

**Configure the Test Database**

4.  **Configure the Test Database:**

    In your `src/test/resources` directory, create an `application-test.properties` file to configure the In Memory test database for testing:

    ```properties
    spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    spring.datasource.driver-class-name=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password

    spring.jpa.hibernate.ddl-auto=create-drop
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

    spring.h2.console.enabled=true
    ```

**Write Integration Tests**

5.  **Write Integration Tests:**

    Use RestAssured for writing integration tests for the API endpoints. Here are the specific tests for the `AccountController` methods: `addAccount` and `getAccounts`.

**Integration Test for addAccount Endpoint**

Create a test class `AccountControllerIntegrationTest.java` in the `src/test/java/com/example/BankManagementSystem/controller` package:

```java
package com.example.BankManagementSystem.controller;

import com.example.BankManagementSystem.model.Account;
import com.example.BankManagementSystem.model.Customer;
import com.example.BankManagementSystem.repository.AccountRepository;
import com.example.BankManagementSystem.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test for the AccountController to verify API interactions using RestAssured.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    /**
     * Sets up the test environment before each test.
     * Initializes RestAssured port, clears the repositories, and sets up a test customer.
     */
    @BeforeEach
    void setUp() {
        RestAssured.port = port; // Set the port for RestAssured
        accountRepository.deleteAll(); // Clear all accounts
        customerRepository.deleteAll(); // Clear all customers
        customer = new Customer();
        customer.setName("Test Customer");
        customer = customerRepository.save(customer); // Save a test customer
    }

    /**
     * Test case for adding an account.
     * It verifies that an account is successfully added and returns a CREATED status.
     */
    @Test
    void addAccount_shouldReturnCreatedStatus() {
        // JSON representation of the account to be added
        String accountJson = "{\"accountNumber\":\"123456\",\"accountType\":\"Savings\",\"balance\":1000,\"openingDate\":\"2023-01-01\",\"customer\":{\"id\":" + customer.getId() + "}}";

        // Send POST request and validate response
        given()
                .contentType(ContentType.JSON) // Set the content type to JSON
                .body(accountJson) // Set the request body
                .when()
                .post("/api/accounts") // Send a POST request to /api/accounts
                .then()
                .statusCode(HttpStatus.CREATED.value()) // Expect a 201 Created status
                .body("accountNumber", equalTo("123456")) // Verify the account number
                .body("accountType", equalTo("Savings")) // Verify the account type
                .body("balance", equalTo(1000.0f)) // Verify the balance (use float for balance)
                .body("openingDate", equalTo("2023-01-01")) // Verify the opening date
                .body("customer.id", equalTo(customer.getId().intValue())); // Verify the customer ID
    }
}
```

**Explanation of Code:**

* **RestAssured.port = port**: Configures RestAssured to use the port of the Spring Boot application.
* **@ActiveProfiles("test")**: Activates the "test" profile for the Spring Boot application.
* **addAccount\_shouldReturnCreatedStatus()**: Tests the `addAccount` endpoint to ensure it correctly adds an account and returns the created status.

**Commit the Integration Test for addAccount Endpoint**

1.  **Add the Test File to Git:**

    ```bash
    git add .
    ```
2.  **Commit the addAccount Integration Test:**

    ```bash
    git commit -m "Add integration test for AccountController addAccount endpoint. Work Item #303"
    ```

**Integration Test for getAccounts Endpoint**

Add the following test to `AccountControllerIntegrationTest.java`:

```java
/**
 * Test case for retrieving a list of accounts.
 * It verifies that the API returns the correct list of accounts.
 */
@Test
void getAccounts_shouldReturnListOfAccounts() {
    // Creating the first test account
    Account account1 = new Account();
    account1.setAccountNumber("123456");
    account1.setAccountType("Savings");
    account1.setBalance(1000);
    account1.setOpeningDate(LocalDate.of(2023, 1, 1));
    account1.setCustomer(customer);
    accountRepository.save(account1); // Save the first account to the repository

    // Creating the second test account
    Account account2 = new Account();
    account2.setAccountNumber("654321");
    account2.setAccountType("Checking");
    account2.setBalance(2000);
    account2.setOpeningDate(LocalDate.of(2023, 1, 2));
    account2.setCustomer(customer);
    accountRepository.save(account2); // Save the second account to the repository

    // Send GET request to retrieve the list of accounts and validate the response
    given()
            .contentType(ContentType.JSON) // Set the content type to JSON
            .when()
            .get("/api/accounts") // Send a GET request to /api/accounts
            .then()
            .statusCode(HttpStatus.OK.value()) // Expect a 200 OK status
            .body("size()", equalTo(2)) // Verify the size of the returned list
            .body("[0].accountNumber", equalTo("123456")) // Verify the account number of the first account
            .body("[0].accountType", equalTo("Savings")) // Verify the account type of the first account
            .body("[0].balance", equalTo(1000.0f)) // Verify the balance of the first account (use float for balance)
            .body("[0].openingDate", equalTo("2023-01-01")) // Verify the opening date of the first account
            .body("[0].customer.name", equalTo("Test Customer")) // Verify the customer name of the first account
            .body("[1].accountNumber", equalTo("654321")) // Verify the account number of the second account
            .body("[1].accountType", equalTo("Checking")) // Verify the account type of the second account
            .body("[1].balance", equalTo(2000.0f)) // Verify the balance of the second account (use float for balance)
            .body("[1].openingDate", equalTo("2023-01-02")) // Verify the opening date of the second account
            .body("[1].customer.name", equalTo("Test Customer")); // Verify the customer name of the second account
}
```

**Explanation of Code:**

* **getAccounts\_shouldReturnListOfAccounts()**: Tests the `getAccounts` endpoint to ensure it returns a list of accounts.
* **accountRepository.save(account1)**: Saves `account1` to the test database.
* **given().contentType(ContentType.JSON).when().get("/api/accounts")**: Sends a GET request to the `getAccounts` endpoint and verifies the response.

**Commit the Integration Test for getAccounts Endpoint**

1.  **Add the Changes to Git:**

    ```bash
    git add .
    ```
2.  **Commit the getAccounts Integration Test:**

    ```bash
    git commit -m "Add integration test for AccountController getAccounts endpoint. Work Item #304"
    ```

**Push Changes to Azure DevOps**

1.  **Push the Changes to the Feature Branch:**

    ```bash
    git push origin feature/add-integration-tests
    ```

**Syncing Branches**

1. **Create a Pull Request to Merge `feature/add-integration-tests` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-integration-tests`
     * Target branch: `dev`
     * Title: "Add integration tests for AccountController endpoints"
     * Description: "This PR adds integration tests for the AccountController endpoints addAccount and getAccounts."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `feature/add-integration-tests` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING MERGE.
2.  **Delete Locally the `feature/add-integration-tests` Branch:**

    ```bash
    git checkout dev
    git branch -D feature/add-integration-tests
    ```
3.  **Pull `dev` Locally:**

    ```bash
    git checkout dev
    git pull origin dev
    ```
4. **Change the State of the Feature and User Story:**
   * Change the state of the Feature “Integration Testing Implementation” to “Done”.
   * Change the state of the User Story “Write Integration Tests for Account API” to “Done”.
5. **Create a Pull Request to Merge `dev` into `staging`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `dev`
     * Target branch: `staging`
     * Title: "Merge dev into staging"
     * Description: "Merge the latest changes from dev into staging for testing."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `dev` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING REBASE AND FF.
6.  **Pull `staging` Locally:**

    ```bash
    git checkout staging
    git pull origin staging
    ```
7. **Create a Pull Request to Merge `staging` into `master`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `staging`
     * Target branch: `master`
     * Title: "Merge staging into master"
     * Description: "Merge the tested changes from staging into master for production deployment."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `staging` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING REBASE AND FF.
8.  **Pull `master` Locally:**

    ```bash
    git checkout master
    git pull origin master
    ```

***

### **End-to-End Testing**

End-to-end (E2E) testing ensures that the entire application flow works as expected from start to finish. This section provides a comprehensive guide to implementing E2E tests for the Account Management functionality using Selenium WebDriver, including tests for adding and deleting accounts.

**Change the State of the User Story**

1. **Change the State of the User Story:**
   * Change the state of the User Story “Write End-to-End Tests for Account Management” to “In Progress”.

**Create a Feature Branch**

2.  **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-end-to-end-tests
    ```

**Add Dependencies**

3.  **Add Dependencies:**

    Ensure that the following dependencies are added to your `pom.xml` file for Selenium WebDriver:

    ```xml
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.23.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    ```

**Update the Test Class to Use WebDriverManager**

Update the `AccountManagementE2ETest.java` class to use WebDriverManager:

```java
package com.example.BankManagementSystem.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end (E2E) test for account management in a Spring Boot application using Selenium.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AccountManagementE2ETest {

    private WebDriver driver;
    private String baseUrl;

    /**
     * Sets up the WebDriverManager before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    /**
     * Sets up the WebDriver and test data before each test.
     */
    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run browser in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        baseUrl = System.getProperty("base.url", "http://localhost:8080");

        // Login or register if necessary
        driver.get(baseUrl + "/login");
        loginOrRegister();

        // Ensure a clean state by clearing any existing test data
        clearTestData();

        // Add a customer to use in the tests
        addTestCustomer();
    }

    /**
     * Cleans up the WebDriver and test data after each test.
     */
    @AfterEach
    void tearDown() {
        clearTestData();
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Logs in or registers a new user if login fails.
     */
    private void loginOrRegister() {
        try {
            // Attempt login
            WebElement usernameField = driver.findElement(By.name("username"));
            usernameField.sendKeys("testuser");
            driver.findElement(By.name("password")).sendKeys("password");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            // Check if login was successful
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Accounts")));
        } catch (Exception e) {
            // Register a new user if login fails
            driver.get(baseUrl + "/register");
            driver.findElement(By.id("username")).sendKeys("testuser");
            driver.findElement(By.id("password")).sendKeys("password");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            loginOrRegister(); // Attempt login again after registration
        }
    }

    /**
     * Clears all test data from the system.
     */
    private void clearTestData() {
        // Deleting all accounts
        driver.get(baseUrl + "/accounts");
        List<WebElement> accounts = driver.findElements(By.cssSelector("#accountList li"));

        for (WebElement account : accounts) {
            try {
                WebElement deleteButton = account.findElement(By.cssSelector("button"));
                deleteButton.click();
            } catch (Exception e) {
                System.err.println("Error deleting account: " + e.getMessage());
            }
        }

        // Deleting all customers
        driver.get(baseUrl + "/customers");
        List<WebElement> customers = driver.findElements(By.cssSelector("#customerList li"));

        for (WebElement customer : customers) {
            try {
                WebElement deleteButton = customer.findElement(By.cssSelector("button"));
                deleteButton.click();
            } catch (Exception e) {
                System.err.println("Error deleting customer: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a test customer to the system.
     */
    private void addTestCustomer() {
        driver.get(baseUrl + "/customers");
        driver.findElement(By.id("name")).sendKeys("Test Customer");
        driver.findElement(By.cssSelector("form#addCustomerForm button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Accounts")));
        driver.findElement(By.linkText("Accounts")).click(); // Navigate back to Accounts page
    }
}
```

**Write End-to-End Tests**

1. **Write End-to-End Tests:**
   * Use Selenium WebDriver for writing end-to-end tests for the application. Here are the specific tests for the `Account Management` functionality: `Add Account` and `Delete Account`.

**End-to-End Test for Add Account Functionality**

Add the following test to `AccountManagementE2ETest.java`:

```java
/**
 * Test case to verify that a new account is displayed in the list after being added.
 */
@Test
void addAccount_shouldDisplayNewAccountInList() {
    // Enter the account number
    driver.findElement(By.id("accountNumber")).sendKeys("123456");
    // Enter the account type
    driver.findElement(By.id("accountType")).sendKeys("Savings");
    // Enter the balance
    driver.findElement(By.id("balance")).sendKeys("1000");
    // Enter the opening date
    driver.findElement(By.id("openingDate")).sendKeys("01/01/2023"); // Set opening date
    // Enter the customer ID (assuming a customer with ID 1 exists)
    driver.findElement(By.id("customerId")).sendKeys("1");
    // Click the submit button to add the account
    driver.findElement(By.cssSelector("form#addAccountForm button[type='submit']")).click();

    // Add a small delay to ensure the page is updated
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accountList")));

    // Find the account list element
    WebElement accountList = driver.findElement(By.id("accountList"));
    // Get the text content of the account list
    String pageSource = accountList.getText();

    // Verify that the account list contains the new account details
    assertTrue(pageSource.contains("123456")); // Check for account number
    assertTrue(pageSource.contains("SAVINGS")); // Check for account type (assuming the type is displayed in uppercase)
    assertTrue(pageSource.contains("1000")); // Check for balance
    assertTrue(pageSource.contains("2023-01-01")); // Check for opening date
}
```

**Commit the End-to-End Test for Add Account Functionality**

1.  **Add the Test File to Git:**

    ```bash
    git add .
    ```
2.  **Commit the addAccount End-to-End Test:**

    ```bash
    git commit -m "Add end-to-end test for Account Management addAccount functionality. Work Item #305"
    ```

**End-to-End Test for Delete Account Functionality**

Add the following test to `AccountManagementE2ETest.java`:

```java
/**
 * Test case to verify that an account is removed from the list after being deleted.
 */
@Test
void deleteAccount_shouldRemoveAccountFromList() {
    // First, add an account to delete
    driver.findElement(By.id("accountNumber")).sendKeys("123456");
    // Enter the account type
    driver.findElement(By.id("accountType")).sendKeys("Savings");
    // Enter the balance
    driver.findElement(By.id("balance")).sendKeys("1000");
    // Enter the opening date
    driver.findElement(By.id("openingDate")).sendKeys("01/01/2023"); // Set opening date
    // Enter the customer ID (assuming a customer with ID 1 exists)
    driver.findElement(By.id("customerId")).sendKeys("1");
    // Click the submit button to add the account
    driver.findElement(By.cssSelector("form#addAccountForm button[type='submit']")).click();

    // Then, delete the account
    WebElement accountList = driver.findElement(By.id("accountList"));
    // Find the delete button for the added account using XPath
    WebElement deleteButton = accountList.findElement(By.xpath("//li[contains(text(), '123456')]//button[contains(text(), 'Delete')]"));
    // Click the delete button to remove the account
    deleteButton.click();

    // Verify the account is removed by waiting until the delete button is no longer visible
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.invisibilityOf(deleteButton));
    // Get the text content of the account list
    String pageSource = accountList.getText();
    // Assert that the account list no longer contains the account number
    assertTrue(!pageSource.contains("123456"));
}
```

**Commit the End-to-End Test for Delete Account Functionality**

1.  **Add the Changes to Git:**

    ```bash
    git add .
    ```
2.  **Commit the deleteAccount End-to-End Test:**

    ```bash
    git commit -m "Add end-to-end test for Account Management deleteAccount functionality. Work Item #306"
    ```

**Push Changes to Azure DevOps**

1.  **Push the Changes to the Feature Branch:**

    ```bash
    git push origin feature/add-end-to-end-tests
    ```

**Syncing Branches**

1. **Create a Pull Request to Merge `feature/add-end-to-end-tests` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-end-to-end-tests`
     * Target branch: `dev`
     * Title: "Add end-to-end tests for Account Management functionality"
     * Description: "This PR adds end-to-end tests for the Account Management functionality, including addAccount and deleteAccount."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `feature/add-end-to-end-tests` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING MERGE.
2.  **Delete Locally the `feature/add-end-to-end-tests` Branch:**

    ```bash
    git checkout dev
    git branch -D feature/add-end-to-end-tests
    ```
3.  **Pull `dev` Locally:**

    ```bash
    git checkout dev
    git pull origin dev
    ```
4. **Change the State of the Feature and User Story:**
   * Change the state of the Feature “End-to-End Testing Implementation” to “Done”.
   * Change the state of the User Story “Write End-to-End Tests for Account Management” to “Done”.
5. **Create a Pull Request to Merge `dev` into `staging`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `dev`
     * Target branch: `staging`
     * Title: "Merge dev into staging"
     * Description: "Merge the latest changes from dev into staging for testing."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `dev` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING REBASE AND FF.
6.  **Pull `staging` Locally:**

    ```bash
    git checkout staging
    git pull origin staging
    ```
7. **Create a Pull Request to Merge `staging` into `master`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `staging`
     * Target branch: `master`
     * Title: "Merge staging into master"
     * Description: "Merge the tested changes from staging into master for production deployment."
   * **Assign Reviewers:**
     * Add team members as reviewers.
   * **Complete Code Review:**
     * Reviewers will review the changes, add comments, and request changes if necessary.
     * Address any feedback by making changes in the `staging` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request USING REBASE AND FF.
8.  **Pull `master` Locally:**

    ```bash
    git checkout master
    git pull origin master
    ```

***

### Conclusion

Congratulations! By completing this workshop activity, you have successfully implemented comprehensive test automation for the banking management system. You have written unit tests to ensure individual methods in the `AccountService` class work correctly, integration tests to verify that the `AccountController` endpoints interact seamlessly with services and repositories, and end-to-end tests to confirm that the entire application workflow functions as expected.

This hands-on experience is crucial as you move towards integrating these automated tests into CI/CD pipelines. Continuous testing, early bug detection, and faster release cycles will become a reality, significantly improving code quality and user experience.

Remember to keep your tests up-to-date and continuously refine your testing strategies. Effective test automation is key to successful software development and deployment, and you are now well-equipped to implement it in real-world scenarios. Great job!
