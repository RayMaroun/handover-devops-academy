---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1542034890068-a100eb40a269?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHw2fHxicmFuY2h8ZW58MHx8fHwxNzI0NTM3MjMwfDA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Live Demo 3 - Adding Branch Validation Pipeline

### **Introduction**

In this live demo, we will add a new pipeline to your project that will serve as a build validation for the `staging` and `master` branches. This pipeline ensures that you can only merge changes into `staging` from `dev` and into `master` from `staging`. By the end of this demo, you will have a validation mechanism in place that enforces branch integrity, preventing unauthorized merges.

***

### **Step 1: Create a New Pipeline File**

1.  **Create a New Branch for the Pipeline:**

    First, let’s create a new branch where we will add the validation pipeline:

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/add-branch-validation-pipeline
    ```

    > **Explanation:** By working on a feature branch, you isolate these changes from the main codebase, allowing for easier review and testing.
2.  **Add a New Pipeline File:**

    In your project’s root directory, create a new YAML file for the pipeline. Name it something descriptive like `branch-validation-pipeline.yml`.

    > **Explanation:** Creating a dedicated YAML file for this pipeline keeps your project organized and makes it clear what each pipeline is responsible for.
3.  **Copy and Paste the Pipeline Configuration:**

    Open the new `branch-validation-pipeline.yml` file in your code editor and paste the following configuration:

    ```yaml
    trigger: none  # This pipeline shouldn't trigger on commits, only run as a PR validation

    jobs:
      - job: ValidateBranch
        pool:
          vmImage: 'ubuntu-latest'
        steps:
          - script: |
              echo "Validating branch merge..."
              allowed_branch_for_staging="dev"
              allowed_branch_for_master="staging"
              
              if [ "$(System.PullRequest.TargetBranch)" == "refs/heads/staging" ]; then
                if [ "$(System.PullRequest.SourceBranch)" != "refs/heads/$allowed_branch_for_staging" ]; then
                  echo "Error: Only 'dev' branch can be merged into 'staging'."
                  exit 1
                fi
              elif [ "$(System.PullRequest.TargetBranch)" == "refs/heads/master" ]; then
                if [ "$(System.PullRequest.SourceBranch)" != "refs/heads/$allowed_branch_for_master" ]; then
                  echo "Error: Only 'staging' branch can be merged into 'master'."
                  exit 1
                fi
              else
                echo "Branch validation passed."
              fi
            displayName: 'Validate Source and Target Branches'
    ```

    > **Explanation:** This pipeline configuration includes a job that validates whether the source and target branches for a pull request are correct. It ensures that only the `dev` branch can be merged into `staging` and only the `staging` branch can be merged into `master`.
4. **Save the new pipeline file.**

***

### **Step 2: Commit and Push the Changes**

Now that we’ve created the pipeline file, it’s time to commit and push the changes to the remote repository.

1.  **Stage the New Pipeline File:**

    ```bash
    git add branch-validation-pipeline.yml
    ```
2.  **Commit the Changes:**

    ```bash
    git commit -m "Add branch validation pipeline for staging and master branches"
    ```
3.  **Push the Changes to the Remote Repository:**

    ```bash
    git push origin feature/add-branch-validation-pipeline
    ```

***

### **Step 3: Create a Pull Request and Merge into `dev`**

1. **Create a Pull Request to Merge** `feature/add-branch-validation-pipeline` **into `dev`:**
   * **Navigate to Azure DevOps:**
     * Go to Repos -> Pull Requests.
   * **Create a New Pull Request:**
     * Source branch: `feature/add-branch-validation-pipeline`
     * Target branch: `dev`
     * Title: "Add Branch Validation Pipeline"
     * Description: "This PR adds a pipeline to validate branch merges into staging and master."
   * **Assign Reviewers:** Add team members as reviewers.
   * **Merge the PR:** Once the code is approved, merge the pull request **using MERGE**.

***

### **Step 4: Create Pipeline from YAML on Azure DevOps**

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
   * Switch to the `dev` branch and select the newly added `branch-validation-pipeline.yml` file.
7. **Click on Continue:**
   * Proceed to the next step.
8. **Run the Pipeline:**
   * Click on Run to execute the pipeline for the first time.

***

### **Step 5: Rename the CI Pipeline**

1. **Rename the CI pipeline on the Azure DevOps dashboard** to something descriptive, like “Branch-Validation-Pipeline.”

***

### **Step 6: Integrate the Pipeline as a Build Validation**

**6.1 Add the Pipeline to Branch Policies**

1.  **Navigate to Azure DevOps:**

    Go to your Azure DevOps project and navigate to the **Repos** section.
2. **Select the `staging` Branch:**
   * Go to **Branches** and select the `staging` branch.
   * Click on the **ellipsis** (…) next to the `staging` branch and select **Branch policies**.
3.  **Add a Build Validation Policy:**

    * Under **Build validation**, click **+ Add build policy**.
    * In the **Build pipeline** dropdown, select the pipeline you just created (e.g., `Branch-Validation-Pipeline`).
    * Ensure that the policy is set to **Required**.
    * Click **Save**.

    > **Explanation:** By adding this build validation policy, any pull request attempting to merge into `staging` will trigger the branch validation pipeline, ensuring that only allowed branches can be merged.
4.  **Repeat the Process for the `master` Branch:**

    * Go back to **Branches** and select the `master` branch.
    * Follow the same steps to add the build validation pipeline as a required policy for the `master` branch.

    > **Note:** This ensures that the same validation is applied to both the `staging` and `master` branches.

***

### **Step 7: Sync Branches**

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
    git branch -D feature/add-branch-validation-pipeline
    ```
4.  **Pull the Latest Changes Locally:**

    After syncing all branches, make sure to pull the latest changes to your local repository:

    ```bash
    git checkout dev
    git pull origin dev

    git checkout staging
    git pull origin staging

    git checkout master
    git pull origin master
    ```

***

### **Step 8: Test the Pipeline**

1. **Create a Test Pull Request:**
   * Create a new branch from `dev`, make a minor change (e.g., edit a README file), and push the branch.
   * Create a pull request to merge this branch into `staging`.
2. **Observe the Validation Pipeline:**
   * Go to the pull request in Azure DevOps and check the status of the validation pipeline.
   * Ensure that the pipeline runs and validates the source and target branches correctly.
3. **Repeat the Test for the `master` Branch:**
   * Once the PR is merged into `staging`, repeat the process by creating a PR to merge `staging` into `master`.
   * Ensure that the validation pipeline enforces the branch rules correctly.

***

### **Conclusion**

In this live demo, we successfully added and configured a branch validation pipeline to enforce merge rules for the `staging` and `master` branches. This ensures that only the correct branches can be merged into your critical environments, maintaining the integrity of your development workflow.

This setup not only enhances the security of your codebase but also automates the enforcement of branch policies, making your CI/CD process more reliable and robust.
