---
icon: flask
cover: >-
  https://images.unsplash.com/photo-1607969893000-18f7f136b982?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw3fHx3b3Jrc2hvcHxlbnwwfHx8fDE3MjI4MDg1OTl8MA&ixlib=rb-4.0.3&q=85
coverY: 145
---

# Workshop Activity

In this workshop activity, you will replicate the setup demonstrated in the live demo for the **Bank Management System**. This exercise will help you apply Agile project management practices in Azure DevOps by setting up boards, customizing work items, and defining processes. Follow the instructions below and use your creativity while adhering to the provided structure.

### 1. Access the Azure DevOps Project

#### 1.1. Log into Azure DevOps

* Access the Azure DevOps portal and log in using your credentials.

{% hint style="info" %}
Ensure you have the necessary permissions to access the project named **"BankManagementSystem-StudentFirstName"**. The "-StudentFirstName" suffix is a placeholder and should be replaced with your own name. If you encounter any issues, verify with your instructor or project administrator that you've been added to the project.
{% endhint %}

#### 1.2. Access the Assigned Project

* Locate and open the project named **"BankManagementSystem-StudentFirstName"**, which has been pre-created for you.

### 2. Set Up Azure Boards

#### 2.1. Enable New Boards Hub

* Ensure the **New Boards Hub** feature is enabled for the latest board functionalities.

#### 2.2. Customize the Kanban Board

* Access the **Boards** section and customize the columns (e.g., **To Do**, **In Progress**, **Done**). Set appropriate Work in Progress (WIP) limits.

### 3. Create an Inherited Process and Update the Project

#### 3.1. Create an Inherited Process

* Create an inherited process from **Agile** and name it **"Kanban-StudentFirstName"**.

#### 3.2. Customize the Inherited Process

* Update the states for **Epic**, **Feature**, **Task**, and **User Story** to align with your board columns.

#### 3.3. Assign the Process to Your Project

* Change the project process to **Kanban-StudentFirstName**.

{% hint style="danger" %}
Changing the process will affect all work items in the project. Ensure that the new process is fully configured before making the switch.
{% endhint %}

### 4. Automate Work Item State Changes

* Set up automation rules to complete parent work items when all child items are done.

### 5. Add and Configure Work Items

Create and configure the following work items in your project. You can refer to the live demo for guidance:

#### Required Work Items

{% hint style="danger" %}
You must create the following work items as they are shown here, as they are needed for later stages. Once it is done, feel free to add more based on your preferences.
{% endhint %}

* **Epics**:
  1. **Title**: "Bank Operations Management"
     * **Description**: "Implement core functionalities for managing bank operations including account management and customer management."
  2. **Title**: "User Account Management"
     * **Description**: "Implement functionalities for managing user accounts, including registration, authentication, and profile management."
  3. **Title**: "Testing Strategy Implementation"
     * **Description**: "Implement comprehensive testing strategies including unit testing, integration testing, and end-to-end testing for the Bank Management System."

{% hint style="info" %}
Epics provide a high-level view of significant functionalities and help in organizing related features and tasks.
{% endhint %}

* **Features**:
  1. **Title**: "Add Opening Date to Customer Account"
     * **Description**: "Enhance the customer account management module to include the opening date for each account."
     * **Link to Epic**: "Bank Operations Management"
  2. **Title**: "Add Interest Rate to Customer Account"
     * **Description**: "Enhance the customer account management module to include the interest rate for each account."
     * **Link to Epic**: "Bank Operations Management"
  3. **Title**: "Unit Testing Implementation"
     * **Description**: "Implement unit tests for backend code using JUnit."
     * **Link to Epic**: "Testing Strategy Implementation"
  4. **Title**: "Integration Testing Implementation"
     * **Description**: "Implement integration tests for API endpoints using RestAssured."
     * **Link to Epic**: "Testing Strategy Implementation"
  5. **Title**: "End-to-End Testing Implementation"
     * **Description**: "Implement end-to-end tests for the application using Selenium."
     * **Link to Epic**: "Testing Strategy Implementation"
* **User Stories**:
  1. **Title**: "Add New Account with Opening Date"
     * **Description**: "As a banker, I want to create new accounts with specific opening dates so that customers can have different opening dates."
     * **Link to Feature**: "Add Opening Date to Customer Account"
  2. **Title**: "Add New Account with Interest Rate"
     * **Description**: "As a banker, I want to create new accounts with specific interest rates so that customers can have different interest rates"
     * **Link to Feature**: "Add Interest Rate to Customer Account"
  3. **Title**: "Write Unit Tests for Account Service"
     * **Description**: "As a developer, I want to write unit tests for the Account Service to ensure its methods function correctly."
     * **Link to Feature**: "Unit Testing Implementation"
  4. **Title**: "Write Integration Tests for Account API"
     * **Description**: "As a developer, I want to write integration tests for the Account API to ensure the endpoints function correctly."
     * **Link to Feature**: "Integration Testing Implementation"
  5. **Title**: "Write End-to-End Tests for Account Management"
     * **Description**: "As a tester, I want to write end-to-end tests for the Account Management feature to ensure the UI and backend integration function correctly."
     * **Link to Feature**: "End-to-End Testing Implementation"
* **Tasks**:
  1. **Title**: "Update Account Model for Opening Date"
     * **Description**: "Add the opening date field to the Account model class."
     * **Link to User Story**: "Add New Account with Opening Date"
  2. **Title**: "Update Frontend for Opening Date"
     * **Description**: "Modify the frontend templates to include and display the Opening Date for accounts."
     * **Link to User Story**: "Add New Account with Opening Date"
  3. **Title**: "Update Account Model for Interest Rate"
     * **Description**: "Add the interest rate field to the Account model class."
     * **Link to User Story**: "Add New Account with Interest Rate"
  4. **Title**: "Update Frontend for Transaction Categories"
     * **Description**: "Modify the frontend templates to include and display the Interest Rate for accounts."
     * **Link to User Story**: "Add New Account with Interest Rate"
  5. **Title**: "Create Unit Tests for createAccount Method"
     * **Description**: "Write unit tests for the createAccount method in Account Service."
     * **Link to User Story**: "Write Unit Tests for Account Service"
  6. **Title**: "Create Unit Tests for getAllAccounts Method"
     * **Description**: "Write unit tests for the getAllAccounts method in Account Service."
     * **Link to User Story**: "Write Unit Tests for Account Service"
  7. **Title**: "Create Integration Tests for addAccount Endpoint"
     * **Description**: "Write integration tests for the addAccount endpoint in Account API."
     * **Link to User Story**: "Write Integration Tests for Account API"
  8. **Title**: "Create Integration Tests for getAccounts Endpoint"
     * **Description**: "Write integration tests for the getAccounts endpoint in Account API."
     * **Link to User Story**: "Write Integration Tests for Account API"
  9. **Title**: "Create End-to-End Tests for Add Account Functionality"
     * **Description**: "Write end-to-end tests for the Add Account functionality in the UI."
     * **Link to User Story**: "Write End-to-End Tests for Account Management"
  10. **Title**: "Create End-to-End Tests for Delete Account Functionality"
      * **Description**: "Write end-to-end tests for the Delete Account functionality in the UI."
      * **Link to User Story**: "Write End-to-End Tests for Account Management"

{% hint style="info" %}
Ensure all required work items are added and linked appropriately.
{% endhint %}

***

#### Conclusion

This workshop activity allows you to apply the concepts demonstrated in the live demo, giving you hands-on experience with setting up an Agile project in Azure DevOps. Use this opportunity to familiarize yourself with the platform, experiment with configurations, and ensure you understand the workflow and customization options available. If you encounter challenges or have questions, don't hesitate to ask for assistance.
