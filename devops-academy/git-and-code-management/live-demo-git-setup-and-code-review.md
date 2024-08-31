---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1518773553398-650c184e0bb3?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwzfHxjb2RlfGVufDB8fHx8MTcyMjkxMTcwN3ww&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Live Demo - Git Setup and Code Review

In this live demo, we'll set up Git for the **Library Management System**, connect to Azure Repos, create branches, set up branching policies, and demonstrate a full feature implementation with a pull request and code review process. This session will give you hands-on experience with Git operations and best practices for collaborative development.

### **1. Project Initialization**

#### **1.1. Project Verification**

1. **Open the LibraryManagementSystem project and check it.**
2. **Ensure the database is created and run the project.**
3. **Access the project from the browser using** [**http://localhost:8080**](http://localhost:8080)**.**
4. **Test the functionalities of the project.**
5. **Stop the project once you are done.**

***

### **2. Git Setup**

#### **2.1. Initialize Git**

1. **Navigate to the Root Directory:**
   * Open a terminal or command prompt in the root directory of your project.
2.  **Initialize Git Repository:**

    * Run the following command to initialize a new Git repository:

    ```bash
    git init
    ```

#### **2.2. Add Remote Repository**

1.  **Add Azure DevOps Repository:**

    * Add the Azure DevOps repository as a remote:

    ```bash
    git remote add origin <PROJECT_URL>
    ```

> Note: \<PROJECT\_URL> should be replaced by the project provided in your Azure Repos.

#### **2.3. Commit and Push Changes**

1.  **Stage All Files:**

    * Add all files to the repository:

    ```bash
    git add .
    ```
2.  **Set User Information:**

    * Set the username and email for the repository:

    ```bash
    git config user.name "Your Name"
    git config user.email "your_email@example.com"
    ```
3.  **Commit Changes:**

    * Commit the initial code:

    ```bash
    git commit -m "Initial commit"
    ```
4.  **Push Changes:**

    * Push the changes to the `master` branch:

    ```bash
    git push origin master
    ```
5. You will be prompted by an Authentication Pop. Login using your Microsoft Account.

{% hint style="info" %}
You should now be able to see your project in Azure Repos.
{% endhint %}

***

### **3. Branching Strategy**

#### **3.1. Create Branches**

1.  **Create and Push Development Branch:**

    ```bash
    git checkout -b dev
    git push origin dev
    ```
2.  **Create and Push Staging Branch:**

    ```bash
    git checkout -b staging
    git push origin staging
    ```
3. **Branch Purposes:**
   * `dev`: For ongoing development. All new features and bug fixes should be integrated here.
   * `staging`: For testing and quality assurance. This branch is a pre-production environment.
   * `master`: For production-ready code. This branch should always represent the latest stable release.

#### **3.2. Configure Branch Policies**

1. **Navigate to Repos -> Branches:**
   * In your Azure DevOps project, go to **Repos** and select **Branches**.
2. **Set Policies for Branches:**
   * Click on the ellipsis (three dots) next to each branch (e.g., `dev`, `staging`, `master`) and select **Branch policies**.
   * Configure the following policies:
     * **Minimum number of reviewers:** Set to **1**.
     * **Allow requestor to approve their own changes:** Enable this option.
     * Mention that this is possible but we will not be adding it here: Enforce successful build validation before the pull request can be completed.

***

### **4. Feature Branch Workflow**

#### **4.1. Feature Implementation**

* **Change Work Item State:**
  * Set the User Story “Add New Book with Publication Year” to **In Progress**.
*   **Create a Feature Branch:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-publication-year
    ```
*   **Implement the Feature:**

    * **Model Update:**
      * Add a `publicationYear` field to the `Book` model.

    ```java
    // src/main/java/com/example/LibraryManagementSystem/model/Book.java

    package com.example.LibraryManagementSystem.model;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "books")
    public class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        @ManyToOne
        @JoinColumn(name = "author_id", nullable = false)
        private Author author;
        private String isbn;
        private int publicationYear; // Add this line

        // Getters and Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public int getPublicationYear() {
            return publicationYear; // Add this getter
        }

        public void setPublicationYear(int publicationYear) {
            this.publicationYear = publicationYear; // Add this setter
        }
    }
    ```

    *   **Commit Changes:**

        ```bash
        git add .
        git commit -m "Update Book Model: Add publication year field. Work Item #123"
        ```

        \
        The “#123” should be replaced with the actual id of the task that you working on. This is will attach the commit directly to your task.
    * **Frontend Update:**
      * Update the frontend template to include the `publicationYear` field.

    ```html
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Books - Library Management System</title>
        <link rel="stylesheet" th:href="@{/css/styles.css}">
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                fetchBooks();

                document.getElementById('addBookForm').addEventListener('submit', function(event) {
                    event.preventDefault();
                    addBook();
                });

                document.getElementById('searchBookForm').addEventListener('submit', function(event) {
                    event.preventDefault();
                    searchBooks();
                });
            });

            function fetchBooks() {
                fetch('/api/books')
                    .then(response => response.json())
                    .then(books => {
                        const bookList = document.getElementById('bookList');
                        bookList.innerHTML = '';
                        books.forEach(book => {
                            const li = document.createElement('li');
                            li.textContent = `${book.title} by ${book.author.name} - Published in ${book.publicationYear}`;
                            const deleteButton = document.createElement('button');
                            deleteButton.textContent = 'Delete';
                            deleteButton.onclick = () => deleteBook(book.id);
                            li.appendChild(deleteButton);
                            bookList.appendChild(li);
                        });
                    });
            }

            function addBook() {
                const title = document.getElementById('title').value;
                const authorId = document.getElementById('authorId').value;
                const isbn = document.getElementById('isbn').value;
                const publicationYear = document.getElementById('publicationYear').value; // Add this line

                fetch('/api/books', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ title, author: { id: authorId }, isbn, publicationYear}) // Add publication year here
                })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(data => { throw new Error(data.message); });
                    }
                    return response.json();
                })
                .then(() => {
                    fetchBooks();
                    document.getElementById('addBookForm').reset();
                })
                .catch(error => alert('Error: ' + error.message));
            }

            function deleteBook(id) {
                fetch(`/api/books/${id}`, {
                    method: 'DELETE'
                }).then(() => fetchBooks());
            }

            function searchBooks() {
                const title = document.getElementById('searchTitle').value;
                fetch(`/api/books/search?title=${title}`)
                    .then(response => response.json())
                    .then(books => {
                        const bookList = document.getElementById('bookList');
                        bookList.innerHTML = '';
                        books.forEach(book => {
                            const li = document.createElement('li');
                            li.textContent = `${book.title} by ${book.author.name} - Published in ${book.publicationYear}`;
                            const deleteButton = document.createElement('button');
                            deleteButton.textContent = 'Delete';
                            deleteButton.onclick = () => deleteBook(book.id);
                            li.appendChild(deleteButton);
                            bookList.appendChild(li);
                        });
                    });
            }
        </script>
    </head>
    <body>
    <nav>
        <ul>
            <li><a th:href="@{/}">Home</a></li>
            <li><a th:href="@{/books}">Books</a></li>
            <li><a th:href="@{/authors}">Authors</a></li>
        </ul>
        <ul>
            <li>
                <form th:action="@{/logout}" method="post">
                    <button type="submit">Logout</button>
                </form>
            </li>
        </ul>
    </nav>
    <div class="container">
        <h1>Books</h1>
        <form id="addBookForm">
            <input type="text" id="title" placeholder="Title" required>
            <select id="authorId" required>
                <option th:each="author : ${authors}" th:value="${author.id}" th:text="${author.name}">Author</option>
            </select>
            <input type="text" id="isbn" placeholder="ISBN" required>
            <input type="number" id="publicationYear" placeholder="Publication Year" required> <!-- Add this line -->
            <button type="submit">Add Book</button>
        </form>
        <h2>Search Books</h2>
        <form id="searchBookForm">
            <input type="text" id="searchTitle" placeholder="Search by Title">
            <button type="submit">Search</button>
        </form>
        <ul id="bookList"></ul>
    </div>
    </body>
    </html>
    ```

    *   **Commit Changes:**

        ```bash
        git add .
        git commit -m "Update Frontend: Include and display publication year for books. Work Item #125"
        ```

        \
        The “#125” should be replaced with the actual id of the task that you working on. This is will attach the commit directly to your task.
*   **Push Changes:**

    ```bash
    git push origin feature/add-publication-year
    ```

#### **4.2. Create and Review Pull Request**

1. **Create Pull Request:**
   * Navigate to **Repos -> Pull Requests** in Azure DevOps.
   * Create a new pull request from `feature/add-publication-year` to `dev`.
     * Source branch: `feature/add-publication-year`
     * Target branch: `dev`
     * Title: "Add publication year to Book model"
     * Description: "This PR adds a publication year field to the Book model, and modifies the frontend template to display the publication year."
2. **Assign Reviewers:**
   * Add team members as reviewers.
3. **Complete Code Review:**
   * Reviewers review the code, add comments, and request changes if necessary.
   * Address feedback, commit, and push any changes.
   * Once approved, merge the pull request.
4. **Update Work Items:**
   * Set the Feature “Add Publication Year to Book” and User Story “Add New Book with Publication Year” to **Done,** if not updated already.

#### **4.3. Merge Pull Request**

1.  **Delete Local Feature Branch:**

    ```bash
    git checkout dev
    git branch -D feature/add-publication-year
    ```
2. **Merge to `dev`:**
   * Once the pull request is approved, merge it into the `dev` branch via Azure DevOps, if not set automatically.
   * Update the status of the work items linked to this PR to "Done" if not updated already.
3. **Promote to `staging`:**
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
     * Once the code is approved, merge the pull request using **Rebase and FF**.
4.  **Update Local \`staging\` Branch:**

    ```bash
    git checkout staging
    git pull origin staging
    ```
5. **Promote to `master`:**
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
     * Address any feedback by making changes in the `dev` branch, committing, and pushing the changes.
     * Once the code is approved, merge the pull request using **Rebase and FF**.
6.  **Update Local \`staging\` Branch:**

    ```bash
    git checkout master
    git pull origin master
    ```

***

### **Conclusion**

In this live demo, we walked through setting up Git for the Library Management System, creating branches, and implementing a feature using the feature branch workflow. We've demonstrated the complete process from creating a feature branch to merging it into the main codebase. This hands-on session has equipped you with the essential Git skills for managing code changes and collaborating effectively in a team.
