---
icon: flask
cover: >-
  https://images.unsplash.com/photo-1519389950473-47ba0277781c?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwxMHx8d29yayUyMHNob3B8ZW58MHx8fHwxNzI0NTM3MTg2fDA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Workshop Activity 2

### **Introduction**

In this workshop activity, you will apply what you’ve learned to optimize a CI pipeline. The focus will be on improving build times by implementing caching, ensuring clear test reporting by splitting unit and integration tests, and maintaining a clean pipeline by avoiding unnecessary result publishing. By following the steps provided, you will gain hands-on experience in enhancing a CI pipeline, making it more efficient and effective for real-world scenarios.

This activity is designed to be a practical exercise where you’ll work through each step independently, solidifying your understanding of the concepts covered in the live demo.

***

### **Step 1: Create a Feature Branch**

We’ll start by creating a new feature branch where we’ll make all the necessary changes.

1.  **Switch to the `dev` branch** to ensure you’re starting from the latest version:

    ```bash
    git checkout dev
    git pull origin dev
    ```
2.  **Create a new feature branch** to work on the pipeline improvements:

    ```bash
    git checkout -b feature/optimize-ci-pipeline
    ```

    > **Explanation:** By creating a new feature branch, you isolate your changes from the main codebase. This makes it easier to review and test your changes before they’re merged.

***

### **Step 2: Update `pom.xml` for Split Test Execution**

**2.1 Adding Maven Surefire Plugin Configuration**

We’ll configure the `maven-surefire-plugin` to split the execution of unit and integration tests. This ensures that separate reports are generated for each test type, making it easier to identify issues.

1. **Locate the `pom.xml` file** in the root directory of your project. You can use your code editor’s file explorer to find and open it.
2.  **Add the Maven Surefire Plugin configuration** by pasting the following code inside the `<plugins>` section of your `pom.xml` file:

    ```xml
    <plugin>
    	<groupId>org.apache.maven.plugins</groupId>
    	<artifactId>maven-surefire-plugin</artifactId>
    	<version>3.0.0-M5</version>
    	<configuration>
    		<!-- Custom directory for test reports -->
    		<reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
    		<!-- Use the 'groups' parameter directly for the report name suffix -->
    		<reportNameSuffix>${testType}</reportNameSuffix>
    	</configuration>
    </plugin>
    ```

    > **Explanation:** The `<executions>` section allows us to define multiple executions within the Maven Surefire Plugin. We’ve created two executions: one for unit tests and another for integration tests. Each execution is tagged with a `reportNameSuffix`, ensuring that the test results are stored separately.
3. **Save the changes** to the `pom.xml` file.

***

### **Step 3: Implement Caching in the CI Pipeline**

**3.1 Update the Pipeline YAML File**

Now, let’s update the CI pipeline to implement caching for Maven dependencies, which will help speed up the build process.

1. **Locate the `ci-pipeline.yml` file** in your project’s root directory and open it in your code editor.
2.  **Add the following steps** before the `Install Java 17` step:

    ```yaml
    # Ensure Maven Repository Directory Exists
    - script: mkdir -p $(HOME)/.m2/repository
      displayName: "Ensure Maven Repository Directory Exists"

    # Cache Maven Dependencies
    - task: Cache@2
      inputs:
        key: 'maven | "$(Agent.OS)" | **/pom.xml'
        path: $(HOME)/.m2/repository
        restoreKeys: |
          maven | "$(Agent.OS)"
      displayName: "Cache Maven Dependencies"
    ```

    > **Explanation:** The `mkdir` command ensures that the directory where Maven stores its dependencies exists. The `Cache` task then caches these dependencies based on the operating system and `pom.xml` file. This means that on subsequent builds, Maven will reuse cached dependencies, significantly reducing build time.
3. **Save the changes** to the `ci-pipeline.yml` file.

***

### **Step 4: Split Test Results in the CI Pipeline**

**4.1 Modify the YAML File for Separate Test Executions**

Next, we’ll configure the CI pipeline to run unit and integration tests separately and publish their results.

1. **In the same `ci-pipeline.yml` file,** locate the section where tests are currently being run. This is typically within a `Maven@3` task.
2.  **Replace the existing test execution configuration** with the following code:

    ```yaml
    # Run Unit Tests and Publish Results
    - task: Maven@3
      inputs:
        mavenVersionOption: 'Default'
        mavenPomFile: 'pom.xml'
        goals: 'test'
        options: '-Dgroups=unit -DtestType=unit'
        testResultsFiles: '**/target/surefire-reports/*unit*.xml'
        testRunTitle: 'Unit Tests'
      displayName: 'Run and Publish Unit Tests'

    # Run Integration Tests and Publish Results
    - task: Maven@3
      inputs:
        mavenVersionOption: 'Default'
        mavenPomFile: 'pom.xml'
        goals: 'test'
        options: '-Dgroups=integration -DtestType=integration'
        testResultsFiles: '**/target/surefire-reports/*integration*.xml'
        testRunTitle: 'Integration Tests'
      displayName: 'Run and Publish Integration Tests'
    ```

    > **Explanation:** Here, we’ve set up two separate tasks for running and publishing unit and integration tests. Each task runs the tests tagged with the appropriate group (`unit` or `integration`) and publishes the results under a specific title. The `testResultsFiles` parameter ensures that only the relevant test results are collected and published.
3. **Save the changes** to the `ci-pipeline.yml` file.

***

### **Step 5: Avoid Unnecessary Result Publishing**

**5.1 Modify the Packaging Step**

Finally, we’ll ensure that unnecessary test results are not published during the packaging phase.

1. **Locate the packaging step** in the `ci-pipeline.yml` file, which is usually a `Maven@3` task towards the end of the file.
2.  **Add the following configuration** to the packaging step:

    ```yaml
    # Package Project
    - task: Maven@3
      inputs:
        mavenVersionOption: 'Default'
        mavenPomFile: 'pom.xml'
        goals: 'package -DskipTests'
        publishJUnitResults: false
      displayName: 'Package Project'
    ```

    > **Explanation:** By adding `publishJUnitResults: false`, we prevent Maven from publishing any test results during the packaging phase. This ensures that only relevant test results are published, keeping your reports clean and focused.
3. **Save the changes** to the `ci-pipeline.yml` file.

***

### **Step 6: Commit and Push All Changes**

Now that we’ve made all the necessary changes, it’s time to commit and push them to the remote repository.

1.  **Stage all the changes** you’ve made:

    ```bash
    git add pom.xml ci-pipeline.yml
    ```
2.  **Commit the changes** with a clear message:

    ```bash
    git commit -m "Enhance CI pipeline with caching, split test execution, and optimized result publishing"
    ```
3.  **Push the changes** to your feature branch:

    ```bash
    git push origin feature/optimize-ci-pipeline
    ```

    > **Explanation:** By pushing all the changes at once, you keep your commit history clean and make it easier for others to review your work.

***

### **Step 7: Sync Branches**

1. **Create a Pull Request to Merge** `feature/optimize-ci-pipeline` **into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/optimize-ci-pipeline`
     * Target branch: `dev`
     * Title: "Optimize CI Pipeline"
     * Description: "This PR implements caching, splits test execution, and avoids unnecessary result publishing."
   * **Assign Reviewers:** Add team members as reviewers.
   * **Merge the PR:** Once the code is approved, merge the pull request **using MERGE**.
2. **Sync `dev` with `staging`:**
   * **Create a New Pull Request:**
     * Source branch: `dev`
     * Target branch: `staging`
     * Title: "Sync dev to staging"
     * Description: "This PR syncs the latest changes from dev to staging."
   * **Merge the PR:** Once approved, merge the pull request **using REBASE AND FF**.
3. **Sync `staging` with `master`:**
   * **Create a New Pull Request:**
     * Source branch: `staging`
     * Target branch: `master`
     * Title: "Sync staging to master"
     * Description: "This PR syncs the latest changes from staging to master."
   * **Merge the PR:** Once approved, merge the pull request **using REBASE AND FF**.
4.  **Delete the Feature Branch Locally:**

    ```bash
    git checkout dev
    git branch -D feature/optimize-ci-pipeline
    ```
5.  **Pull the Latest Changes Locally:**

    After syncing all branches, make sure to pull the latest changes to your local repository:

    ```bash
    git checkout dev
    git pull origin dev

    git checkout staging
    git pull origin staging

    git checkout master
    git pull origin master
    ```

    > **Note:** Pulling the latest changes locally ensures that your local branches are up-to-date with the remote repository.

***

### **Step 8: Test the Setup**

1.  **Create a Minor Change for Testing:**

    ```bash
    git checkout -b feature/test-optimized-pipeline
    # Make a small change, e.g., add a comment to pom.xml
    git add .
    git commit -m "Test optimized CI pipeline"
    git push origin feature/test-optimized-pipeline
    ```
2.  **Create a Pull Request and Sync the Branches:**

    * Create a PR from `feature/test-optimized-pipeline` to `dev`, and merge it **using MERGE**.
    * Follow the same steps to sync `dev` to `staging` and `staging` to `master`.
    * Delete the local feature branch after syncing:

    ```bash
    git checkout dev
    git branch -D feature/test-optimized-pipeline
    ```
3.  **Pull the Latest Changes Locally:**

    After syncing all branches, pull the latest changes to ensure your local repository is up-to-date:

    ```bash
    git checkout dev
    git pull origin dev

    git checkout staging
    git pull origin staging

    git checkout master
    git pull origin master
    ```

    > **Note:** This step ensures that your local environment reflects the latest changes from all synced branches.

***

### **Conclusion**

Congratulations on completing the workshop activity! You’ve successfully enhanced your CI pipeline, gaining valuable experience in implementing caching, organizing test executions, and optimizing result publishing. These improvements will help ensure that your CI process is both faster and more reliable.

Take a moment to reflect on the changes you made and consider how these techniques can be applied to other pipelines in your projects. The skills you’ve practiced today are essential for maintaining high-quality and efficient CI/CD pipelines in any development environment.
