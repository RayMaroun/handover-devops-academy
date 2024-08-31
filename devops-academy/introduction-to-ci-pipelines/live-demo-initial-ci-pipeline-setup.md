---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1613601084252-6ac0b63f9d0b?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw2fHxhenVyZXxlbnwwfHx8fDE3MjMxMjU5ODZ8MA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Live Demo - Initial CI Pipeline Setup

### **Introduction**

In this live demo, we will walk through the steps to set up an initial Continuous Integration (CI) pipeline. This pipeline will automate the build process for our project, ensuring that every code change is compiled, tested, and ready for integration. Follow the steps below to create, configure, and run your CI pipeline.

***

### **Step-by-Step Guide to Setting Up CI Pipeline**

#### **Step 1: Create a Feature Branch**

Start by creating a feature branch to isolate your changes.

```bash
git checkout dev
git pull origin dev
git checkout -b feature/add-ci-pipeline
```

***

### **Step 2: Create and Configure the CI Pipeline**

#### **Define the Pipeline YAML File:**

1.  **Create a New YAML File:**

    Create a new file named `ci-pipeline.yml` in the root directory of your project.\

2.  **Add Trigger Configuration:**

    Define when the pipeline should be triggered.

    ```yaml
    # ci-pipeline.yml

    # Trigger the pipeline on changes to the specified branches
    trigger:
      branches:
        include:
          - dev # Trigger pipeline on changes to the 'dev' branch
          - staging # Trigger pipeline on changes to the 'staging' branch
          - master # Trigger pipeline on changes to the 'master' branch
    ```

    _Explanation:_

    * The `trigger` section specifies that the pipeline should automatically run whenever there are changes to the `dev`, `staging`, or `master` branches.\

3.  **Specify the Build Agent:**

    Choose the virtual machine image for the build agent.

    ```yaml
    # Specify the virtual machine image for the build agent
    pool:
      vmImage: "ubuntu-latest" # Use the latest Ubuntu image for the build agent
    ```

    _Explanation:_

    * The `pool` section defines the agent that will run the jobs. In this case, it specifies the use of the latest Ubuntu image.\

4.  **Define Jobs and Steps:**

    Define the jobs to be executed in the pipeline and the steps within each job.

    ```yaml
    # Define the jobs to be executed in the pipeline
    jobs:
      - job: Build
        displayName: "Build"
        steps:
          # Install Java 17
          - task: JavaToolInstaller@0
            inputs:
              versionSpec: "17" # Specify Java version 17
              jdkArchitectureOption: "x64" # Use 64-bit architecture
              jdkSourceOption: "PreInstalled" # Use pre-installed JDK if available
            displayName: "Install Java 17"
    ```

    _Explanation:_

    * The `jobs` section defines a job named `Build` with a display name of "Build".
    * The `steps` section within the job specifies the individual tasks to be executed.
    * The `JavaToolInstaller@0` task installs Java 17, using 64-bit architecture and pre-installed JDK if available.

    {% code overflow="wrap" %}
    ```yaml
    # Verify Java Installation
          - script: |
              echo "JAVA_HOME environment variable: $JAVA_HOME"
              java -version
            displayName: "Verify Java Installation" # Output JAVA_HOME and Java version to verify installation
    ```
    {% endcode %}

    _Explanation:_

    * The `script` step runs a shell script to print the `JAVA_HOME` environment variable and the Java version. This verifies that Java 17 was installed correctly.

    ```yaml
    # Compile Project
          - task: Maven@3
            inputs:
              mavenVersionOption: "Default" # Use the default Maven version
              mavenPomFile: "pom.xml" # Path to the Maven POM file
              goals: "clean compile" # Goals to clean and compile the project
            displayName: "Compile Project"
    ```

    _Explanation:_

    * The `Maven@3` task uses Maven to clean and compile the project. The `mavenPomFile` specifies the path to the `pom.xml` file, and the `goals` are set to `clean compile`.

***

#### **Create a Pull Request to Merge `feature/add-ci-pipeline` into `dev`:**

1. **Navigate to Azure DevOps:**
   * Go to Repos -> Pull Requests.
2. **Create a New Pull Request:**
   * Source branch: `feature/add-ci-pipeline`
   * Target branch: `dev`
   * Title: "Add CI Pipeline"
   * Description: "This PR adds CI Pipeline to our system."
3. **Assign Reviewers:**
   * Add team members as reviewers.
4. **Complete Code Review:**
   * Reviewers will review the changes, add comments, and request changes if necessary.
   * Address any feedback by making changes in the `feature/add-ci-pipeline` branch, committing, and pushing the changes.
   * Once the code is approved, merge the pull request USING MERGE.

***

#### **Delete Locally the `feature/add-ci-pipeline` Branch:**

```bash
git checkout dev
git branch -D feature/add-ci-pipeline
```

***

#### **Pull `dev` Locally:**

```bash
git checkout dev
git pull origin dev
```

***

### **Step 3: Create Pipeline from YAML on Azure DevOps**

1. **Go to Pipelines Page:**
   * Navigate to the Pipelines page in Azure DevOps.
2. **Click on New Pipeline:**
   * Select the option to create a new pipeline.
3. **Choose Azure Repos:**
   * Select the Azure Repos option.
4. **Choose Your Repository:**
   * Select the repository you are working on.
5. **Select Existing Azure Pipelines YAML File:**
   * Choose to use an existing Azure Pipelines YAML file.
6. **Select the `dev` Branch and YAML File:**
   * Switch to the `dev` branch and select the newly added `ci-pipeline.yml` file.
7. **Click on Continue:**
   * Proceed to the next step.
8. **Run the Pipeline:**
   * Click on Run to execute the pipeline for the first time.

***

### **Step 4: Rename the CI Pipeline**

Rename the CI pipeline on the Azure DevOps dashboard to “LibraryManagementSystem-CI.”

***

### **Step 5: Sync Branches**

1. **Create a Pull Request to Merge `dev` into `staging`:**
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
2. **Pull `staging` Locally:**

```bash
git checkout staging
git pull origin staging
```

3. **Create a Pull Request to Merge `staging` into `master`:**
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
4. **Pull `master` Locally:**

```bash
git checkout master
git pull origin master
```

***

### **Step 6: Making a Minor Change for Testing**

1.  **Create a New Branch for a Minor Change:**

    Create and switch to a new branch, e.g., `feature/test-sync`:

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/test-sync
    ```
2.  **Make a Minor Change:**

    Open any file (e.g., `pom.xml`) and make a minor change such as adding a comment.
3.  **Commit and Push the Change:**

    Commit the minor change to the new branch:

    ```bash
    git add .
    git commit -m "Test sync branches by adding a comment"
    git push origin feature/test-sync
    ```
4.  **Create PR to Merge `feature/test-sync` into `dev`:**

    * Create a PR from the `feature/test-sync` branch to the `dev` branch.
    * Title: "Test Sync Branches"
    * Description: "This PR tests the syncing of branches by adding a minor change (comment) to the pom.xml file."
    * Once reviewed and approved, merge the PR using MERGE. The CI pipeline will be triggered.
    * Pull the latest changes from `dev` locally:

    ```bash
    git checkout dev
    git pull origin dev
    ```
5.  **Sync `dev` with `staging` and `master` via PRs:**

    * Create a PR from `dev` to `staging` and merge after approval using REBASE AND FF.
      * Title: "Sync dev to staging"
      * Description: "This PR syncs the latest changes from dev to staging."
    * Pull the latest changes from `staging` locally:

    ```bash
    git checkout staging
    git pull origin staging
    ```

    * Create a PR from `staging` to `master` and merge after approval using REBASE AND FF.
      * Title: "Sync staging to master"
      * Description: "This PR syncs the latest changes from staging to master."
    * Pull the latest changes from `master` locally:

    ```bash
    git checkout master
    git pull origin master
    ```
6.  **Delete Local Feature Branches:**

    After successful merges, delete the feature branches locally:

    ```bash
    git branch -D feature/test-sync
    ```

***

### **Conclusion**

By following this live demo, you have set up a Continuous Integration (CI) pipeline that automates the build process for your project. This pipeline ensures that every code change is compiled and tested, providing immediate feedback to developers. The initial setup forms a strong foundation for more advanced CI/CD practices, enabling rapid and reliable software development and deployment. As you continue to refine your CI pipeline, remember to incorporate best practices and leverage the powerful features of your CI/CD tools to enhance your workflow efficiency and code quality.
