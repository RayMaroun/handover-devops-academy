---
icon: flask
cover: >-
  https://images.unsplash.com/photo-1596496181848-3091d4878b24?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw5fHx3b3Jrc2hvcHxlbnwwfHx8fDE3MjI5MTQyMzR8MA&ixlib=rb-4.0.3&q=85
coverY: 35
---

# Workshop Activity

In this workshop activity, you'll apply the concepts demonstrated in the live demo to set up Git, manage branches, and perform code reviews for the **Bank Management System** project. This exercise will help you practice the workflow, understand the branching strategies, and reinforce your knowledge of Git commands. Follow the instructions below, and feel free to explore additional features and approaches. This page provides instructions and hints; for detailed step-by-step guidance, refer to the live demo page.

### **1. Project Setup**

#### **1.1. Download and Setup Project**

1. **Download the Project:**
   * Use the link provided to download the **BankManagementSystem** project files: [Link](https://drive.google.com/file/d/1hG9Twt9tLIvx-Ko-oBvGU0YdR3TEzQsi/view?usp=sharing).
2. **Unzip the Project:**
   * Extract the downloaded files to a directory of your choice.
3. **Open the Project:**
   * Open the project in your preferred IDE.

***

### **2. Git Setup**

#### **2.1. Initialize Git and Connect to Remote Repository**

1.  **Initialize Git Repository:**

    * In the root directory of the project, initialize a Git repository:

    ```bash
    git init
    ```
2.  **Add Remote Repository:**

    * Add the provided Azure DevOps repository as a remote:

    ```bash
    git remote add origin <PROJECT_URL>
    ```

    Replace `<PROJECT_URL>` with the URL of your assigned Azure DevOps repository.

#### **2.2. Commit and Push Initial Code**

1.  **Stage and Commit Files:**

    * Add all project files and commit the initial setup:

    ```bash
    git add .
    git commit -m "Initial commit"
    ```
2.  **Push to Remote Repository:**

    * Push the changes to the `master` branch:

    ```bash
    git push origin master
    ```

{% hint style="warning" %}
Ensure your commit messages are clear and descriptive, providing context for the changes made.
{% endhint %}

***

### **3. Branching Strategy**

#### **3.1. Create and Push Branches**

1.  **Create and Push `dev` Branch:**

    ```bash
    git checkout -b dev
    git push origin dev
    ```
2.  **Create and Push `staging` Branch:**

    ```bash
    git checkout -b staging
    git push origin staging
    ```

#### **3.2. Configure Branch Policies**

* Set up branch policies for `dev`, `staging`, and `master` in Azure DevOps, similar to the live demo. Ensure at least one reviewer for pull requests and consider enabling build validation.

***

### **4. Feature Branch Workflow**

#### **4.1. Implement a Feature**

* In this activity, you will add the **"openingDate"** field to the **BankAccount** model and update the frontend to include and display this new field.

1.  **Create a Feature Branch:**

    * Choose a user story or task related to this feature.
    * Create a new feature branch:

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-opening-date
    ```
2. **Implement the Feature:**
   * Add the `openingDate` field to the **BankAccount** model

```java
// src/main/java/com/example/BankManagementSystem/model/Account.java

package com.example.BankManagementSystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String accountType;
    private double balance;
    private LocalDate openingDate; // Add this line
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getOpeningDate() {
        return openingDate; // Add this getter
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate; // Add this setter
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
```

*   **Commit and Push Changes:**

    * Commit your changes with a clear message and push the feature branch:

    <pre class="language-bash"><code class="lang-bash"><strong>git add .
    </strong>git commit -m "Update Account Model: Add opening date field. Work Item #123"
    </code></pre>
* &#x20;Update the frontend code accordingly.

```markup
<!-- src/main/resources/templates/accounts.html -->

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Accounts - Bank Management System</title>
    <link rel="stylesheet" th:href="@{/css/styles.css}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            fetchAccounts();

            document.getElementById('addAccountForm').addEventListener('submit', function(event) {
                event.preventDefault();
                addAccount();
            });

            document.getElementById('searchAccountForm').addEventListener('submit', function(event) {
                event.preventDefault();
                searchAccounts();
            });
        });

        function fetchAccounts() {
            fetch('/api/accounts')
                .then(response => response.json())
                .then(accounts => {
                    const accountList = document.getElementById('accountList');
                    accountList.innerHTML = '';
                    accounts.forEach(account => {
                        const li = document.createElement('li');
                        li.textContent = `${account.accountNumber} - ${account.accountType} - Balance: ${account.balance} - Opened on: ${account.openingDate}`;
                        const deleteButton = document.createElement('button');
                        deleteButton.textContent = 'Delete';
                        deleteButton.onclick = () => deleteAccount(account.id);
                        li.appendChild(deleteButton);
                        accountList.appendChild(li);
                    });
                });
        }

        function addAccount() {
            const accountNumber = document.getElementById('accountNumber').value;
            const accountType = document.getElementById('accountType').value;
            const balance = document.getElementById('balance').value;
            const customerId = document.getElementById('customerId').value;
            const openingDate = document.getElementById('openingDate').value;

            fetch('/api/accounts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ accountNumber, accountType, balance, customer: { id: customerId }, openingDate})
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => { throw new Error(data.message); });
                }
                return response.json();
            })
            .then(() => {
                fetchAccounts();
                document.getElementById('addAccountForm').reset();
            })
            .catch(error => alert('Error: ' + error.message));
        }

        function deleteAccount(id) {
            fetch(`/api/accounts/${id}`, {
                method: 'DELETE'
            }).then(() => fetchAccounts());
        }

        function searchAccounts() {
            const accountNumber = document.getElementById('searchAccountNumber').value;
            fetch(`/api/accounts/search?accountNumber=${accountNumber}`)
                .then(response => response.json())
                .then(accounts => {
                    const accountList = document.getElementById('accountList');
                    accountList.innerHTML = '';
                    accounts.forEach(account => {
                        const li = document.createElement('li');
                        li.textContent = `${account.accountNumber} - ${account.accountType} - Balance: ${account.balance} - Opened on: ${account.openingDate}`;
                        const deleteButton = document.createElement('button');
                        deleteButton.textContent = 'Delete';
                        deleteButton.onclick = () => deleteAccount(account.id);
                        li.appendChild(deleteButton);
                        accountList.appendChild(li);
                    });
                });
        }
    </script>
</head>
<body>
<nav>
    <ul>
        <li><a th:href="@{/}">Home</a></li>
        <li><a th:href="@{/accounts}">Accounts</a></li>
        <li><a th:href="@{/customers}">Customers</a></li>
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
    <h1>Accounts</h1>
    <form id="addAccountForm">
        <input type="text" id="accountNumber" placeholder="Account Number" required>
        <select id="accountType" required>
            <option value="SAVINGS">Savings</option>
            <option value="CURRENT">Current</option>
            <option value="FIXED">Fixed Deposit</option>
        </select>
        <input type="number" id="balance" placeholder="Balance" required>
        <input type="date" id="openingDate" required> <!-- Add this line -->
        <select id="customerId" required>
            <option th:each="customer : ${customers}" th:value="${customer.id}" th:text="${customer.name}">Customer</option>
        </select>
        <button type="submit">Add Account</button>
    </form>
    <h2>Search Accounts</h2>
    <form id="searchAccountForm">
        <input type="text" id="searchAccountNumber" placeholder="Search by Account Number">
        <button type="submit">Search</button>
    </form>
    <ul id="accountList"></ul>
</div>
</body>
</html>
```

* **Commit and Push Changes:**
  *   Commit your changes with a clear message and push the feature branch:

      ```bash
      git add .
      git commit -m "Update Frontend: Include and display opening date for accounts. Work Item #125"
      ```
*   **Push Changes:**

    ```bash
    git push origin feature/add-opening-date
    ```

#### **4.2. Create a Pull Request**

1. **Open a Pull Request:**
   * In Azure DevOps, create a pull request from your feature branch to `dev`.
2. **Review and Merge:**
   * Request reviews from your team members.
   * Address any feedback, update your branch, and complete the merge once approved.

{% hint style="info" %}
Remember to update the status of related work items in Azure Boards, if needed.
{% endhint %}

***

### **5. Promotion to Staging and Production**

1. **Promote Changes to `staging`:**
   * After merging your feature branch into `dev`, create a pull request to merge `dev` into `staging`.
2. **Promote Changes to `master`:**
   * Once changes are tested in `staging`, create a pull request to merge `staging` into `master`.

***

### **Conclusion**

This workshop activity provides hands-on experience with Git workflows and Azure Repos. By following the steps outlined, you will practice the essential tasks of setting up repositories, managing branches, and collaborating on code changes through pull requests and code reviews. Feel free to explore additional features and approaches, and don't hesitate to ask for assistance if needed. For more detailed instructions and examples, refer to the live demo page.
