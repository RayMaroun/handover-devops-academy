---
icon: flask
cover: >-
  https://images.unsplash.com/photo-1542147864-027e5d1330c9?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwyfHx3b3JrJTIwc2hvcHxlbnwwfHx8fDE3MjM0MTQyNzF8MA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Workshop Activity

### **Introduction**

In this workshop activity, you'll apply the concepts of Continuous Deployment (CD) by setting up and configuring a CD pipeline in Azure DevOps for your own project. The goal of this exercise is to reinforce your understanding of automating the deployment process, managing different environments, and ensuring that your application is consistently delivered to production with minimal manual intervention. By working through these steps, you'll gain hands-on experience with real-world deployment scenarios, using Heroku as the target environment.

***

### **Step 1: Set Up Heroku Accounts and Applications**

1. **Create an Account on Heroku:**
   * If you don't have a Heroku account, sign up at the [Heroku website](https://www.heroku.com).
2. **Create Applications on Heroku:**
   * **Development Environment:**
     * Go to the Heroku dashboard and create a new app named `bank-management-<YourFirstName>-development`.
   * **Staging Environment:**
     * Create another app named `bank-management-<YourFirstName>-staging`.
   * **Production Environment:**
     * Finally, create a third app named `bank-management-<YourFirstName>-prod`.
   * If the name is too long, remove `management`.
3. **Add JawsDB Plugin (Free Version):**
   * For each Heroku app, navigate to the **Resources** tab.
   * Under **Add-ons**, search for **JawsDB MySQL** and select the free plan.
   * Once added, JawsDB will automatically generate the necessary database URL and credentials.
4. **Set Up Environment Variables:**
   * Go to the **Settings** tab of each Heroku app.
   * Add the following **Config Vars**:
     * `spring.datasource.url`: `jdbc:mysql://<your-database-url>`
     * `spring.datasource.username`: `<your-username>`
     * `spring.datasource.password`: `<your-password>`
     * `spring.jpa.hibernate.ddl-auto`: `update`
     * `spring.jpa.properties.hibernate.dialect`: `org.hibernate.dialect.MySQLDialect`
     * `spring.jpa.show-sql`: `true`

***

### **Step 2: Configure Azure DevOps with Heroku Settings and Application Settings**

1. **Store Heroku Settings in Azure DevOps:**
   * Navigate to **Pipelines** > **Library** in Azure DevOps.
   * Create a new variable group named **HerokuSettings** or select an existing group.
   * Add the following variables:
     * `HEROKU_API_KEY`: Your Heroku API key (mark as secret).
     * `HEROKU_DEV_APP`: The name of the Heroku development app (e.g., `bank-management-<YourFirstName>-development`).
     * `HEROKU_STAGING_APP`: The name of the Heroku staging app (e.g., `bank-management-<YourFirstName>-staging`).
     * `HEROKU_PRODUCTION_APP`: The name of the Heroku production app (e.g., `bank-management-<YourFirstName>-prod`).
     * `HEROKU_STAGING_URL`: The URL of the Heroku staging app (e.g., `https://bank-management-<YourFirstName>-staging.herokuapp.com`).

***

### **Step 3: Set Up Cloudsmith Entitlement Tokens**

1. **Obtain Entitlement Tokens from Cloudsmith:**
   * Log into your Cloudsmith account and generate entitlement tokens for accessing packages.
   * Ensure you have tokens for the development, staging, and production repositories.
2. **Add Cloudsmith Entitlement Tokens to Azure DevOps Library:**
   * In Azure DevOps, navigate to **Pipelines** > **Library**.
   * Add the following variables:
     * `CLOUDSMITH_ENTITLEMENT_TOKEN_DEV`: `<your-development-entitlement-token>`
     * `CLOUDSMITH_ENTITLEMENT_TOKEN_STAGING`: `<your-staging-entitlement-token>`
     * `CLOUDSMITH_ENTITLEMENT_TOKEN_MASTER`: `<your-production-entitlement-token>`

***

### **Step 4: Create a Feature Branch and Prepare for CD Pipeline**

1.  **Create a Feature Branch for CD Pipeline Work:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-cd-pipeline
    ```

***

### **Step 5: Add `system.properties` File for Heroku**

1.  **Create `system.properties` File:**

    * In the root directory of your project, create a file named `system.properties`.
    * Add the following content to specify the Java version:

    ```plaintext
    java.runtime.version=17
    ```

    \
    This file ensures that Heroku uses the specified Java version for your application.

***

### **Step 6: Update `application.properties`**

1.  **Update the `application.properties` Locally:**

    * In the `application.properties` file, add the following line:

    ```properties
    server.port=${PORT:8080}
    ```

    \
    This ensures that the application binds to the port provided by Heroku's environment, with a default fallback to port 8080.

***

### **Step 7: Update E2E Test Scripts**

1. **Modify E2E Test Scripts:**
   * Add a tag above the E2E test and comment out `@SpringBootTest` to avoid starting the application context.

<pre class="language-java"><code class="lang-java">// Example of updating the e2e test script
<strong>//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
</strong>@Tag("e2e")
public class AccountManagementE2ETest {
    // Test code here...
}
</code></pre>

***

### **Step 8: Configure the CD Pipeline**

#### **Part 1: Creating the YAML File**

1. **Create a New YAML File for the CD Pipeline:**
   * In the root directory of your project, create a file named `cd-pipeline.yml`.

#### **Part 2: CD Pipeline YAML Configuration**

1.  **Basic Configuration and Triggers:**

    {% code overflow="wrap" %}
    ```yaml
    # cd-pipeline.yml

    # No branches trigger this pipeline automatically; it is dependent on the CI pipeline's output.
    trigger:
      branches:
        exclude:
          - "*"

    # Defines the CI pipeline as a resource, triggering this pipeline based on its successful completion.
    resources:
      pipelines:
        - pipeline: ciPipeline
          source: BankManagementSystem-CI
          trigger:
            branches:
              include:
                - dev # Trigger on successful CI pipeline for dev branch
                - staging # Trigger on successful CI pipeline for staging branch
                - master # Trigger on successful CI pipeline for master branch

    # Specifies the virtual machine image to use for the deployment agents.
    pool:
      vmImage: "ubuntu-latest"
    ```
    {% endcode %}

    * **Explanation:**
      * This configuration ensures that the CD pipeline only triggers based on the successful completion of the CI pipeline.
      * The `trigger` section exclude all branches from directly triggering the CD pipeline.
2.  **Setting Variables:**

    {% code overflow="wrap" %}
    ```yaml
    # Grouped variables for managing Cloudsmith and Heroku settings.
    variables:
      - group: CloudsmithSettings # Includes Cloudsmith-related settings (e.g., API keys)
      - group: HerokuSettings # Includes Heroku-related settings (e.g., API keys, app names)
    ```
    {% endcode %}

    * **Explanation:**
      * This section pulls in variables from the groups created earlier, ensuring the necessary credentials and application names are available throughout the pipeline.
3. **Defining Stages for Deployment:**

*   `Dev`:\


    ```yaml
    # Defines the stages for the CD pipeline.
    stages:
      # Deploy to the Development environment
      - stage: DeployToDev
        displayName: "Deploy to Development"
        dependsOn: [] # This stage does not depend on any other CD stage
        condition: eq(variables['Build.SourceBranch'], 'refs/heads/dev') # Only run this stage for the dev branch
        jobs:
          - job: Deploy
            displayName: "Deploy to Heroku Dev"
            steps:
              # Install Cloudsmith CLI
              - script: |
                  pip install cloudsmith-cli
                displayName: "Install Cloudsmith CLI"

              # Install Heroku CLI
              - script: |
                  curl https://cli-assets.heroku.com/install.sh | sh
                displayName: "Install Heroku CLI"

              # Install Java 17 on the deployment agent
              - task: JavaToolInstaller@0
                inputs:
                  versionSpec: "17" # Specify Java version 17
                  jdkArchitectureOption: "x64" # Use 64-bit architecture
                  jdkSourceOption: "PreInstalled" # Use pre-installed JDK if available
                displayName: "Install Java 17"

              # Verify the Java installation
              - script: |
                  echo "JAVA_HOME environment variable: $JAVA_HOME"
                  java -version
                displayName: "Verify Java Installation"

              # Login to Heroku
              - script: |
                  echo ${HEROKU_API_KEY} | heroku auth:token
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
                displayName: "Login to Heroku"

              # Install Heroku Java Plugin
              - script: |
                  heroku plugins:install java
                displayName: "Install Heroku Java Plugin"

              # Create a directory for artifacts
              - script: mkdir -p artifacts
                displayName: "Create artifacts directory"

              # Download the artifact from Cloudsmith
              - script: |
                  echo "Listing packages in Cloudsmith..."
                  json=$(cloudsmith ls pkgs ${ORG}/${REPO_DEV} -F json)
                  echo "Getting list of packages ... OK"
                  echo "Cloudsmith response: $json"
                  url=$(echo $json | jq -r '.data[0].cdn_url')
                  echo "Download URL: $url"
                  wget --auth-no-challenge --user=token --password=${CLOUDSMITH_ENTITLEMENT_TOKEN_DEV} -O artifacts/BankManagementSystem.jar $url
                displayName: "Download Artifact from Cloudsmith (Dev)"

              # Deploy the artifact to the Heroku Dev app
              - script: |
                  heroku deploy:jar artifacts/BankManagementSystem.jar --app $(HEROKU_DEV_APP)
                displayName: "Deploy to Heroku Dev"
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
    ```



*   `Staging`:\


    <pre class="language-yaml"><code class="lang-yaml">  # Deploy to the Staging environment
    <strong>  - stage: DeployToStaging
    </strong>    displayName: "Deploy to Staging"
        dependsOn: [] # This stage does not depend on any other CD stage
        condition: eq(variables['Build.SourceBranch'], 'refs/heads/staging') # Only run this stage for the staging branch
        jobs:
          - job: Deploy
            displayName: "Deploy to Heroku Staging"
            steps:
              # Install Cloudsmith CLI
              - script: |
                  pip install cloudsmith-cli
                displayName: "Install Cloudsmith CLI"
      
              # Install Heroku CLI
              - script: |
                  curl https://cli-assets.heroku.com/install.sh | sh
                displayName: "Install Heroku CLI"
      
              # Install Java 17 on the deployment agent
              - task: JavaToolInstaller@0
                inputs:
                  versionSpec: "17"
                  jdkArchitectureOption: "x64"
                  jdkSourceOption: "PreInstalled"
                displayName: "Install Java 17"
      
              # Verify the Java installation
              - script: |
                  echo "JAVA_HOME environment variable: $JAVA_HOME"
                  java -version
                displayName: "Verify Java Installation"
      
              # Login to Heroku
              - script: |
                  echo ${HEROKU_API_KEY} | heroku auth:token
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
                displayName: "Login to Heroku"
      
              # Install Heroku Java Plugin
              - script: |
                  heroku plugins:install java
                displayName: "Install Heroku Java Plugin"
      
              # Create a directory for artifacts
              - script: mkdir -p artifacts
                displayName: "Create artifacts directory"
      
              # Download the artifact from Cloudsmith
              - script: |
                  echo "Listing packages in Cloudsmith..."
                  json=$(cloudsmith ls pkgs ${ORG}/${REPO_STAGING} -F json)
                  echo "Getting list of packages ... OK"
                  echo "Cloudsmith response: $json"
                  url=$(echo $json | jq -r '.data[0].cdn_url')
                  echo "Download URL: $url"
                  wget --auth-no-challenge --user=token --password=${CLOUDSMITH_ENTITLEMENT_TOKEN_STAGING} -O artifacts/BankManagementSystem.jar $url
                displayName: "Download Artifact from Cloudsmith (Staging)"
      
              # Deploy the artifact to the Heroku Staging app
              - script: |
                  heroku deploy:jar artifacts/BankManagementSystem.jar --app $(HEROKU_STAGING_APP)
                displayName: "Deploy to Heroku Staging"
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
      
              # Run end-to-end (E2E) tests on the Staging environment
              - task: Maven@3
                inputs:
                  mavenVersionOption: 'Default'
                  mavenPomFile: 'pom.xml'
                  goals: 'test'
                  options: '-Dgroups=e2e -DtestType=e2e -Dbase.url=$(HEROKU_STAGING_URL)'
                  testResultsFiles: '**/target/surefire-reports/*e2e*.xml'
                  testRunTitle: 'End-to-End Tests'
                displayName: "Run e2e Tests on Staging Environment"
    </code></pre>



*   `Prod`:\


    ```yaml
      # Deploy to the Production environment
      - stage: DeployToProduction
        displayName: "Deploy to Production"
        dependsOn: [] # This stage does not depend on any other CD stage
        condition: eq(variables['Build.SourceBranch'], 'refs/heads/master') # Only run this stage for the master branch
        jobs:
          - job: Deploy
            displayName: "Deploy to Heroku Production"
            steps:
              # Install Cloudsmith CLI
              - script: |
                  pip install cloudsmith-cli
                displayName: "Install Cloudsmith CLI"
      
              # Install Heroku CLI
              - script: |
                  curl https://cli-assets.heroku.com/install.sh | sh
                displayName: "Install Heroku CLI"
      
              # Install Java 17 on the deployment agent
              - task: JavaToolInstaller@0
                inputs:
                  versionSpec: "17"
                  jdkArchitectureOption: "x64"
                  jdkSourceOption: "PreInstalled"
                displayName: "Install Java 17"
      
              # Verify the Java installation
              - script: |
                  echo "JAVA_HOME environment variable: $JAVA_HOME"
                  java -version
                displayName: "Verify Java Installation"
      
              # Login to Heroku
              - script: |
                  echo ${HEROKU_API_KEY} | heroku auth:token
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
                displayName: "Login to Heroku"
      
              # Install Heroku Java Plugin
              - script: |
                  heroku plugins:install java
                displayName: "Install Heroku Java Plugin"
      
              # Create a directory for artifacts
              - script: mkdir -p artifacts
                displayName: "Create artifacts directory"
      
              # Download the artifact from Cloudsmith
              - script: |
                  echo "Listing packages in Cloudsmith..."
                  json=$(cloudsmith ls pkgs ${ORG}/${REPO_MASTER} -F json)
                  echo "Getting list of packages ... OK"
                  echo "Cloudsmith response: $json"
                  url=$(echo $json | jq -r '.data[0].cdn_url')
                  echo "Download URL: $url"
                  wget --auth-no-challenge --user=token --password=${CLOUDSMITH_ENTITLEMENT_TOKEN_MASTER} -O artifacts/BankManagementSystem.jar $url
                displayName: "Download Artifact from Cloudsmith (Master)"
      
              # Deploy the artifact to the Heroku Production app
              - script: |
                  heroku deploy:jar artifacts/BankManagementSystem.jar --app $(HEROKU_PRODUCTION_APP)
                displayName: "Deploy to Heroku Staging"
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
    ```



* **Explanation:**
  * **Stages:** Each stage corresponds to a different environment (Development, Staging, Production).
  * **dependsOn:** This ensures that stages do not wait for each other to complete; they are independent.
  * **condition:** Each stage is triggered only when the corresponding branch is updated.
  * **Tool Installation:** Ensure Cloudsmith CLI, Heroku CLI, and Java 17 are installed.
  * **Artifact Handling:** Download the built artifact from Cloudsmith and prepare it for deployment.
  * **Deployment:** Deploy the artifact to the specified Heroku environment.
  * End-to-End Tests: Run the End-to-End tests against the deployed app on `staging`.

***

### **Step 9: Commit and Push the YAML File**

1.  **Commit and Push:**

    ```bash
    git add .
    git commit -m "Add CD pipeline configuration"
    git push origin feature/add-cd-pipeline
    ```
2. **Create PR to Merge `feature/add-cd-pipeline` into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to **Repos** > **Pull Requests**.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-cd-pipeline`
     * Target branch: `dev`
     * Title: "Add CD Pipeline"
     * Description: "This PR adds the CD pipeline configuration."
   * **Complete the Review:**
     * Once the PR is reviewed and approved, merge it into `dev` using the **Merge** strategy.
3.  **Delete the `feature/add-cd-pipeline` Branch Locally:**

    ```bash
    git checkout dev
    git branch -D feature/add-cd-pipeline
    ```
4.  **Pull `dev` Locally:**

    ```bash
    git checkout dev
    git pull origin dev
    ```

***

### **Step 10: Create Pipeline from YAML on Azure DevOps** <a href="#step-3-create-pipeline-from-yaml-on-azure-devops" id="step-3-create-pipeline-from-yaml-on-azure-devops"></a>

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
   * Switch to the `dev` branch and select the newly added `cd-pipeline.yml` file.
7. **Click on Continue:**
   * Proceed to the next step.
8. **Run the Pipeline:**
   * Click on Run to execute the pipeline for the first time.

***

### **Step 11: Rename the CD Pipeline** <a href="#step-4-rename-the-ci-pipeline" id="step-4-rename-the-ci-pipeline"></a>

Rename the CI pipeline on the Azure DevOps dashboard to “BankManagementSystem-CD.”

***

### **Step 12: Sync Branches**

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
2.  **Pull `staging` Locally:**

    <pre class="language-bash"><code class="lang-bash">git checkout staging
    <strong>git pull origin staging
    </strong></code></pre>
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

### **Step 13: Making a Minor Change for Testing**

1.  **Create a New Branch for a Minor Change:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/test-cd-pipeline
    ```
2. **Make a Minor Change:**
   * Add a comment or a minor change in a file (e.g., `pom.xml`).
3.  **Commit and Push the Change:**

    ```bash
    git add .
    git commit -m "Test CD pipeline by adding a comment"
    git push origin feature/test-cd-pipeline
    ```
4. **Create a PR and Merge into `dev`:**
   * Create and merge the PR into the `dev` branch.
5. **Sync Branches via PRs:**
   * Sync `dev` with `staging` and `staging` with `master` via PRs.
6.  **Delete Local Feature Branches:**

    ```bash
    git branch -D feature/test-cd-pipeline
    ```

***

### **Step 13: Verify Deployment**

#### **Verify Deployment on Heroku:**

* Log into Heroku and verify that the application is running as expected in both staging and production environments.
* Access the deployed apps and test them live.

***

### **Step 14: C**ustomize **your project**

Use the remaining time to test and customize your project based on the lessons learned from previous and today's sessions.

***

### **Conclusion**

By completing this workshop activity, you’ve successfully implemented a fully automated CD pipeline, deploying your application across multiple environments with confidence. This hands-on experience not only reinforces the theoretical knowledge gained in the course but also prepares you for real-world DevOps challenges. As you continue to work on your projects, the skills and techniques you've practiced here will be invaluable in ensuring efficient, reliable, and scalable software deployments.
