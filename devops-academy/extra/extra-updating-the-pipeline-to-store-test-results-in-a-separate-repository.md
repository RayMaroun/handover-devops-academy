---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1720983590448-28b749bd403d?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MjM0MTI0OTV8&ixlib=rb-4.0.3&q=85
coverY: -35
---

# Extra - Updating the Pipeline to Store Test Results in a Separate Repository

### **Introduction**

In this live demo, we will update your existing CI pipeline to store test results in a separate repository. This approach ensures that test results are preserved even if pipeline runs are deleted, allowing you to maintain a history of them. We will also update any paths and configurations necessary to accommodate these changes, ensuring the pipeline operates smoothly.

By the end of this demo, you will have an updated pipeline that archives test results in a dedicated repository, ensuring long-term storage and traceability.

***

### **Step 1: Prepare the Separate Repository for Test Results**

1. **Create a Separate Repository (If Not Already Created):**
   * Create a new repository in your Azure DevOps project named `LibraryManagementSystem-TestResults`.
   * Set the default branch to `main`.
2.  **Grant Security Access to the Build Service:**

    * **Navigate to Project Settings:**
      * Go to the Azure DevOps project where your pipeline is located.
    * **Select Security and Permissions:**
      * Under **Security**, select the repository `LibraryManagementSystem-TestResults`.
    * **Add the Build Service as a Contributor:**
      * Add `ProjectName Build Service` (replace `ProjectName` with your project’s name) and grant it permissions to **Contribute**, **Create branch**, and **Edit policies**.
    * **Save the Changes:**
      * Confirm and save the permission settings.

    > **Explanation:** Granting these permissions allows the pipeline to push changes, such as test results, to the separate repository.

***

### **Step 2: Update the Existing Pipeline**

**2.1 Modify the Pipeline to Include the Test Results Repository**

1.  **Open the Existing `ci-pipeline.yml` File:**

    Locate and open the `ci-pipeline.yml` file in your project. This file defines the CI pipeline configuration.
2.  **Add the `resources` Section:**

    At the top of the pipeline file, under the `trigger` section, add a `resources` block to define the separate repository where test results will be stored:

    ```yaml
    resources:
      repositories:
        - repository: testResultsRepo  # Alias for the repository
          type: git
          name: LibraryManagementSystem/LibraryManagementSystem-TestResults  # Name of the repository
          ref: main  # Branch you want to check out
    ```

    > **Explanation:** This section defines the external repository that the pipeline will use to store test results. The `repository` alias (`testResultsRepo`) will be referenced later in the pipeline.
3.  **Update Paths and Variables:**

    Ensure that paths in the pipeline are updated to accommodate changes for storing test results. Specifically:

    *   **Update the Maven `pom.xml` Paths:**

        ```yaml
        mavenPomFile: "$(Build.SourcesDirectory)/LibraryManagementSystem/pom.xml"  # Ensure the correct path
        ```

        > **Explanation:** The `mavenPomFile` path ensures that Maven commands reference the correct `pom.xml` file in the updated directory structure.
    *   **Add a Step to Check Out the Test Results Repository:**

        Scroll down to the section where jobs are defined and add a step to check out the `testResultsRepo` repository:

        ```yaml
        - checkout: testResultsRepo
          persistCredentials: true  # Ensure credentials are available for Git operations
        ```

        > **Explanation:** This step checks out the separate repository into the build environment so that the pipeline can push test results there.
    *   **Ensure Variables Capture the Correct Information:**

        Verify and update the script capturing additional information:

        ```yaml
        - script: |
            echo "##vso[task.setvariable variable=CommitHash]$(Build.SourceVersion)"
            echo "##vso[task.setvariable variable=BuildId]$(Build.BuildId)"
            echo "##vso[task.setvariable variable=BranchName]$(Build.SourceBranchName)"
            echo "##vso[task.setvariable variable=PipelineName]$(System.DefinitionName)"
            echo "##vso[task.setvariable variable=JobName]$(System.JobName)"
            echo "##vso[task.setvariable variable=Timestamp]$(date)"
          displayName: 'Capture Additional Information'
        ```

        > **Explanation:** These variables will be used later when archiving the test results, ensuring that all necessary metadata is correctly captured.

**2.2 Archive and Push Test Results**

4.  **Update Paths for Archiving Unit Test Results with Metadata:**

    Add the following script steps after the unit tests have been run and results have been published:

    ```yaml
    # Archive Unit Test Results with Metadata
    - script: |
        mkdir -p $(Build.ArtifactStagingDirectory)/TestResults/UnitTests
        cp -R $(Build.SourcesDirectory)/LibraryManagementSystem/target/surefire-reports/*unit*.xml $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/
        echo "Commit Hash: $(CommitHash)" > $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
        echo "Build ID: $(BuildId)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
        echo "Branch Name: $(BranchName)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
        echo "Pipeline Name: $(PipelineName)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
        echo "Job Name: $(JobName)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
        echo "Timestamp: $(Timestamp)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
      displayName: 'Archive Unit Test Results'
    ```

    > **Explanation:** This script archives the unit test results and metadata into a specific directory (`TestResults/UnitTests`). The metadata includes important information like the commit hash, build ID, and timestamp.
5.  **Update Paths for Archiving Integration Test Results with Metadata:**

    Similarly, add the following script steps after the integration tests have been run and results have been published:

    ```yaml
    # Archive Integration Test Results with Metadata
    - script: |
        mkdir -p $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests
        cp -R $(Build.SourcesDirectory)/LibraryManagementSystem/target/surefire-reports/*integration*.xml $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/
        echo "Commit Hash: $(CommitHash)" > $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
        echo "Build ID: $(BuildId)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
        echo "Branch Name: $(BranchName)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
        echo "Pipeline Name: $(PipelineName)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
        echo "Job Name: $(JobName)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
        echo "Timestamp: $(Timestamp)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
      displayName: 'Archive Integration Test Results'
    ```

    > **Explanation:** This script archives the integration test results and metadata into a directory (`TestResults/IntegrationTests`), following the same approach as for unit tests.
6.  **Ensure Correct Paths for Copying and Pushing Results to the Test Results Repository:**

    Add the following script steps to copy the archived test results to the `testResultsRepo` repository and push the changes:

    ```yaml
    # Copy Unit Test Results to the Test Results Repository
    - script: |
        mkdir -p $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/UnitTests
        cp -R $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/* $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/UnitTests/
      displayName: "Copy Unit Test Results to Repository"

    # Copy Integration Test Results to the Test Results Repository
    - script: |
        mkdir -p $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/IntegrationTests
        cp -R $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/* $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/IntegrationTests/
      displayName: "Copy Integration Test Results to Repository"

    # Commit and Push the Test Results to the Test Results Repository
    - script: |
        cd $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults
        git add .
        git commit -m "Test results for Build $(BuildId) - Commit $(CommitHash)"
        git push origin main
      displayName: "Commit and Push Test Results to Repository"
    ```

    > **Explanation:** These steps copy the archived test results to the test results repository under a directory named after the build ID. The changes are then committed and pushed to the repository, preserving the test results and associated metadata.

***

### **Step 3: Full Updated Pipeline**

```yaml
# ci-pipeline.yml

# Trigger the pipeline on changes to the specified branches
trigger:
  branches:
    include:
      - dev # Trigger pipeline on changes to the 'dev' branch
      - staging # Trigger pipeline on changes to the 'staging' branch
      - master # Trigger pipeline on changes to the 'master' (production) branch

# Specify the repository resources (this is where you define the additional repo)
resources:
  repositories:
    - repository: testResultsRepo  # Alias for the repository
      type: git
      name: LibraryManagementSystem/LibraryManagementSystem-TestResults  # Name of the repository
      ref: main  # Branch you want to check out

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

      # Step 1: Checkout the main repository (the code you are testing)
      - checkout: self
        persistCredentials: true  # Ensure credentials are available for Git operations

      # Ensure Maven Repository Directory Exists
      - script: mkdir -p $(HOME)/.m2/repository
        displayName: "Ensure Maven Repository Directory Exists"

      # Cache Maven Dependencies
      - task: Cache@2
        inputs:
          key: 'maven | "$(Agent.OS)" | $(Build.SourcesDirectory)/LibraryManagementSystem/pom.xml'
          path: $(HOME)/.m2/repository
          restoreKeys: |
            maven | "$(Agent.OS)"
        displayName: "Cache Maven Dependencies"

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

      # Capture Additional Information
      - script: |
          echo "##vso[task.setvariable variable=CommitHash]$(Build.SourceVersion)"
          echo "##vso[task.setvariable variable=BuildId]$(Build.BuildId)"
          echo "##vso[task.setvariable variable=BranchName]$(Build.SourceBranchName)"
          echo "##vso[task.setvariable variable=PipelineName]$(System.DefinitionName)"
          echo "##vso[task.setvariable variable=JobName]$(System.JobName)"
          echo "##vso[task.setvariable variable=Timestamp]$(date)"
        displayName: 'Capture Additional Information'

      # Compile Project
      - task: Maven@3
        inputs:
          mavenVersionOption: "Default" # Use the default Maven version
          mavenPomFile: "$(Build.SourcesDirectory)/LibraryManagementSystem/pom.xml"  # Update the path to the correct location
          goals: "clean compile" # Goals to clean and compile the project
        displayName: "Compile Project"

      # Run Unit Tests and Publish Results
      - task: Maven@3
        inputs:
          mavenVersionOption: 'Default'
          mavenPomFile: "$(Build.SourcesDirectory)/LibraryManagementSystem/pom.xml"
          goals: 'test'
          options: '-Dgroups=unit'
          testResultsFiles: '**/target/surefire-reports/*unit*.xml'
          testRunTitle: 'Unit Tests'
        displayName: 'Run and Publish Unit Tests'

      # Archive Unit Test Results with Metadata
      - script: |
          mkdir -p $(Build.ArtifactStagingDirectory)/TestResults/UnitTests
          cp -R $(Build.SourcesDirectory)/LibraryManagementSystem/target/surefire-reports/*unit*.xml $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/
          echo "Commit Hash: $(CommitHash)" > $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
          echo "Build ID: $(BuildId)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
          echo "Branch Name: $(BranchName)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
          echo "Pipeline Name: $(PipelineName)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
          echo "Job Name: $(JobName)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
          echo "Timestamp: $(Timestamp)" >> $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/metadata.txt
        displayName: 'Archive Unit Test Results'

      # Run Integration Tests and Publish Results
      - task: Maven@3
        inputs:
          mavenVersionOption: 'Default'
          mavenPomFile: "$(Build.SourcesDirectory)/LibraryManagementSystem/pom.xml"
          goals: 'test'
          options: '-Dgroups=integration'
          testResultsFiles: '**/target/surefire-reports/*integration*.xml'
          testRunTitle: 'Integration Tests'
        displayName: 'Run and Publish Integration Tests'

      # Archive Integration Test Results with Metadata
      - script: |
          mkdir -p $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests
          cp -R $(Build.SourcesDirectory)/LibraryManagementSystem/target/surefire-reports/*integration*.xml $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/
          echo "Commit Hash: $(CommitHash)" > $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
          echo "Build ID: $(BuildId)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
          echo "Branch Name: $(BranchName)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
          echo "Pipeline Name: $(PipelineName)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
          echo "Job Name: $(JobName)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
          echo "Timestamp: $(Timestamp)" >> $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/metadata.txt
        displayName: 'Archive Integration Test Results'

      # Package Project
      - task: Maven@3
        inputs:
          mavenVersionOption: "Default"
          mavenPomFile: "$(Build.SourcesDirectory)/LibraryManagementSystem/pom.xml"
          goals: "package -DskipTests" # Package the project, skipping tests
          publishJUnitResults: false
        displayName: "Package Project"

      # Step 10: Checkout the test results repository where you want to store test results
      - checkout: testResultsRepo
        persistCredentials: true  # Ensure credentials are available for Git operations

      # Change to the correct directory before setting Git user identity
      - script: |
          cd $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults
          git config user.email "your-email@example.com"
          git config user.name "Your Name"
        displayName: "Set Git User Identity"

      # Ensure the branch exists or create a new branch
      - script: |
          cd $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults
          git checkout -b main || git checkout main
        displayName: "Checkout or Create Branch"

      # Copy Unit Test Results to the Test Results Repository
      - script: |
          mkdir -p $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/UnitTests
          cp -R $(Build.ArtifactStagingDirectory)/TestResults/UnitTests/* $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/UnitTests/
        displayName: "Copy Unit Test Results to Repository"

      # Copy Integration Test Results to the Test Results Repository
      - script: |
          mkdir -p $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/IntegrationTests
          cp -R $(Build.ArtifactStagingDirectory)/TestResults/IntegrationTests/* $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults/$(BuildId)/IntegrationTests/
        displayName: "Copy Integration Test Results to Repository"

      # Commit and Push the Test Results to the Test Results Repository
      - script: |
          cd $(Build.SourcesDirectory)/LibraryManagementSystem-TestResults
          git add .
          git commit -m "Test results for Build $(BuildId) - Commit $(CommitHash)"
          git push origin main
        displayName: "Commit and Push Test Results to Repository"

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

***

### **Step 4: Commit and Push the Updated Pipeline**

1.  **Stage the Updated Pipeline File:**

    ```bash
    git add ci-pipeline.yml
    ```
2.  **Commit the Changes:**

    ```bash
    git commit -m "Update pipeline to store test results in a separate repository"
    ```
3.  **Push the Changes to the Remote Repository:**

    ```bash
    git push origin feature/store-test-results
    ```

***

### **Step 5: Create a Pull Request and Merge into `dev`**

1. **Create a Pull Request to Merge** `feature/store-test-results` **into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/store-test-results`
     * Target branch: `dev`
     * Title: "Store Test Results in Separate Repository"
     * Description: "This PR updates the pipeline to store test results in a separate repository for long-term storage."
   * **Assign Reviewers:** Add team members as reviewers.
   * **Merge the PR:** Once the code is approved, merge the pull request **using MERGE**.

***

### **Step 6: Sync Branches**

1. **Create a Pull Request to Merge** `dev` **into `staging`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `dev`
     * Target branch: `staging`
     * Title: "Sync dev to staging"
     * Description: "This PR syncs the latest changes from dev to staging."
   * **Merge the PR:** Once approved, merge the pull request **using REBASE AND FF**.
2. **Sync `staging` with `master`:**
   * **Create a New Pull Request:**
     * Source branch: `staging`
     * Target branch: `master`
     * Title: "Sync staging to master"
     * Description: "This PR syncs the latest changes from staging to master."
   * **Merge the PR:** Once approved, merge the pull request **using REBASE AND FF**.
3.  **Delete the Feature Branch Locally:**

    ```bash
    git checkout dev
    git branch -D feature/store-test-results
    ```
4.  **Pull the Latest Changes Locally:**

    After syncing all branches, make sure to pull the latest changes to your local repository:

    ```bash
    git pull origin dev
    git pull origin staging
    git pull origin master
    ```

***

### **Step 7: Test the Setup**

1. **Create a Test Pull Request:**
   * Create a new branch from `dev`, make a minor change (e.g., edit a README file), and push the branch.
   * Create a pull request to merge this branch into `staging`.
2. **Observe the Pipeline Execution:**
   * Go to the pull request in Azure DevOps and check the status of the pipeline.
   * Ensure that the pipeline runs successfully and that test results are archived and pushed to the separate repository.
3. **Check the Test Results Repository:**
   * Navigate to the `LibraryManagementSystem-TestResults` repository.
   * Verify that the test results and metadata are correctly stored under the appropriate build ID.
4. **Repeat the Test for the `master` Branch:**
   * Once the PR is merged into `staging`, repeat the process by creating a PR to merge `staging` into `master`.
   * Ensure that the pipeline properly archives and stores the test results in the repository.

***

### **Conclusion**

In this live demo, we successfully updated the CI pipeline to store test results in a separate repository. This setup ensures that your test results are preserved long-term, even if pipeline runs are deleted. By following these steps, you’ve enhanced the traceability and reliability of your CI/CD process, making it easier to track test results over time.

This additional capability is particularly useful for maintaining a historical record of test outcomes, which can be crucial for audit purposes, debugging, and continuous improvement.
