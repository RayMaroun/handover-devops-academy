# DevOps Academy Handover Document

## 1. Introduction

### Project Overview

This document provides a comprehensive handover for the DevOps Academy course, which focuses on using Azure DevOps to implement CI/CD pipelines, manage projects, and deploy applications. Over the course of two weeks, students have engaged in a structured learning path that includes theory, live demos, and workshop activities. The projects involved in this course include a Library Management System, used in live demos, and a Bank Management System, which students built upon each day until their final presentation.

### Purpose of the Handover

The purpose of this handover document is to ensure a smooth transition of the course materials and to provide all necessary information for the continued use and management of the content.

## 2. Repository and Documentation

### GitBook and GitHub Integration

The content for this DevOps Academy was originally created on GitBook and has been automatically generated into a GitHub repository in Markdown format. This integration allows the content to be easily managed, updated, and accessed in both platforms.

#### Key Features:

- **Content Structure**: The repository contains Markdown files that mirror the structure of the GitBook content. This includes all lessons, labs, and resources.
- **Table of Contents**: A Table of Contents (ToC) is included in the GitHub repository, allowing for quick navigation between different sections and pages.
- **Resource Linking**: All necessary resources, such as images, scripts, and reference materials, are linked directly within the Markdown files.
- **Re-importing to GitBook**: If needed, the Markdown content can be re-imported into GitBook, enabling easy updates or republishing.

### Course Published Version

- **Course URL**: [DevOps Academy](https://devray.gitbook.io/devops-academy)

### Project Attachments

- **Library Management System**: This project is used during the live demos and serves as a practical example of the concepts taught. The project files and related resources are included in the repository.
- **Bank Management System**: This project is the focus of the student workshop activities. Each student has worked on their version of this project, building it up over the course of the training. The final version is included in the repository, along with any intermediate versions if needed.

## 3. Environment Setup

### Azure DevOps Setup

The course extensively uses Azure DevOps, leveraging the free tier access provided by Microsoft. Each student was assigned to their own project within Azure DevOps to work on their assignments.

#### Key Points:

- **Free Tier Access**: Azure DevOps offers a free tier that includes 10 parallel jobs with Microsoft-hosted agents, which were utilized during the course. This allows students to have their own isolated environments.
- **Project Creation**: Each student was assigned an individual project within Azure DevOps, ensuring that their work is separated and can be independently assessed.
- **User Management**: Note that adding more users or extending beyond the free tier might incur additional costs.

### Heroku Setup

In addition to Azure DevOps, Heroku was used for deploying applications developed during the course.

#### Key Points:

- **Team Setup**: A team was created on Heroku, and each student was added to this team. Heroku does not offer a free option when part of a team; the minimum requirement is a basic dyno, which necessitates payment.
- **Deployment Costs**: As Heroku's team deployment requires at least a basic dyno, payment is necessary for deploying applications within the course.

### Other Tools and Services

Other tools and services used throughout the course primarily offer free trials or free access tiers, ensuring that students could complete all tasks without additional costs.

## 4. Live Demos and Workshop Activities

### Live Demo

Each day of the course included a live demo, led by the instructor, focused on practical application of the theory covered. The live demos centered around the Library Management System project, demonstrating how to set up CI/CD pipelines, integrate testing, and deploy applications using Azure DevOps.

#### Key Demo Topics:

- **CI/CD Pipeline Setup**: Step-by-step guide on configuring Azure DevOps pipelines for continuous integration and continuous deployment.
- **Testing Integration**: Incorporating unit, integration, and automated tests into the pipeline.
- **Deployment**: Demonstrating deployment to both Azure and Heroku environments.

### Student Workshop

The students worked on a Bank Management System project throughout the course, applying the skills and concepts learned each day. This project was built incrementally, with new features and improvements added daily. On the final day, students presented their completed projects.

#### Key Workshop Activities:

- **Daily Incremental Development**: Each day, students added new features or made improvements to the Bank Management System.
- **Final Presentation**: On the last day, students presented their final version of the project, showcasing the skills they had acquired.

## 5. Technical Details

### Course Overview and Pipeline Details

The final project that each student will present is a Java project built with Spring Boot, connected to a MySQL database, and integrated with Azure DevOps. The project involves both CI and CD pipelines.

#### CI Pipeline:

- **Build Process**: The pipeline is configured to build the Java application.
- **Testing**: Includes unit testing and integration testing.
- **Analysis Tools**: SonarCloud for code quality analysis and Snyk for security analysis.
- **Artifact Management**: The built artifact is uploaded to CloudSmith for storage.

#### CD Pipeline:

- **Artifact Deployment**: The pipeline downloads the artifact from CloudSmith and deploys it to Heroku.
- **Staging Environment Tests**: For the staging environment, the pipeline also includes end-to-end testing, Dynamic Application Security Testing (DAST), and load testing.

### Environment Requirements

To work with the course content and projects, the following environment setup is required:

- **Java 17**: Ensure that Java 17 is installed and configured.
- **Git and Git Bash**: Git and Git Bash should be installed for version control and command-line operations.
- **IntelliJ IDEA**: The primary IDE for development; ensure that IntelliJ IDEA is installed and properly configured.
- **MySQL Workbench**: Used for managing the MySQL database. An empty database should be created for the relevant project before starting.

### Deployment Instructions

The repository includes all necessary files and instructions for deploying the Library Management System and Bank Management System projects. Detailed steps for deployment are provided within the course content, as this is a key part of the learning experience.

### Automation and Scripts

The steps for automation and scripting are embedded within the course content, where students are taught how to execute these as part of their learning. These scripts are not separately included in the repository but are covered comprehensively during the lessons.

## 6. Conclusion

### Summary and Next Steps

This document has provided an overview of the DevOps Academy course, including the repository setup, environment requirements, and key project details. All necessary materials are included in the GitHub repository, ensuring that the course can be easily managed and utilized by the receiving team.

If any further clarification is needed, or if there are any questions regarding the content, please do not hesitate to reach out.
