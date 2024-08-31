---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1434030216411-0b793f4b4173?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw1fHx3b3JrfGVufDB8fHx8MTcyMzU1NDY3NXww&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Live Demo - Implementing SAST and SCA

### **Introduction**

In this live demo, you'll integrate Static Application Security Testing (SAST) using SonarCloud and Software Composition Analysis (SCA) using Snyk into your Continuous Integration (CI) pipeline. These tools help ensure that your code is not only functionally correct but also secure and compliant with best practices. By the end of this demo, you will have a fully automated process for detecting code quality issues and security vulnerabilities in both your source code and third-party dependencies.

***

### **Part 1: Introduction to Static Application Security Testing (SAST) using SonarCloud**

Static Application Security Testing (SAST) is a crucial part of maintaining secure and high-quality code. In this section, you'll set up SonarCloud to continuously analyze your codebase for potential vulnerabilities and code quality issues. Integrating SonarCloud into your CI pipeline ensures that every code change is automatically scanned, providing real-time feedback to developers and helping to prevent security issues before they reach production.

***

### **Step 1: Set Up a SonarCloud Account and Link with Azure DevOps**

1. **Sign Up for SonarCloud Using Azure DevOps:**
   * Go to [SonarCloud](https://sonarcloud.io/).
   * Click on **Sign Up** and choose **Azure DevOps** as the sign-up method.
   * Authorize SonarCloud to access your Azure DevOps account.
2. The instructor will add the participants to the organization in SonarCloud to be able to import their projects.
3. **Import a Project in SonarCloud:**
   * In SonarCloud, go to the **Projects** tab and click **+** to create a new project.
   * Select your Azure DevOps organization and the repository you want to analyze.
   * Choose “Number of Days” and set it to 1.
   * SonarCloud will provide a unique project key and organization key.
4. **Generate a Token in SonarCloud:**
   * Navigate to **My Account** -> **Security** -> **Generate Tokens**.
   * Generate a new token and save it securely for CI pipeline authentication.

***

### **Step 2: Set Up SonarCloud in Azure DevOps**

1. **Add SonarCloud from Azure DevOps Marketplace:**
   * In Azure DevOps, navigate to **Project Settings** -> **Extensions** -> **Browse marketplace**.
   * Search for **SonarCloud** and add it to your Azure DevOps organization.
2. **Create a SonarCloud Service Connection:**
   * Go to **Project Settings** -> **Service connections**.
   * Click **+ New service connection** and select **SonarCloud**.
   * Fill in the required details, including the SonarCloud organization key and token.
   * Name it **SonarCloud Service Connection** and save.
3. **Store SonarCloud Token in Azure DevOps:**
   * Navigate to **Pipelines** -> **Library** in Azure DevOps.
   * Create a new variable group named **SonarCloudSettings** or use an existing group.
   * Add the following variables:
     * `SONAR_ORG`: Your SonarCloud organization key.
     * `SONAR_PROJECT_KEY`: The unique key for your project in SonarCloud.

***

### **Step 3: Create a Custom Quality Gate in SonarCloud**

Before running your SonarCloud analysis, it’s important to configure the quality gate criteria. A quality gate in SonarCloud is a set of conditions that your code must meet to pass the analysis. For testing purposes, you may want to create a custom quality gate with less stringent conditions and assign it to your project.

#### **Steps to Create and Assign a Custom Quality Gate:**

1. **Create a New Quality Gate:**
   * Navigate to the **Organization Settings** in your SonarCloud account.
   * Click on **Quality Gates** in the menu.
   * Click on **Create** to create a new quality gate.
   * Name your quality gate (e.g., "Test Quality Gate").
2. **Set Conditions for the Quality Gate:**
   * Add conditions for metrics such as code coverage, duplication, and maintainability.
   * For testing purposes, you can set these conditions to lower values (e.g., minimum code coverage of 20%, maximum duplication of 10%, etc.).
   * Save the quality gate after configuring the conditions.
3. **Assign the Quality Gate to Your Project:**
   * Go to the **Projects** tab in SonarCloud.
   * Select your project and navigate to the **Administration** tab.
   * Under **Quality Gate**, select the newly created quality gate (e.g., "Test Quality Gate").
   * Save the changes to assign this quality gate to your project.

***

### **Step 4: Create a Feature Branch for SonarCloud Integration**

1.  **Switch to the `dev` branch and pull the latest changes:**

    ```bash
    git checkout dev
    git pull origin dev
    ```
2.  **Create a new branch for the SonarCloud integration:**

    ```bash
    git checkout -b feature/add-sonarcloud
    ```

***

### **Step 5: Update `ci-pipeline.yml` to Include SonarCloud Analysis**

1.  **Add JaCoCo Plugin to `pom.xml`:**

    Add the following plugin configuration under the `<build>` section of your `pom.xml`:

    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.8</version> <!-- Use the latest version -->
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```
2.  **Update the YAML Configuration to Include SonarCloud Analysis:**

    ```yaml
    # ci-pipeline.yml

    # Trigger the pipeline on changes to the specified branches
    trigger:
      branches:
        include:
          - dev # Trigger pipeline on changes to the 'dev' branch
          - staging # Trigger pipeline on changes to the 'staging' branch
          - master # Trigger pipeline on changes to the 'master' (production) branch

    # Specify the virtual machine image for the build agent
    pool:
      vmImage: "ubuntu-latest" # Use the latest Ubuntu image for the build agent

    # Define variables for the pipeline, including Cloudsmith and SonarCloud settings
    variables:
      - group: CloudsmithSettings # Group containing settings for Cloudsmith, such as API keys
      - group: SonarCloudSettings # Group containing settings for SonarCloud, such as organization and project keys

    # Define the jobs to be executed in the pipeline
    jobs:
      - job: Build
        displayName: "Build, Test, and Analyze"
        steps:

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

          # Prepare SonarCloud Analysis
          - task: SonarCloudPrepare@2
            inputs:
              SonarCloud: "SonarCloud Service Connection" # Service connection for SonarCloud
              organization: $(SONAR_ORG) # SonarCloud organization key
              scannerMode: "CLI" # Use CLI mode for the Sonar scanner
              configMode: "manual" # Manually configure SonarCloud settings
              cliProjectKey: $(SONAR_PROJECT_KEY) # Project key in SonarCloud
              cliProjectName: "LibraryManagementSystem" # Project name in SonarCloud
              cliSources: "src" # Source directory for Sonar analysis
              extraProperties: |
                sonar.java.binaries=target/classes
            displayName: "Prepare SonarCloud Analysis"

          # Install Java 17
          - task: JavaToolInstaller@0
            inputs:
              versionSpec: "17" # Specify Java version 17
              jdkArchitectureOption: "x64" # Use 64-bit architecture
              jdkSourceOption: "PreInstalled" # Use pre-installed JDK if available
            displayName: "Install Java 17"

          # Verify Java Installation
          - script: |
              echo "JAVA_HOME environment variable: $JAVA_HOME"
              java -version
            displayName: "Verify Java Installation" # Output JAVA_HOME and Java version to verify installation

          # Compile Project
          - task: Maven@3
            inputs:
              mavenVersionOption: "Default" # Use the default Maven version
              mavenPomFile: "pom.xml" # Path to the Maven POM file
              goals: "clean compile" # Goals to clean and compile the project
            displayName: "Compile Project"

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

          # Package Project
          - task: Maven@3
            inputs:
              mavenVersionOption: 'Default'
              mavenPomFile: 'pom.xml'
              goals: 'package -DskipTests'                  # Package the project, skipping tests
              publishJUnitResults: false
            displayName: 'Package Project'

          # Publish Code Coverage Results
          - task: PublishCodeCoverageResults@2
            inputs:
              codeCoverageTool: "JaCoCo" # Code coverage tool (JaCoCo)
              summaryFileLocation: "target/site/jacoco/jacoco.xml" # Path to the summary file
              reportDirectory: "target/site/jacoco" # Directory containing the report
            displayName: "Publish Code Coverage Results"

          # Run SonarCloud Analysis
          - task: SonarCloudAnalyze@2
            displayName: "Run SonarCloud Analysis" # Execute the SonarCloud analysis

          # Publish Quality Gate Results
          - task: SonarCloudPublish@2
            inputs:
              pollingTimeoutSec: "300" # Set polling timeout for quality gate results
            displayName: "Publish Quality Gate Results"

          # Install Cloudsmith CLI
          - script: |
              pip install --upgrade cloudsmith-cli
            displayName: "Install Cloudsmith CLI" # Install the Cloudsmith CLI tool

          # Test Cloudsmith Login
          - script: |
              cloudsmith whoami -k $(CLOUDSMITH_API_KEY)
            displayName: "Test Cloudsmith Login" # Test the Cloudsmith login with the API key

          # Publish Artifacts to Cloudsmith
          - script: |
              cloudsmith push raw --republish $(ORG)/$(Build.SourceBranchName)-repo target/LibraryManagementSystem-0.0.1-SNAPSHOT.jar -k $(CLOUDSMITH_API_KEY)
            displayName: "Publish Artifacts to Cloudsmith" # Upload the built artifacts to Cloudsmith
    ```
3.  **Commit and Push the Updated YAML File:**

    Ensure you are on the `feature/add-sonarcloud` branch:

    ```bash
    git add .
    git commit -m "Add SonarCloud integration for SAST"
    git push origin feature/add-sonarcloud
    ```

***

### **Step 6: Create a Pull Request**

1. **Navigate to Azure DevOps:**
   * Go to **Repos** -> **Pull Requests**.
2. **Create a New Pull Request:**
   * **Source branch:** `feature/add-sonarcloud`
   * **Target branch:** `dev`
   * **Title:** "SonarCloud Integration"
   * **Description:** "This PR is for the integration of SonarCloud."
3. **Assign Reviewers:** (Optional)
   * Add team members as reviewers if needed.
4. **Complete Code Review:**
   * Once the review is completed, approve and merge the PR into the `dev` branch using MERGE NO FF.

***

### **Step 7: Update Detection of Long-Lived Branches**

1. **Update the Detection of Long-Lived Branches Regex:**
   * Go to your SonarCloud project dashboard.
   * Navigate to the **Branches** tab.
   * On the top right of the screen, find the **Long-lived branches pattern** option and edit.
   * Set the regex to `"^(master|dev|staging|release.*)$"` to classify `dev` and `staging` as long-lived branches.
   * Save the changes.
   * Delete the `Dev` branch on SonarCloud.

***

### **Step 8: Sync Branches and Verify SonarCloud Integration**

**Objective:** Ensure that all branches (`dev`, `staging`, and `master`) are in sync with the latest changes and that SonarCloud analysis results are consistently reflected across environments.

1. **Sync `dev` with `staging`:**
   * Create a PR from `dev` to `staging` and complete the review process using REBASE AND FF.
2. **Sync `staging` with `master`:**
   * Create a PR from `staging` to `master` and complete the review process using REBASE AND FF.
3. **Verify SonarCloud Analysis:**
   * Go to your SonarCloud project dashboard and verify that the analysis results appear for all branches.

***

### **Step 9: Monitoring and Addressing Issues (Best Practices)**

1. **Monitor Code Quality and Security Issues:**
   * Regularly monitor the SonarCloud dashboard for new issues, vulnerabilities, and code smells.
2. **Address Issues Promptly:**
   * Create tasks or work items in Azure DevOps to address issues reported by SonarCloud.
   * Ensure all issues are resolved before merging changes into the `master` branch.

***

### **Step 10: Testing SonarCloud Integration**

**Objective:** Ensure successful integration by making a minor change to the codebase, triggering the pipeline, and verifying the analysis results on SonarCloud.

1.  **Create a Feature Branch for Testing:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/test-sonarcloud-integration
    ```
2.  **Make a Minor Change:**

    Add a comment or minor change:

    ```xml
    <!-- This is a test comment to trigger SonarCloud analysis -->
    ```
3.  **Commit and Push the Changes:**

    ```bash
    git add .
    git commit -m "Test SonarCloud integration with a minor change"
    git push origin feature/test-sonarcloud-integration
    ```
4. **Create a Pull Request:**
   * Create a PR from `feature/test-sonarcloud-integration` to `dev`.
5. **Verify SonarCloud Analysis:**
   * Check the pipeline and verify the analysis on SonarCloud.
6. **Sync Branches:**
   * Sync `dev`, `staging`, and `master` branches following the same PR review process as before.
7. **Delete Local Feature Branch:**
   * Delete the `feature/test-sonarcloud-integration` branch locally.

***

### **Part 2: Introduction to Software Composition Analysis (SCA) using Snyk**

Software Composition Analysis (SCA) is an essential practice for identifying and managing security vulnerabilities in open-source components used in your project. This section will guide you through the integration of Snyk into your CI pipeline. By the end of this section, you will have a system that automatically scans your project’s dependencies for known vulnerabilities, helping you to ensure the security and compliance of your software.

***

### **Step 1: Set Up a Snyk Account**

1. **Sign Up for Snyk:**
   * Go to the [Snyk website](https://snyk.io/).
   * Sign up using your preferred method (GitHub, GitLab, Bitbucket, Azure AD, or email).

***

### **Step 2: Integrate Snyk with Azure DevOps**

1. **Install Snyk Extension:**
   * Install the **Snyk Security** extension from the Azure DevOps Marketplace.
2. **Generate Snyk API Token:**
   * Generate an API token from your Snyk account and save it securely.
3. **Configure Snyk in Azure DevOps:**
   * Create a new service connection in Azure DevOps, named **Snyk Service Connection**, and connect it with the API token.

***

### **Step 3: Create a Feature Branch for Snyk Integration**

1.  **Switch to the `dev` branch and pull the latest changes:**

    ```bash
    git checkout dev
    git pull origin dev
    ```
2.  **Create a new branch for the Snyk integration:**

    ```bash
    git checkout -b feature/add-snyk
    ```

***

#### **Step 4: Update `ci-pipeline.yml` to Include Snyk Analysis**

1.  **Update the YAML Configuration to Include Snyk Analysis:**

    ```yaml
    # ci-pipeline.yml

    # Trigger the pipeline on changes to the specified branches
    trigger:
      branches:
        include:
          - dev # Trigger pipeline on changes to the 'dev' branch
          - staging # Trigger pipeline on changes to the 'staging' branch
          - master # Trigger pipeline on changes to the 'master' (production) branch

    # Specify the virtual machine image for the build agent
    pool:
      vmImage: "ubuntu-latest" # Use the latest Ubuntu image for the build agent

    # Define variables for the pipeline, including Cloudsmith and SonarCloud settings
    variables:
      - group: CloudsmithSettings # Group containing settings for Cloudsmith, such as API keys
      - group: SonarCloudSettings # Group containing settings for SonarCloud, such as organization and project keys

    # Define the jobs to be executed in the pipeline
    jobs:
      - job: Build
        displayName: "Build, Test, and Analyze"
        steps:

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

          # Prepare SonarCloud Analysis
          - task: SonarCloudPrepare@2
            inputs:
              SonarCloud: "SonarCloud Service Connection" # Service connection for SonarCloud
              organization: $(SONAR_ORG) # SonarCloud organization key
              scannerMode: "CLI" # Use CLI mode for the Sonar scanner
              configMode: "manual" # Manually configure SonarCloud settings
              cliProjectKey: $(SONAR_PROJECT_KEY) # Project key in SonarCloud
              cliProjectName: "LibraryManagementSystem" # Project name in SonarCloud
              cliSources: "src" # Source directory for Sonar analysis
              extraProperties: |
                sonar.java.binaries=target/classes
            displayName: "Prepare SonarCloud Analysis"

          # Install Java 17
          - task: JavaToolInstaller@0
            inputs:
              versionSpec: "17" # Specify Java version 17
              jdkArchitectureOption: "x64" # Use 64-bit architecture
              jdkSourceOption: "PreInstalled" # Use pre-installed JDK if available
            displayName: "Install Java 17"

          # Verify Java Installation
          - script: |
              echo "JAVA_HOME environment variable: $JAVA_HOME"
              java -version
            displayName: "Verify Java Installation" # Output JAVA_HOME and Java version to verify installation

          # Compile Project
          - task: Maven@3
            inputs:
              mavenVersionOption: "Default" # Use the default Maven version
              mavenPomFile: "pom.xml" # Path to the Maven POM file
              goals: "clean compile" # Goals to clean and compile the project
            displayName: "Compile Project"

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

          # Package Project
          - task: Maven@3
            inputs:
              mavenVersionOption: 'Default'
              mavenPomFile: 'pom.xml'
              goals: 'package -DskipTests'                  # Package the project, skipping tests
              publishJUnitResults: false
            displayName: 'Package Project'

          # Use Node.js
          - task: UseNode@1
            inputs:
              versionSpec: "14.x" # Specify Node.js version 14.x
            displayName: "Use Node.js"

          # Grant execute permissions to the Maven wrapper
          - script: chmod +x mvnw
            displayName: "Grant execute permissions to mvnw" # Ensure the Maven wrapper is executable

          # Run Snyk Security Analysis
          - task: SnykSecurityScan@1
            inputs:
              serviceConnectionEndpoint: "Snyk Service Connection" # Service connection for Snyk
              testType: "app" # Type of analysis (application)
              monitorWhen: "always" # Monitor the project for issues always
              projectName: "LibraryManagementSystem-$(Build.SourceBranchName)" # Project name with branch context
              failOnIssues: "true" # Fail the build if issues are found
            displayName: "Run Snyk Security Analysis"

          # Publish Code Coverage Results
          - task: PublishCodeCoverageResults@2
            inputs:
              codeCoverageTool: "JaCoCo" # Code coverage tool (JaCoCo)
              summaryFileLocation: "target/site/jacoco/jacoco.xml" # Path to the summary file
              reportDirectory: "target/site/jacoco" # Directory containing the report
            displayName: "Publish Code Coverage Results"

          # Run SonarCloud Analysis
          - task: SonarCloudAnalyze@2
            displayName: "Run SonarCloud Analysis" # Execute the SonarCloud analysis

          # Publish Quality Gate Results
          - task: SonarCloudPublish@2
            inputs:
              pollingTimeoutSec: "300" # Set polling timeout for quality gate results
            displayName: "Publish Quality Gate Results"

          # Install Cloudsmith CLI
          - script: |
              pip install --upgrade cloudsmith-cli
            displayName: "Install Cloudsmith CLI" # Install the Cloudsmith CLI tool

          # Test Cloudsmith Login
          - script: |
              cloudsmith whoami -k $(CLOUDSMITH_API_KEY)
            displayName: "Test Cloudsmith Login" # Test the Cloudsmith login with the API key

          # Publish Artifacts to Cloudsmith
          - script: |
              cloudsmith push raw --republish $(ORG)/$(Build.SourceBranchName)-repo target/LibraryManagementSystem-0.0.1-SNAPSHOT.jar -k $(CLOUDSMITH_API_KEY)
            displayName: "Publish Artifacts to Cloudsmith" # Upload the built artifacts to Cloudsmith
    ```
2.  **Add a Vulnerable Dependency for Testing:**

    ```xml
    <dependency>
        <groupId>commons-collections</groupId>
        <artifactId>commons-collections</artifactId>
        <version>3.2.1</version> <!-- Known vulnerable version -->
    </dependency>
    ```
3.  **Commit and Push the Updated YAML File:**

    Ensure you are on the `feature/add-snyk` branch:

    ```bash
    git add .
    git commit -m "Add Snyk integration for SCA"
    git push origin feature/add-snyk
    ```

***

### **Step 5: Create a Pull Request**

1. **Create a New Pull Request:**
   * Create a PR from `feature/add-snyk` to `dev`.
2. **Handle Pipeline Failures:**
   * The pipeline will fail due to the vulnerable dependency. Modify the Snyk configuration to not fail on issues.
3.  Use the previous feature branch, still available locally, update the YAML file.

    ```yaml
    - task: SnykSecurityScan@1
      inputs:
        serviceConnectionEndpoint: 'Snyk Service Connection'
        testType: 'app'
        monitorWhen: 'always'
        projectName: 'LibraryManagementSystem-$(Build.SourceBranchName)'
        failOnIssues: 'false'
      displayName: 'Run Snyk Security Analysis'
    ```
4.  **Commit and Push the Updated YAML File:**

    ```bash
    git add .
    git commit -m "Update Snyk configuration to prevent failure on issues"
    git push origin feature/add-snyk
    ```

***

### **Step 6: Sync Branches and Verify Snyk Integration**

1. **Sync `dev` with `staging`:**
   * Create a PR from `dev` to `staging` and complete the review using REBASE AND FF.
2. **Sync `staging` with `master`:**
   * Create a PR from `staging` to `master` and complete the review using REBASE AND FF.
3. **Verify Snyk Analysis:**
   * Go to your Snyk dashboard and verify that the analysis results appear for the branches.

***

### **Step 7: Monitoring and Addressing Issues**

1. **Monitor Security Vulnerabilities and License Compliance:**
   * Regularly monitor the Snyk dashboard for new vulnerabilities and license compliance issues.
   * Set up notifications in Snyk to receive alerts about new vulnerabilities or compliance issues.
2. **Address Issues Promptly:**
   * Create tasks or work items in Azure DevOps to address issues reported by Snyk.
   * Ensure all vulnerabilities and compliance issues are resolved before merging changes into the `master` branch.

***

### **Step 8: Testing Snyk Integration**

**Objective:** Ensure successful integration by making a minor change to the codebase, triggering the pipeline, and verifying the analysis results on Snyk.

1.  **Create a Feature Branch for Testing:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/test-snyk-integration
    ```
2.  **Make a Minor Change:**

    Add a comment or minor change:

    ```xml
    <!-- This is a test comment to trigger Snyk analysis -->
    ```
3.  **Remove Vulnerable Dependency and Update Pipeline Configuration:**

    Remove the vulnerable dependency and revert the Snyk configuration to fail on issues:

    ```yaml
    - task: SnykSecurityScan@1
      inputs:
        serviceConnectionEndpoint: 'Snyk Service Connection'
        testType: 'app'
        monitorWhen: 'always'
        projectName: 'LibraryManagementSystem-$(Build.SourceBranchName)'
        failOnIssues: 'true'
      displayName: 'Run Snyk Security Analysis'
    ```

    Commit and push the changes:

    ```bash
    git add .
    git commit -m "Revert Snyk configuration to fail on issues"
    git push origin feature/test-snyk-integration
    ```
4. **Create a Pull Request:**
   * Create a PR from `feature/test-snyk-integration` to `dev`.
5. **Verify Snyk Analysis:**
   * Check the pipeline and verify the analysis on Snyk.
6. **Sync Branches:**
   * Sync `dev`, `staging`, and `master` branches following the same PR review process as before.
7. **Delete Local Feature Branch:**
   * Delete the `feature/test-snyk-integration` branch locally.

***

### **Conclusion**

By integrating both Static Application Security Testing (SAST) with SonarCloud and Software Composition Analysis (SCA) with Snyk into your CI pipeline, you’ve built a robust and automated security process. These integrations will continuously monitor your code and dependencies for vulnerabilities and quality issues, allowing your team to address them proactively. As you continue to refine these processes, you’ll enhance your organization’s ability to deliver secure, high-quality software with greater confidence and efficiency.
