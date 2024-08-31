---
description: >-
  Objective: Ensure that all environments, pipelines, and tests are functioning
  correctly before the presentation. This checklist will help you verify that
  everything is in order.
icon: clipboard-list-check
cover: >-
  https://images.unsplash.com/photo-1505373877841-8d25f7d46678?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwxfHxwcmVzZW50YXRpb258ZW58MHx8fHwxNzIzNzcxMjA3fDA&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Presentation Day Checklist

### **1. Environment Verification**

* [ ] **Development Environment (dev):** Ensure that all recent changes are deployed and functional.
* [ ] **Staging Environment (staging):** Confirm that the latest build is successfully deployed and that all features are working as expected.
* [ ] **Production Environment (prod):** Double-check that the production environment is stable, and no unplanned changes have been made.
* [ ] **Pre-Deployment Check on Production:** Ensure that the pre-deployment check for the production environment is properly configured and functioning as expected.

***

### **2. CI Pipeline Validation**

* [ ] **Unit Tests:** Confirm that all unit tests are passing.
* [ ] **Integration Tests:** Ensure that all integration tests are running without issues.
* [ ] **SonarCloud Analysis:** Verify that the SonarCloud analysis is passing.
* [ ] **Snyk Security Check:** Confirm that the Snyk scan is passing and any identified vulnerabilities have been addressed or are noted.

***

### **3. CD Pipeline Validation (Staging Environment)**

* [ ] **End-to-End (E2E) Tests:** Ensure that all E2E tests are passing on the staging environment.
* [ ] **Load Testing:** Verify that the load testing results meet the expected performance benchmarks.
* [ ] **Dynamic Application Security Testing (DAST):** Check the OWASP ZAP report for any vulnerabilities and ensure they are addressed or documented.

***

### **4. Azure Boards Review**

* [ ] **Work Items:** Confirm the status of all work items. Ensure that those intended to be completed are marked as closed, while any open items are noted as intentionally deferred.
* [ ] **Tasks:** Verify that all tasks are appropriately marked as complete, with any remaining tasks either closed or correctly deferred.
* [ ] **Epics and User Stories:** Review the status of epics and user stories, confirming that all intended work is fully implemented and tested, and note any intentionally left open items.

***

### **5. Final Review**

* [ ] **Branch Syncing:** Ensure that the `dev`, `staging`, and `prod` branches are properly synced, with no unmerged changes.
* [ ] **Branch Policies:** Make sure branch policies are enabled.
* [ ] **Artifact Verification:** Confirm that all build artifacts are properly stored in Cloudsmith and that they correspond to the correct environments.
