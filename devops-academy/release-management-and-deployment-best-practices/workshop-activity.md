---
icon: flask
cover: >-
  https://images.unsplash.com/photo-1560174971-443de64be852?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw4fHxhcHByb3ZhbHxlbnwwfHx8fDE3MjM2OTMyNDd8MA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Workshop Activity

### **Introduction**

In this workshop activity, you will put into practice the concepts and configurations learned during the live demo on setting up production environments, configuring deployment pipelines, and implementing both pre-deployment approvals and business hours gates in Azure DevOps. The goal of this activity is to reinforce your understanding of these essential deployment controls by applying them to your own project. By the end of this workshop, you should feel confident in your ability to secure and manage production deployments effectively, ensuring that your code is deployed safely and only under the right conditions.

***

### **Part 1: Introduction to Pre-Deployment Approval**

In any production environment, deploying new code requires careful consideration to ensure that the changes do not introduce risks or downtime. Pre-deployment approvals are a crucial control mechanism that allows teams to review and approve changes before they are deployed to production. This process helps maintain the stability and security of the production environment by ensuring that only thoroughly reviewed and tested code makes it to the live application. In this section, you will learn how to set up and enforce pre-deployment approvals within your Azure DevOps pipeline, adding an essential layer of security to your deployment process.

***

### **Step 1: Create a Feature Branch**

Begin by creating a feature branch in your Git repository to make the necessary changes.

```bash
git checkout dev
git pull origin dev
git checkout -b feature/add-approval
```

***

### **Step 2: Create a Production Environment in Azure DevOps**

1. **Navigate to the Environments Section:**
   * Go to your Azure DevOps project.
   * Click on **Pipelines** in the left-hand menu.
   * Select **Environments**.
2. **Create a New Environment:**
   * Click on **+ New environment**.
   * Enter a name for the environment, such as `Production`.
   * Leave the resources empty, as deployments will be managed via Heroku.
3. **Define Environment Permissions:**
   * Configure security settings to restrict access. Only authorized personnel should have deployment permissions for the Production environment.

***

### **Step 3: Configure the Deployment Job in the YAML Pipeline**

#### **What is a Deployment Job?**

A deployment job in Azure DevOps is a specialized job type designed specifically for deploying applications to environments, such as production, staging, or development. Unlike a regular job that simply runs a set of tasks in a pipeline, a deployment job provides more control and flexibility when deploying your application. It is tightly integrated with Azure DevOps environments, allowing you to take advantage of features like approvals, checks, and environment-specific configurations.

#### **Key Differences Between Deployment Jobs and Regular Jobs:**

* **Environment Integration:** Deployment jobs are directly linked to environments defined in Azure DevOps. This linkage enables environment-specific settings and policies, such as approvals, gates, and resource management.
* **Approval and Checks:** Deployment jobs can have pre-deployment approvals and checks configured. This ensures that deployments only proceed when they meet specified criteria, adding an extra layer of control.
* **Strategy Control:** Deployment jobs support various deployment strategies, such as `runOnce`, `rolling`, and `canary`. These strategies dictate how the deployment process is executed, offering options like deploying to all targets simultaneously or incrementally.
* **Resource Management:** Deployment jobs can manage and control environment resources, such as virtual machines, containers, or cloud services, ensuring that the appropriate resources are used during the deployment.

#### **Deployment Job Breakdown:**

Let's break down the example deployment job provided in the YAML pipeline:

```yaml
- deployment: DeployToProduction
  displayName: "Deploy to Heroku Production"
  environment: 'Production'
  strategy:
    runOnce:
      deploy:
        steps:
          # Rest of the Steps
```

* **`deployment: DeployToProduction`:** Defines a deployment job named `DeployToProduction`.\

* **`displayName: "Deploy to Heroku Production"`:** The name displayed in the Azure DevOps UI.\

* **`pool: vmImage: 'ubuntu-latest'`:** Specifies the VM image to use for the deployment. In this case, it's an Ubuntu machine.\

* **`environment: 'Production'`:** Links this deployment job to the `Production` environment defined in Azure DevOps.\

* **`strategy:`:** Defines how the deployment will be carried out. Here, the `runOnce` strategy is used, which means the deployment will run once and deploy to all targets simultaneously.

#### **Deployment Strategies:**

**`runOnce`:**

* **Description:** The "[runOnce](https://learn.microsoft.com/en-us/azure/devops/pipelines/yaml-schema/jobs-deployment-strategy-run-once?view=azure-pipelines)" strategy executes the deployment once and deploys to all targets at the same time. This approach is straightforward and is best suited for scenarios where you have a single environment or a small number of instances that need to be updated simultaneously.
* **Use Cases:**
  * **Single-Instance Environments:** If you’re deploying to an environment with only one server, such as a small web application running on a single VM, the `runOnce` strategy makes sense because there’s no need to stagger the deployment.
  * **Non-Critical Applications:** For applications where downtime is acceptable, deploying to all instances at once can simplify the process.
*   **Example:** Suppose you have a small internal tool running on a single Azure VM, and you want to deploy the latest version. The `runOnce` strategy would deploy the update directly to that VM in one go.

    ```yaml
    strategy:
      runOnce:
        deploy:
          steps:
            - script: echo "Deploying to single-instance environment"
    ```

**`rolling`:**

* **Description:** The "[rolling](https://learn.microsoft.com/en-us/azure/devops/pipelines/yaml-schema/jobs-deployment-strategy-rolling?view=azure-pipelines)" strategy deploys the application to a subset of the environment’s instances at a time. It rolls out the deployment across the instances incrementally, which can minimize downtime and reduce the risk of the entire environment being affected by potential issues with the new deployment.
* **Use Cases:**
  * **Large Environments:** When dealing with large-scale applications running across many instances (e.g., multiple VMs, containers, or server farms), a rolling deployment helps to avoid taking the entire application offline.
  * **Minimal Downtime:** Rolling deployments ensure that some instances remain active while others are being updated, which is crucial for high-availability applications.
*   **Example:** Imagine deploying an update to an e-commerce website running across 10 VMs. A rolling deployment might update two VMs at a time. This way, 80% of the VMs remain operational during the update, minimizing the impact on users.

    ```yaml
    strategy:
      rolling:
        maxParallel: 2  # Deploy to 2 instances at a time
        deploy:
          steps:
            - script: echo "Rolling deployment in progress"
    ```

**`canary`:**

* **Description:** The "[canary](https://learn.microsoft.com/en-us/azure/devops/pipelines/yaml-schema/jobs-deployment-strategy-canary?view=azure-pipelines)" strategy deploys the new version to a small subset of the environment first, known as "canary" instances. After this initial deployment, the new version is monitored, and if everything looks good, the deployment gradually proceeds to the remaining instances. This strategy helps in catching issues early, before they impact the majority of users.
* **Use Cases:**
  * **Production Testing:** Canary deployments are often used in production environments to test new features or updates on a small scale before rolling them out to all users.
  * **Risk Mitigation:** By deploying to only a few instances first, you can validate the new version’s stability and performance, reducing the risk of widespread issues.
*   **Example:** Suppose you are deploying a new feature to a social media platform that has millions of users. A canary deployment would first release the update to 5% of the servers. If no issues are detected, the deployment would gradually expand to 20%, 50%, and eventually 100% of the servers.

    ```yaml
    strategy:
      canary:
        increments: [5, 20, 50, 100]  # Deploy to 5%, then 20%, then 50%, then 100% of instances
        deploy:
          steps:
            - script: echo "Canary deployment in progress"
    ```

**Summary of When to Use Each Strategy:**

* **`runOnce`:** Best for simple environments where you can afford downtime or where there’s only one instance.
* **`rolling`:** Ideal for large, distributed environments where minimizing downtime is crucial.
* **`canary`:** Perfect for production environments where new updates need to be tested with minimal risk before full deployment.

#### **Update the YAML Pipeline for Production Deployment:**

Modify the `cd-pipeline.yml` file to include a deployment job for the Production environment.

```yaml
# Deploy to the Production environment
- stage: DeployToProduction
  displayName: "Deploy to Production"
  dependsOn: []  # This stage does not depend on any other CD stage
  condition: eq(variables['Build.SourceBranch'], 'refs/heads/master') # Only run this stage for the master branch
  jobs:
    - deployment: DeployToProduction
      displayName: "Deploy to Heroku Production"
      environment: 'Production' # Reference to the environment configured in Azure DevOps
      strategy:
        runOnce:
          deploy:
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

              # Ensure system.properties specifies Java 17
              - script: |
                  echo "java.runtime.version=17" > system.properties
                displayName: "Ensure Java 17 in system.properties"

              # Verify system.properties file
              - script: cat system.properties
                displayName: "Verify system.properties"

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
                displayName: "Deploy to Heroku Production"
                env:
                  HEROKU_API_KEY: $(HEROKU_API_KEY)
```

#### **Define the Deployment Job:**

The deployment job references the `Production` environment created in Azure DevOps. It includes steps to install necessary tools, verify configurations, and deploy the application to Heroku.

***

### **Step 4: Implement Pre-Deployment Approvals**

1. **Set Up Pre-Deployment Approvals:**
   * Go to the Azure DevOps **Environments** section.
   * Select the `Production` environment.
   * Navigate to **Approvals and checks**.
   * Click on **+ Add check** and select **Approvals**.
   * Specify the users or groups required to approve deployments to the `Production` environment.
   * Configure additional options, such as a timeout for approvals and notification settings.
2.  **Save the Approval Settings:**

    Save the configuration to enforce pre-deployment approvals for the `Production` environment.

***

### **Step 5: Commit and Push the Updated YAML File**

1.  **Ensure you are on the `feature/add-approval` branch:**

    * Add, commit, and push the changes to the repository.

    ```bash
    git add .
    git commit -m "Update CD pipeline to include environment"
    git push origin feature/add-approval
    ```

***

### **Step 6: Create a Pull Request**

1. **Navigate to Azure DevOps:**
   * Go to **Repos** -> **Pull Requests**.
2. **Create a New Pull Request:**
   * **Source branch:** `feature/add-approval`
   * **Target branch:** `dev`
   * **Title:** "Add Approval"
   * **Description:** "This PR is for adding Approval."
3. **Assign Reviewers:** (Optional)
   * Add team members as reviewers if needed.
4. **Complete Code Review:**
   * Once the review is completed, approve and merge the PR into the `dev` branch using MERGE NO FF.

***

### **Step 7: Sync Branches**

**Objective:** Ensure that all branches (`dev`, `staging`, and `master`) are in sync with the latest changes.

1. **Sync `dev` with `staging`:**
   * Create a PR from `dev` to `staging` and complete the review process using REBASE AND FF.
2. **Sync `staging` with `master`:**
   * Create a PR from `staging` to `master` and complete the review process using REBASE AND FF.
3.  **Clean Up:**

    * **Delete the feature branch locally and remotely:**

    ```bash
    git branch -D feature/add-approval
    ```

***

### **Step 8: Testing the Production Deployment Process**

**Objective:** Validate the setup by triggering a deployment to the `Production` environment, verifying the approval process, and ensuring successful deployment.

1.  **Create a Feature Branch for Testing:**

    * **Switch to the `dev` branch and pull the latest changes:**

    ```bash
    git checkout dev
    git pull origin dev
    ```

    * **Create a new branch for testing the deployment:**

    ```bash
    git checkout -b feature/test-production-deployment
    ```
2.  **Make a Minor Change:**

    * **Open any file (e.g., `pom.xml`) and make a small change, such as adding a comment:**

    ```xml
    <!-- This is a test comment for production deployment -->
    ```
3.  **Commit and Push the Changes:**

    * **Add, commit, and push the changes:**

    ```bash
    git add .
    git commit -m "Test production deployment with minor change"
    git push origin feature/test-production-deployment
    ```
4. **Create a Pull Request:**
   * **Navigate to Azure DevOps:**
     * Go to **Repos** -> **Pull Requests**.
   * **Create a new Pull Request:**
     * **Source branch:** `feature/test-production-deployment`
     * **Target branch:** `dev`
     * **Title:** "Test Production Deployment"
     * **Description:** "This PR tests the production deployment process with a minor change."
   * **Assign Reviewers:** (Optional)
     * Add team members as reviewers if needed.
   * **Complete Code Review:**
     * Once the review is completed, approve and merge the PR into the `dev`branch.
5. **Sync Branches:**
   * Sync `dev` with `staging`: Create a PR from `dev` to `staging`, review, approve, and merge using REBASE AND FF. Pull the latest changes from `staging` locally.
   * Sync `staging` with `master`: Create a PR from `staging` to `master`, review, approve, and merge using REBASE AND FF. Pull the latest changes from `master` locally.
   * Ensure all branches are up to date.
6. **Approve Deployment:**
   * The deployment will pause at the pre-deployment approval step.
   * Approvers will receive a notification to review the deployment.
   * Once approved, the deployment will proceed to the `Production` environment.
7. **Verify Deployment:**
   * Confirm that the application is successfully deployed to the `Production` environment.
   * Review any logs and deployment reports to ensure there are no issues.
8.  **Clean Up:**

    * **Delete the feature branch locally and remotely:**

    ```bash
    git branch -D feature/test-production-deployment
    ```

***

### **Part 2: Introduction to Implementing a Business Hours Gate**

Deployments to production environments often come with inherent risks, and performing these deployments outside of regular business hours can compound those risks. Implementing a business hours gate ensures that deployments only occur during specified hours when the necessary personnel are available to address any issues that may arise. This gate helps mitigate the potential impact of any deployment-related problems by limiting changes to times when the team is fully staffed and ready to respond. In this part of the demo, you will learn how to configure a business hours gate in Azure DevOps, providing an additional safeguard for your production deployments.

***

### **Step 1: Implement a Business Hours Gate**

1. **Set Up a Business Hours Gate:**
   * Go to the Azure DevOps **Environments** section.
   * Select the `Production` environment.
   * Navigate to **Approvals and checks**.
   * Click on **+ Add check** and select **Business hours**.
   * Specify the business hours during which deployments can occur, such as 9 AM to 5 PM, Monday to Friday.
2. **Save the Gate Settings:**
   * Save the configuration to enforce the business hours gate for the `Production` environment.

***

### **Step 2: Test the Business Hours Gate**

1. **Attempt to Deploy Outside of Business Hours:**
   * Ensure the deployment does not proceed outside of the specified business hours.
2. **Verify Deployment:**
   * Confirm that the application is successfully deployed during the specified business hours.
3.  **Clean Up:**

    * **Delete the feature branch locally and remotely:**

    ```bash
    git branch -D feature/test-production-deployment
    ```

***

### **Conclusion**

Through this workshop, you have gained hands-on experience in setting up critical deployment controls within Azure DevOps. By configuring pre-deployment approvals and business hours gates, you have learned how to implement essential safeguards that help maintain the stability and security of your production environment. These practices are key to ensuring that your deployments are well-managed and aligned with your organization’s operational policies. As you continue to develop and deploy applications, these skills will be invaluable in maintaining the integrity and reliability of your production systems.
