---
cover: >-
  https://images.unsplash.com/photo-1701146125128-b0b03267e58a?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwxMHx8cGlwZWxpbmV8ZW58MHx8fHwxNzIzMDg2MTg4fDA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Pipeline Agents, Agent Pools, and Deployment Groups

### **Introduction**

In this lesson, we will explore the roles of pipeline agents, agent pools, self-hosted agents, and deployment groups within CI/CD pipelines. These components are essential for executing tasks and deploying applications efficiently. Understanding their configuration and benefits will help you optimize your CI/CD workflows.

***

### **Pipeline Agents**

#### **Definition and Role:**

**Definition:**

A pipeline agent is a software component responsible for running the jobs and tasks defined in your CI/CD pipeline. Agents can be hosted by the CI/CD service (such as Azure DevOps) or self-hosted on your infrastructure.

**Role:**

Agents execute the steps defined in your pipeline, including compiling code, running tests, and deploying applications. They interact with the CI/CD service to fetch the pipeline configuration, run the specified tasks, and report the results back to the service.

**Example:**

```yaml
# Define jobs in the pipeline
jobs:
- job: Build  # Job for building the application
  pool:
    vmImage: 'ubuntu-latest'  # Use the latest Ubuntu VM image
  steps:
  - script: echo Building...  # Command to run the build script
    displayName: 'Run build script'  # Display name for the build step
```

In this example, the `Build` job runs on an agent using the `ubuntu-latest` image provided by the CI/CD service.

***

### **Agent Pools**

#### **Configuration and Use:**

**Definition:**

Agent pools are collections of agents that can be used to run jobs in your pipelines. They provide a way to manage and organize multiple agents, ensuring efficient resource utilization and job distribution.

**Configuration:**

Agent pools can be configured in the CI/CD service interface. You can create new pools, add agents to existing pools, and assign pools to specific jobs or pipelines.

**Default Agent Pools in Azure DevOps:**

Azure DevOps provides several default agent pools that you can use for your pipelines. These include:

* **Azure Pipelines**: This is the default pool which includes a variety of hosted agents.
* **Hosted Ubuntu**: Linux-based agents running the latest Ubuntu with common build tools.
* **Hosted macOS**: macOS-based agents with tools for building and testing iOS and macOS applications.
* **Hosted Windows**: Windows-based agents with common build tools for .NET and Windows applications.

**Example of Using a Specific Agent Pool in Azure DevOps:**

{% code overflow="wrap" %}
```yaml
# Define jobs in the pipeline
jobs:
- job: Build  # Job for building the application
  pool:
    vmImage: 'windows-latest'  # Use the latest Windows VM image
  steps:
  - script: echo Building on Windows...  # Command to run the build script on Windows
    displayName: 'Run build script on Windows'  # Display name for the build step on Windows
```
{% endcode %}

In this example, the `Build` job is assigned to the `windows-latest` agent pool, which uses Windows-based agents managed by Azure DevOps.

#### **Details of Default Agent Pools:**

1. **Ubuntu (ubuntu-latest, ubuntu-20.04, ubuntu-18.04):**
   * Ideal for Linux-based builds.
   * Pre-installed with popular tools such as Docker, .NET Core, Node.js, Python, Java, and more.
2. **Windows (windows-latest, windows-2019, windows-2016):**
   * Suitable for Windows-based builds.
   * Comes with tools like Visual Studio, .NET Framework, PowerShell, and other Windows-specific tools.
3. **macOS (macos-latest, macos-11, macos-10.15):**
   * Required for macOS and iOS application builds.
   * Includes Xcode, Xamarin, Homebrew, and other macOS-specific development tools.

#### **How to Select an Agent Pool:**

When configuring your pipeline, you can specify the agent pool in the `pool` property. This allows you to select the appropriate environment for your build tasks.

{% code overflow="wrap" %}
```yaml
# Define jobs in the pipeline
jobs:
- job: BuildLinux  # Job for building the application on Ubuntu
  pool:
    vmImage: 'ubuntu-latest'  # Use the latest Ubuntu VM image
  steps:
  - script: echo Building on Ubuntu...  # Command to run the build script on Ubuntu
    displayName: 'Run build script on Ubuntu'  # Display name for the build step on Ubuntu

- job: BuildWindows  # Job for building the application on Windows
  pool:
    vmImage: 'windows-latest'  # Use the latest Windows VM image
  steps:
  - script: echo Building on Windows...  # Command to run the build script on Windows
    displayName: 'Run build script on Windows'  # Display name for the build step on Windows

- job: BuildMacOS  # Job for building the application on macOS
  pool:
    vmImage: 'macos-latest'  # Use the latest macOS VM image
  steps:
  - script: echo Building on macOS...  # Command to run the build script on macOS
    displayName: 'Run build script on macOS'  # Display name for the build step on macOS
```
{% endcode %}

In this example, different jobs are assigned to different agent pools based on the operating system required for the build.

***

### **Self-hosted Agents**

#### **Setup and Benefits:**

**Definition:**

Self-hosted agents are agents that run on your own infrastructure rather than being hosted by the CI/CD service. They offer more control over the build environment and can be tailored to specific needs.

**Setup:**

To set up a self-hosted agent, you need to download the agent software from the CI/CD service, configure it on your machine, and register it with your CI/CD service account.

**Example Setup Steps:**

1. **Download the Agent Software:**
   * Navigate to the CI/CD service's agent setup page.
   * Download the agent software for your operating system.
2.  **Configure the Agent:**

    * Unpack the downloaded software.
    * Run the configuration script and follow the prompts to configure the agent.

    ```bash
    ./config.sh
    ```
3. **Register the Agent:**
   * Provide the necessary details such as the agent pool name and authentication token.
   * Verify that the agent is successfully registered and appears in your agent pool.

**Benefits:**

* **Customization:** Self-hosted agents can be customized with specific software, tools, and configurations required for your build and deployment processes.
* **Cost Savings:** Using self-hosted agents can reduce costs by utilizing existing infrastructure.
* **Security:** Sensitive data and environments can be managed more securely within your own infrastructure.

***

### **Deployment Groups**

#### **Brief Explanation and Role in CI/CD:**

**Definition:**

Deployment groups are collections of target machines (physical or virtual) that are used to deploy applications as part of a CI/CD pipeline. They provide a way to organize and manage the deployment targets for your applications.

**Role in CI/CD:**

Deployment groups are used to automate the deployment process across multiple machines or environments. They allow you to define deployment targets, group them logically, and deploy applications consistently and efficiently.

**Using Deployment Groups for Redundancy and Load Balancing:**

Deployment groups can be configured to ensure redundancy and load balancing in your deployment process. By deploying applications across multiple machines or instances, you can achieve high availability and distribute the load evenly.

**Example of Deployment Group Configuration:**

1. **Create a Deployment Group:**
   * Navigate to the deployment groups section in your CI/CD service.
   * Create a new deployment group and provide a name and description.
2.  **Add Target Machines:**

    * On each target machine, install the deployment agent provided by the CI/CD service.
    * Register the agent with the deployment group using the provided registration script and token.

    ```bash
    ./register.sh
    ```
3. **Define Deployment Tasks:**

{% code overflow="wrap" %}
```yaml
# Define jobs in the pipeline
jobs:
- deployment: DeployWebApp  # Deployment job for deploying the web application
  displayName: 'Deploy Web Application'  # Display name for the deployment job
  environment: 'Production'  # Specifies the target environment for deployment

  strategy:
    runOnce:  # Deployment strategy that runs the deployment steps once
      deploy:
        steps:
        - script: echo Deploying web application...  # Command to run the deploy script
          displayName: 'Deploy script'  # Display name for the deploy script step

        - task: AzureWebApp@1  # Task to deploy the web application to Azure
          inputs:
            azureSubscription: 'AzureServiceConnection'  # Azure service connection for authentication
            appName: 'my-web-app'  # Name of the Azure web app to deploy to
            package: '$(Pipeline.Workspace)/drop'  # Path to the package to be deployed
```
{% endcode %}

In this example, the `DeployWebApp` job uses a deployment group to deploy the web application to the `Production` environment.

***

### **Conclusion**

Understanding pipeline agents, agent pools, self-hosted agents, and deployment groups is crucial for optimizing your CI/CD workflows. Pipeline agents execute your jobs and tasks, while agent pools help manage and distribute these workloads efficiently. Self-hosted agents offer customization and cost benefits, making them ideal for specific needs. Deployment groups streamline the deployment process, ensuring consistent and reliable application delivery across multiple environments, and provide redundancy and load balancing.

By mastering these components, you can build robust and scalable CI/CD pipelines that meet the demands of modern software development and deployment practices.
