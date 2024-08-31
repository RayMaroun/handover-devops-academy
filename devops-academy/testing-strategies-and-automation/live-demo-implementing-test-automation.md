---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1694903110330-cc64b7e1d21d?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw1fHxhdXRvbWF0aW9ufGVufDB8fHx8MTcyMzA0MTYwNHww&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Live Demo - Implementing Test Automation

### Introduction

This page provides detailed steps to implement test automation, including unit testing, integration testing, and end-to-end testing, in a live demo setting. Follow the instructions carefully to ensure accurate implementation.

***

### **Unit Testing**

Unit testing is the process of testing individual units or components of a software application to ensure they function as intended. This section provides detailed steps to create and execute unit tests for the `BookService` class, including writing and committing tests for the `saveBook` and `getAllBooks` methods.

1. **Change the State of the User Story:**
   * Change the state of the User Story “Write Unit Tests for Book Service” to “In Progress”.
2.  **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-unit-tests
    ```
3. **Write Unit Tests:**
   * Use JUnit for writing unit tests for backend code. Here are the specific tests for the `BookService` methods: `saveBook` and `getAllBooks`.

#### **Unit Test for saveBook Method**

Create a test class `BookServiceTest.java` in the `src/test/java/com/example/LibraryManagementSystem/service` package. You can go to the BookService class and right-click generate:

```java
package com.example.LibraryManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.LibraryManagementSystem.model.Author;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.repository.AuthorRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Test class for BookService.
 */
public class BookServiceTest {

    // Injecting mocks into the bookService instance
    @InjectMocks
    private BookService bookService;

    // Mocking the AuthorRepository
    @Mock
    private AuthorRepository authorRepository;

    // Mocking the BookRepository
    @Mock
    private BookRepository bookRepository;

    /**
     * Sets up the mock objects before each test.
     */
    @BeforeEach
    void setUp() {
        // Initializing mocks
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the saveBook method in BookService.
     * It verifies that the book is saved correctly.
     */
    @Test
    void saveBook_shouldReturnSavedBook() {
        // Creating a test author
        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        // Creating a test book
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor(author);
        book.setIsbn("123-456-789");
        book.setPublicationYear(2021);

        // Mocking the behavior of authorRepository and bookRepository
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(book);

        // Calling the saveBook method
        Book savedBook = bookService.saveBook(book);

        // Asserting that the saved book is not null
        assertNotNull(savedBook);
        // Asserting that the title of the saved book is "Test Book"
        assertEquals("Test Book", savedBook.getTitle());
        // Asserting that the name of the author of the saved book is "Test Author"
        assertEquals("Test Author", savedBook.getAuthor().getName());
        // Asserting that the ISBN of the saved book is "123-456-789"
        assertEquals("123-456-789", savedBook.getIsbn());
        // Asserting that the publication year of the saved book is 2021
        assertEquals(2021, savedBook.getPublicationYear());

        // Verifying that the findById method of authorRepository was called exactly once with the correct author ID
        verify(authorRepository, times(1)).findById(author.getId());
        // Verifying that the save method of bookRepository was called exactly once with the correct book object
        verify(bookRepository, times(1)).save(book);
    }
}
```

#### **Explanation of Code:**

* **MockitoAnnotations.openMocks(this)**: Initializes the mocks for this test class.
* **@InjectMocks**: Injects the mocks into the instance of `BookService` to test it.
* **@Mock**: Creates mock instances of `AuthorRepository` and `BookRepository`.
* **saveBook\_shouldReturnSavedBook()**: Tests the `saveBook` method to ensure it correctly saves a book and returns it.

#### **Commit the Unit Test for saveBook Method**

1.  **Add the Test File to Git:**

    ```bash
    git add .
    ```
2.  **Commit the saveBook Unit Test:**

    ```bash
    git commit -m "Add unit test for BookService saveBook method. Work Item #105"
    ```

#### **Unit Test for getAllBooks Method**

Add the following test to `BookServiceTest.java`:

```java
/**
 * Test case for the getAllBooks method in BookService.
 * It verifies that the method returns a list of books correctly.
 */
@Test
void getAllBooks_shouldReturnListOfBooks() {
    // Creating a test author
    Author author = new Author();
    author.setId(1L);
    author.setName("Test Author");

    // Creating the first test book
    Book book1 = new Book();
    book1.setTitle("Test Book 1");
    book1.setAuthor(author);
    book1.setIsbn("111-222-333");
    book1.setPublicationYear(2020);

    // Creating the second test book
    Book book2 = new Book();
    book2.setTitle("Test Book 2");
    book2.setAuthor(author);
    book2.setIsbn("444-555-666");
    book2.setPublicationYear(2021);

    // Creating a list of test books
    List<Book> bookList = Arrays.asList(book1, book2);

    // Mocking the behavior of bookRepository to return the list of test books
    when(bookRepository.findAll()).thenReturn(bookList);

    // Calling the getAllBooks method
    List<Book> result = bookService.getAllBooks();

    // Asserting that the result is not null
    assertNotNull(result);
    // Asserting that the size of the result list is 2
    assertEquals(2, result.size());

    // Validating details of the first book in the result list
    assertEquals("Test Book 1", result.get(0).getTitle());
    assertEquals("Test Author", result.get(0).getAuthor().getName());
    assertEquals("111-222-333", result.get(0).getIsbn());
    assertEquals(2020, result.get(0).getPublicationYear());

    // Validating details of the second book in the result list
    assertEquals("Test Book 2", result.get(1).getTitle());
    assertEquals("Test Author", result.get(1).getAuthor().getName());
    assertEquals("444-555-666", result.get(1).getIsbn());
    assertEquals(2021, result.get(1).getPublicationYear());

    // Verifying that the findAll method of bookRepository was called exactly once
    verify(bookRepository, times(1)).findAll();
}
```

#### **Explanation of Code:**

* **getAllBooks\_shouldReturnListOfBooks()**: Tests the `getAllBooks` method to ensure it returns a list of books.
* **when(bookRepository.findAll()).thenReturn(bookList)**: Mocks the behavior of `bookRepository.findAll()` to return a predefined list of books.
* **assertNotNull(result)**: Asserts that the result is not null.
* **assertEquals**: Verifies that the expected and actual values are equal.

#### **Commit the Unit Test for getAllBooks Method**

1.  **Add the Changes to Git:**

    ```bash
    git add .
    ```
2.  **Commit the getAllBooks Unit Test:**

    ```bash
    git commit -m "Add unit test for BookService getAllBooks method. Work Item #106"
    ```

#### **Push Changes to Azure DevOps**

1.  **Push the Changes to the Feature Branch:**

    ```bash
    git push origin feature/add-unit-tests
    ```

#### **Syncing Branches**

1. **Create a Pull Request to Merge `feature/add-unit-tests` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-unit-tests`
     * Target branch: `dev`
     * Title: "Add unit tests for BookService methods"
     * Description: "This PR adds unit tests for the BookService methods saveBook and getAllBooks."
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
   * Change the state of the User Story “Write Unit Tests for Book Service” to “Done”.
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

Integration testing verifies that different modules or services used by your application work well together. This section details the steps to set up and execute integration tests for the `BookController` class using RestAssured, focusing on the `addBook` and `getBooks` endpoints.

1. **Change the State of the User Story:**
   * Change the state of the User Story “Write Integration Tests for Book API” to “In Progress”.
2.  **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-integration-tests
    ```
3.  **Add Dependencies:**

    * Ensure that the following dependencies are added to your `pom.xml` file for RestAssured:

    ```xml
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>
    ```
4.  **Configure the Test Database:**

    * In your `src/test/resources` directory, create an `application-test.properties` file to configure the In Memory test database for testing:

    ```sql
    spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    spring.datasource.driver-class-name=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password

    spring.jpa.hibernate.ddl-auto=create-drop
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

    spring.h2.console.enabled=true
    ```
5. **Write Integration Tests:**
   * Use RestAssured for writing integration tests for the API endpoints. Here are the specific tests for the `BookController` methods: `addBook` and `getBooks`.

#### **Integration Test for addBook Endpoint**

Create a test class `BookControllerIntegrationTest.java` in the `src/test/java/com/example/LibraryManagementSystem/controller` package:

```java
package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.Author;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.repository.AuthorRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration tests for the BookController.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    // Injecting the port where the server is running
    @LocalServerPort
    private int port;

    // Injecting the BookRepository
    @Autowired
    private BookRepository bookRepository;

    // Injecting the AuthorRepository
    @Autowired
    private AuthorRepository authorRepository;

    // Variable to hold a test author
    private Author author;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        // Setting the port for RestAssured
        RestAssured.port = port;
        // Clearing all records in bookRepository and authorRepository
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        // Creating and saving a test author
        author = new Author();
        author.setName("Test Author");
        author = authorRepository.save(author);
    }

    /**
     * Test case for adding a book through the API.
     * It verifies that the book is added successfully and returns a CREATED status.
     */
    @Test
    void addBook_shouldReturnCreatedStatus() {
        // JSON representation of the book to be added
        String bookJson = "{\"title\":\"Test Book\",\"isbn\":\"123456789\",\"publicationYear\":2021,\"author\":{\"id\":" + author.getId() + "}}";

        // Sending a POST request to add the book and verifying the response
        given()
                .contentType(ContentType.JSON) // Setting the content type to JSON
                .body(bookJson) // Setting the request body
                .when()
                .post("/api/books") // Sending a POST request to /api/books
                .then()
                .statusCode(HttpStatus.CREATED.value()) // Asserting that the status code is 201 CREATED
                .body("title", equalTo("Test Book")) // Asserting that the title in the response is "Test Book"
                .body("isbn", equalTo("123456789")) // Asserting that the ISBN in the response is "123456789"
                .body("publicationYear", equalTo(2021)) // Asserting that the publication year in the response is 2021
                .body("author.id", equalTo(author.getId().intValue())); // Asserting that the author ID in the response matches the test author ID
    }
}
```

#### **Explanation of Code:**

* **RestAssured.port = port**: Configures RestAssured to use the port of the Spring Boot application.
* **@ActiveProfiles("test")**: Activates the "test" profile for the Spring Boot application.
* **addBook\_shouldReturnCreatedStatus()**: Tests the `addBook` endpoint to ensure it correctly adds a book and returns the created status.

#### **Commit the Integration Test for addBook Endpoint**

1.  **Add the Test File to Git:**

    ```bash
    git add .
    ```
2.  **Commit the addBook Integration Test:**

    ```bash
    git commit -m "Add integration test for BookController addBook endpoint. Work Item #107"
    ```

#### **Integration Test for getBooks Endpoint**

Add the following test to `BookControllerIntegrationTest.java`:

```java
/**
 * Test case for the getBooks method in BookController.
 * It verifies that the method returns a list of books correctly.
 */
@Test
void getBooks_shouldReturnListOfBooks() {
    // Creating and saving the first test book
    Book book1 = new Book();
    book1.setTitle("Test Book 1");
    book1.setIsbn("1234567890");
    book1.setPublicationYear(2021);
    book1.setAuthor(author);
    bookRepository.save(book1);

    // Creating and saving the second test book
    Book book2 = new Book();
    book2.setTitle("Test Book 2");
    book2.setIsbn("0987654321");
    book2.setPublicationYear(2022);
    book2.setAuthor(author);
    bookRepository.save(book2);

    // Sending a GET request to retrieve the list of books and verifying the response
    given()
            .contentType(ContentType.JSON) // Setting the content type to JSON
            .when()
            .get("/api/books") // Sending a GET request to /api/books
            .then()
            .statusCode(HttpStatus.OK.value()) // Asserting that the status code is 200 OK
            .body("size()", equalTo(2)) // Asserting that the size of the response list is 2
            .body("[0].title", equalTo("Test Book 1")) // Asserting that the title of the first book is "Test Book 1"
            .body("[0].isbn", equalTo("1234567890")) // Asserting that the ISBN of the first book is "1234567890"
            .body("[0].publicationYear", equalTo(2021)) // Asserting that the publication year of the first book is 2021
            .body("[0].author.id", equalTo(author.getId().intValue())) // Asserting that the author ID of the first book matches the test author ID
            .body("[0].author.name", equalTo(author.getName())) // Asserting that the author name of the first book matches the test author name
            .body("[1].title", equalTo("Test Book 2")) // Asserting that the title of the second book is "Test Book 2"
            .body("[1].isbn", equalTo("0987654321")) // Asserting that the ISBN of the second book is "0987654321"
            .body("[1].publicationYear", equalTo(2022)) // Asserting that the publication year of the second book is 2022
            .body("[1].author.id", equalTo(author.getId().intValue())) // Asserting that the author ID of the second book matches the test author ID
            .body("[1].author.name", equalTo(author.getName())); // Asserting that the author name of the second book matches the test author name
}
```

#### **Explanation of Code:**

* **getBooks\_shouldReturnListOfBooks()**: Tests the `getBooks` endpoint to ensure it returns a list of books.
* **bookRepository.save(book1)**: Saves `book1` to the test database.
* **given().contentType(ContentType.JSON).when().get("/api/books")**: Sends a GET request to the `getBooks` endpoint and verifies the response.

#### **Commit the Integration Test for getBooks Endpoint**

1.  **Add the Changes to Git:**

    ```bash
    git add .
    ```
2.  **Commit the getBooks Integration Test:**

    ```bash
    git commit -m "Add integration test for BookController getBooks endpoint. Work Item #108"
    ```

#### **Push Changes to Azure DevOps**

1.  **Push the Changes to the Feature Branch:**

    ```bash
    git push origin feature/add-integration-tests
    ```

#### **Syncing Branches**

1. **Create a Pull Request to Merge `feature/add-integration-tests` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-integration-tests`
     * Target branch: `dev`
     * Title: "Add integration tests for BookController endpoints"
     * Description: "This PR adds integration tests for the BookController endpoints addBook and getBooks."
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
   * Change the state of the User Story “Write Integration Tests for Book API” to “Done”.
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

End-to-end (E2E) testing ensures that the entire application flow works as expected from start to finish. This section provides a comprehensive guide to implementing E2E tests for the Book Management functionality using Selenium WebDriver, including tests for adding and deleting books.

1. **Change the State of the User Story:**
   * Change the state of the User Story “Write End-to-End Tests for Book Management” to “In Progress”.
2.  **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-end-to-end-tests
    ```
3.  **Add Dependencies:**

    * Ensure that the following dependencies are added to your `pom.xml` file for Selenium WebDriver:

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
4. **Update the Test Class to Use WebDriverManager:**

Update the `BookManagementE2ETest.java` class to use WebDriverManager:

```java
package com.example.LibraryManagementSystem.e2e;

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
 * End-to-end tests for the Book Management system.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class BookManagementE2ETest {

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

        // Add an author to use in the tests
        addTestAuthor();
    }

    /**
     * Tears down the WebDriver and clears test data after each test.
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Books")));
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
        // Deleting all books
        driver.get(baseUrl + "/books");
        List<WebElement> books = driver.findElements(By.cssSelector("#bookList li"));

        for (WebElement book : books) {
            try {
                WebElement deleteButton = book.findElement(By.cssSelector("button"));
                deleteButton.click();
            } catch (Exception e) {
                System.err.println("Error deleting book: " + e.getMessage());
            }
        }

        // Deleting all authors
        driver.get(baseUrl + "/authors");
        List<WebElement> authors = driver.findElements(By.cssSelector("#authorList li"));

        for (WebElement author : authors) {
            try {
                WebElement deleteButton = author.findElement(By.cssSelector("button"));
                deleteButton.click();
            } catch (Exception e) {
                System.err.println("Error deleting author: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a test author to the system.
     */
    private void addTestAuthor() {
        driver.get(baseUrl + "/authors");
        driver.findElement(By.id("name")).sendKeys("Test Author");
        driver.findElement(By.cssSelector("form#addAuthorForm button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Books")));
        driver.findElement(By.linkText("Books")).click(); // Navigate back to Books page
    }
}
```

#### **Explanation of Code:**

* **WebDriverManager.chromedriver().setup()**: Sets up the WebDriver for Chrome using WebDriverManager.
* **options.addArguments("--headless")**: Configures the Chrome browser to run in headless mode.
* **driver.findElement(By.id("title")).sendKeys("Test Book")**: Finds the element with the ID `title` and enters the text "Test Book".

#### **Write End-to-End Tests:**

* Use Selenium WebDriver for writing end-to-end tests for the application. Here are the specific tests for the `Book Management` functionality: `Add Book` and `Delete Book`.

#### **End-to-End Test for Add Book Functionality**

Add the following test to `BookManagementE2ETest.java`:

```java
/**
 * Test case to verify that a new book is displayed in the list after being added.
 */
@Test
void addBook_shouldDisplayNewBookInList() {
    // Enter the title of the book
    driver.findElement(By.id("title")).sendKeys("Test Book");
    // Enter the ISBN of the book
    driver.findElement(By.id("isbn")).sendKeys("1234567890");
    // Enter the publication year of the book
    driver.findElement(By.id("publicationYear")).sendKeys("2021");
    // Enter the author ID (assuming an author with ID 1 exists)
    driver.findElement(By.id("authorId")).sendKeys("1");
    // Click the submit button to add the book
    driver.findElement(By.cssSelector("form#addBookForm button[type='submit']")).click();

    // Wait until the book list is visible
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookList")));

    // Get the book list element
    WebElement bookList = driver.findElement(By.id("bookList"));
    // Get the text content of the book list
    String pageSource = bookList.getText();

    // Verify that the book list contains the title of the new book
    assertTrue(pageSource.contains("Test Book"));
    // Verify that the book list contains the publication year of the new book
    assertTrue(pageSource.contains("2021"));
    // Verify that the book list contains the name of the author
    assertTrue(pageSource.contains("Test Author"));
}
```

#### **Explanation of Code:**

* **addBook\_shouldDisplayNewBookInList()**: Tests the `addBook` functionality to ensure the new book is displayed in the list.
* **driver.findElement(By.id("title")).sendKeys("Test Book")**: Enters the title "Test Book" in the title field.
* **assertTrue(pageSource.contains("Test Book"))**: Verifies that the page source contains the text "Test Book".

#### **Commit the End-to-End Test for Add Book Functionality**

1.  **Add the Test File to Git:**

    ```bash
    git add .
    ```
2.  **Commit the addBook End-to-End Test:**

    ```bash
    git commit -m "Add end-to-end test for Book Management addBook functionality. Work Item #109"
    ```

#### **End-to-End Test for Delete Book Functionality**

Add the following test to `BookManagementE2ETest.java`:

```java
/**
 * Test case to verify that a book is removed from the list after being deleted.
 */
@Test
void deleteBook_shouldRemoveBookFromList() {
    // First, add a book that will be deleted in this test
    driver.findElement(By.id("title")).sendKeys("Test Book To Delete");
    driver.findElement(By.id("isbn")).sendKeys("0987654321");
    driver.findElement(By.id("publicationYear")).sendKeys("2022");
    // Enter the author ID (assuming an author with ID 1 exists)
    driver.findElement(By.id("authorId")).sendKeys("1");
    // Click the submit button to add the book
    driver.findElement(By.cssSelector("form#addBookForm button[type='submit']")).click();

    // Wait until the book list is visible after adding the book
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookList")));

    // Find the book list element
    WebElement bookList = driver.findElement(By.id("bookList"));
    // Find the delete button for the added book using XPath
    WebElement deleteButton = bookList.findElement(By.xpath("//li[contains(text(), 'Test Book To Delete')]//button[contains(text(), 'Delete')]"));
    // Click the delete button to remove the book
    deleteButton.click();

    // Verify the book is removed by waiting until the delete button is no longer visible
    wait.until(ExpectedConditions.invisibilityOf(deleteButton));
    // Get the text content of the book list
    String pageSource = driver.findElement(By.id("bookList")).getText();
    // Verify that the book list no longer contains the title of the deleted book
    assertTrue(!pageSource.contains("Test Book To Delete"));
}
```

#### **Explanation of Code:**

* **deleteBook\_shouldRemoveBookFromList()**: Tests the `deleteBook` functionality to ensure the book is removed from the list.
* **WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10))**: Waits for up to 10 seconds for an element to be visible.
* **assertTrue(!pageSource.contains("Test Book To Delete"))**: Verifies that the page source does not contain the text "Test Book To Delete".

#### **Commit the End-to-End Test for Delete Book Functionality**

1.  **Add the Changes to Git:**

    ```bash
    git add .
    ```
2.  **Commit the deleteBook End-to-End Test:**

    ```bash
    git commit -m "Add end-to-end test for Book Management deleteBook functionality. Work Item #110"
    ```

#### **Push Changes to Azure DevOps**

1.  **Push the Changes to the Feature Branch:**

    ```bash
    git push origin feature/add-end-to-end-tests
    ```

#### **Syncing Branches**

1. **Create a Pull Request to Merge `feature/add-end-to-end-tests` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-end-to-end-tests`
     * Target branch: `dev`
     * Title: "Add end-to-end tests for Book Management functionality"
     * Description: "This PR adds end-to-end tests for the Book Management functionality, including addBook and deleteBook."
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
   * Change the state of the User Story “Write End-to-End Tests for Book Management” to “Done”.
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

Implementing comprehensive test automation is essential for ensuring the quality and reliability of your software application. By following the detailed steps outlined in this guide, you can effectively create unit, integration, and end-to-end tests that validate your application's functionality at various levels.

Unit tests focus on individual components, verifying their correctness in isolation. Integration tests ensure that different modules interact correctly, while end-to-end tests validate the complete workflow of your application, simulating real user scenarios.

By integrating these automated tests into your development process and using CI/CD pipelines, you can achieve continuous testing, early bug detection, and faster release cycles. This approach not only improves code quality but also enhances the overall user experience, making your application more robust and reliable.

Ensure to keep your tests up-to-date as the application evolves, and continuously monitor and refine your testing strategies to adapt to new challenges and requirements. Effective test automation is a key factor in successful software development and deployment.
