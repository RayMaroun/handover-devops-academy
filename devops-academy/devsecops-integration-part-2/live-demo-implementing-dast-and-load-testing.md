---
icon: pen-to-square
cover: >-
  https://images.unsplash.com/photo-1646047354763-fe28e2ce222b?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwyfHxsb2FkfGVufDB8fHx8MTcyMzYwODQ2N3ww&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Live Demo - Implementing DAST and Load Testing

### **Introduction**

In this live demo, you'll explore advanced security and performance testing techniques essential for maintaining the integrity and efficiency of your applications. We will be integrating Dynamic Application Security Testing (DAST) using OWASP ZAP and Performance Testing using JMeter into your Continuous Deployment (CD) pipeline. By the end of this session, you will have a thorough understanding of how to automate these critical testing processes to identify potential vulnerabilities and performance bottlenecks in your staging and production environments.

***

### **Part 1: DAST Introduction**

Dynamic Application Security Testing (DAST) is a method of testing live web applications to identify vulnerabilities that could be exploited by attackers. In this section, you'll learn how to integrate OWASP ZAP, a powerful and widely-used DAST tool, into your CD pipeline. This integration will enable you to automatically scan your applications in the staging and production environments, ensuring that any potential security issues are identified and addressed before they can be exploited.

***

### **Step 1: Create a Feature Branch**

```bash
git checkout dev
git pull origin dev
git checkout -b feature/add-dast
```

***

### **Step 2: Install OWASP ZAP in the Pipeline**

1.  Integrate OWASP ZAP in the CD Pipeline YAML File:

    * Update your CD pipeline (`cd-pipeline.yml`) as follows:

    ```yaml
    # Run DAST (Dynamic Application Security Testing) using OWASP ZAP
    - script: |
        wget https://github.com/zaproxy/zaproxy/releases/download/v2.15.0/ZAP_2.15.0_Linux.tar.gz
        mkdir zap
        tar -xvf ZAP_2.15.0_Linux.tar.gz -C zap --strip-components=1
        cd zap
        ./zap.sh -cmd -quickurl $(HEROKU_STAGING_URL) -quickprogress -quickout /home/vsts/work/1/a/zap_report_staging.html
      displayName: "DAST on Staging with OWASP ZAP"

    # Publish the OWASP ZAP report as a build artifact
    - task: PublishBuildArtifacts@1
      inputs:
        pathToPublish: "$(Build.ArtifactStagingDirectory)/zap_report_staging.html"
        artifactName: zap_report_staging
      displayName: "Publish ZAP Report (Staging)"
    ```

***

### **Step 3: Commit and Push the Updated YAML File**

1.  **Ensure you are on the `feature/add-dast` branch:**

    * Add, commit, and push the changes to the repository.

    ```bash
    git add .
    git commit -m "Integrate OWASP ZAP for DAST in CI pipeline"
    git push origin feature/add-dast
    ```

***

### **Step 4: Create a Pull Request**

1. **Navigate to Azure DevOps:**
   * Go to **Repos** -> **Pull Requests**.
2. **Create a New Pull Request:**
   * **Source branch:** `feature/add-dast`
   * **Target branch:** `dev`
   * **Title:** "OWASP ZAP Integration"
   * **Description:** "This PR is for the integration of OWASP ZAP."
3. **Assign Reviewers:** (Optional)
   * Add team members as reviewers if needed.
4. **Complete Code Review:**
   * Once the review is completed, approve and merge the PR into the `dev` branch using MERGE NO FF.

***

### **Step 5: Sync Branches and Verify OWASP ZAP Integration**

**Objective:** Ensure that all branches (`dev`, `staging`, and `master`) are in sync with the latest changes and that OWASP ZAP analysis results are consistently reflected across environments.

1. **Sync `dev` with `staging`:**
   * Create a PR from `dev` to `staging` and complete the review process using REBASE AND FF.
2. **Sync `staging` with `master`:**
   * Create a PR from `staging` to `master` and complete the review process using REBASE AND FF.
3. **Verify OWASP ZAP Analysis:**
   * Go to your OWASP ZAP project dashboard and verify that the analysis results appear for all branches.

***

### **Step 6: Monitoring and Addressing Issues**

1. **Monitor Identified Vulnerabilities:**
   * Regularly review the OWASP ZAP reports for new vulnerabilities.
   * Address the identified vulnerabilities in a timely manner to ensure the security of your applications.

***

### **Step 7: Testing OWASP ZAP Integration**

**Objective:** Validate the integration of OWASP ZAP by running a scan on the staging environment and reviewing the findings.

#### **Step-by-Step Guide**

1.  **Create a Feature Branch for Testing:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/test-owasp-zap-integration
    ```
2.  **Make a Minor Change:**

    * Open any file in the codebase (e.g., `pom.xml`) and make a small change, such as adding a comment:

    ```xml
    <!-- This is a test comment to trigger OWASP ZAP analysis -->
    ```
3.  **Commit and Push the Changes:**

    ```bash
    git add .
    git commit -m "Test OWASP ZAP integration for DAST"
    git push origin feature/test-owasp-zap-integration
    ```
4. **Create a Pull Request:**
   * Navigate to **Azure DevOps**: Go to **Repos** -> **Pull Requests**.
   * Create a new Pull Request:
     * **Source branch:** `feature/test-owasp-zap-integration`
     * **Target branch:** `dev`
     * **Title:** "Test OWASP ZAP Integration"
     * **Description:** "This PR is to test the integration of OWASP ZAP for DAST."
   * Assign reviewers if needed, and complete the code review. Approve and merge the PR into the `dev` branch.
5. **Verify OWASP ZAP Analysis:**
   * Monitor the pipeline run triggered by the merge to ensure that the OWASP ZAP scan runs successfully.
   * Review the `zap_report_staging.html` report for any identified vulnerabilities.
6. **Sync Branches:**
   * Sync `dev` with `staging`: Create a PR from `dev` to `staging`, review, approve, and merge using REBASE AND FF. Pull the latest changes from `staging` locally.
   * Sync `staging` with `master`: Create a PR from `staging` to `master`, review, approve, and merge using REBASE AND FF. Pull the latest changes from `master` locally.
   * Ensure all branches are up to date.
7.  **Delete Local Feature Branch:**

    ```bash
    git branch -D feature/test-owasp-zap-integration
    ```

***

### **Part 2: Performance Testing Introduction**

Performance testing is crucial for ensuring that your application can handle expected user loads without compromising on speed or stability. In this section, you will integrate JMeter, a popular open-source performance testing tool, into your CD pipeline. This integration will allow you to simulate user load on your application in the staging environment and identify any performance bottlenecks that need to be addressed before your application is deployed to production.

***

### **Step 1: Create a Feature Branch**

```bash
git checkout dev
git pull origin dev
git checkout -b feature/add-performance-testing
```

***

### **Step 2: Install JMeter in the Pipeline**

Before updating the CD pipeline, you need to add two important files to your project:

1. **Add the JMeter Test Plan File:**
   * Place the `LibraryManagementSystem_Demo_PerformanceTest.jmx` file in your project directory.
   *   This file will contain the JMeter test plan for performing the performance tests.\


       ```xml
       <?xml version="1.0" encoding="UTF-8"?>
       <jmeterTestPlan version="1.2" properties="5.0" jmeter="5.6.3">
         <hashTree>
           <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="Library Test Plan">
             <elementProp name="TestPlan.user_defined_variables" elementType="Arguments" guiclass="ArgumentsPanel" testclass="Arguments" testname="User Defined Variables">
               <collectionProp name="Arguments.arguments"/>
             </elementProp>
           </TestPlan>
           <hashTree>
             <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Demo Load Test">
               <intProp name="ThreadGroup.num_threads">10</intProp>
               <intProp name="ThreadGroup.ramp_time">20</intProp>
               <boolProp name="ThreadGroup.same_user_on_next_iteration">true</boolProp>
               <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
               <elementProp name="ThreadGroup.main_controller" elementType="LoopController" guiclass="LoopControlPanel" testclass="LoopController" testname="Loop Controller">
                 <stringProp name="LoopController.loops">5</stringProp>
                 <boolProp name="LoopController.continue_forever">false</boolProp>
               </elementProp>
             </ThreadGroup>
             <hashTree>
               <ConfigTestElement guiclass="HttpDefaultsGui" testclass="ConfigTestElement" testname="HTTP Request Defaults">
                 <stringProp name="HTTPSampler.domain">library-management-ray-staging-db89a7b181a7.herokuapp.com</stringProp>
                 <stringProp name="HTTPSampler.protocol">https</stringProp>
                 <elementProp name="HTTPsampler.Arguments" elementType="Arguments" guiclass="HTTPArgumentsPanel" testclass="Arguments" testname="User Defined Variables">
                   <collectionProp name="Arguments.arguments"/>
                 </elementProp>
                 <stringProp name="HTTPSampler.implementation">HttpClient4</stringProp>
               </ConfigTestElement>
               <hashTree/>
               <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="Get All Books">
                 <stringProp name="HTTPSampler.path">/api/books</stringProp>
                 <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
                 <stringProp name="HTTPSampler.method">GET</stringProp>
                 <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
                 <boolProp name="HTTPSampler.postBodyRaw">false</boolProp>
                 <elementProp name="HTTPsampler.Arguments" elementType="Arguments" guiclass="HTTPArgumentsPanel" testclass="Arguments" testname="User Defined Variables">
                   <collectionProp name="Arguments.arguments"/>
                 </elementProp>
               </HTTPSamplerProxy>
               <hashTree/>
               <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="Get All Authors">
                 <stringProp name="HTTPSampler.path">/api/authors</stringProp>
                 <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
                 <stringProp name="HTTPSampler.method">GET</stringProp>
                 <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
                 <boolProp name="HTTPSampler.postBodyRaw">false</boolProp>
                 <elementProp name="HTTPsampler.Arguments" elementType="Arguments" guiclass="HTTPArgumentsPanel" testclass="Arguments" testname="User Defined Variables">
                   <collectionProp name="Arguments.arguments"/>
                 </elementProp>
               </HTTPSamplerProxy>
               <hashTree/>
               <ResultCollector guiclass="SummaryReport" testclass="ResultCollector" testname="Summary Report">
                 <boolProp name="ResultCollector.error_logging">false</boolProp>
                 <objProp>
                   <name>saveConfig</name>
                   <value class="SampleSaveConfiguration">
                     <time>true</time>
                     <latency>true</latency>
                     <timestamp>true</timestamp>
                     <success>true</success>
                     <label>true</label>
                     <code>true</code>
                     <message>true</message>
                     <threadName>true</threadName>
                     <dataType>true</dataType>
                     <encoding>false</encoding>
                     <assertions>true</assertions>
                     <subresults>true</subresults>
                     <responseData>false</responseData>
                     <samplerData>false</samplerData>
                     <xml>false</xml>
                     <fieldNames>true</fieldNames>
                     <responseHeaders>false</responseHeaders>
                     <requestHeaders>false</requestHeaders>
                     <responseDataOnError>false</responseDataOnError>
                     <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>
                     <assertionsResultsToSave>0</assertionsResultsToSave>
                     <bytes>true</bytes>
                     <sentBytes>true</sentBytes>
                     <url>true</url>
                     <threadCounts>true</threadCounts>
                     <idleTime>true</idleTime>
                     <connectTime>true</connectTime>
                   </value>
                 </objProp>
                 <stringProp name="filename"></stringProp>
               </ResultCollector>
               <hashTree/>
             </hashTree>
           </hashTree>
         </hashTree>
       </jmeterTestPlan>
       ```
2. **Add the JTL to JUnit Converter Script:**
   * Place the `jtl_junit_converter.py` file in your project directory.
   *   This Python script will convert the JMeter results from `.jtl` format to a JUnit XML format, which can be used for further analysis and reporting.\


       ```python
       import sys, csv
       from datetime import datetime
       from xml.etree import ElementTree
       from xml.dom import minidom
       from xml.etree.ElementTree import Element, SubElement, Comment

       # jtl table columns
       TIMESTAMP = 0
       ELAPSED = 1
       LABEL = 2
       RESPONSE_MESSAGE = 4
       SUCCESS = 7

       def prettify(elem):
       	"""Returns a pretty-printed XML string for the Element.
       	"""
       	rough_string = ElementTree.tostring(elem, 'utf-8')
       	reparsed = minidom.parseString(rough_string)
       	return reparsed.toprettyxml(indent="  ")

       def retrieve_jmeter_results(jmeter_file):
       	"""Returns a list of JMeter JTL rows without header.
       	The JMeter JTL file must be in CSV format.
       	"""
       	csv_reader = csv.reader(jmeter_file)
       	next(csv_reader)
       	return list(csv_reader)

       def create_request_attrib(jmeter_result):
       	"""Returns a JSON with attributes for a JUnit testsuite: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	request_time = float(jmeter_result[ELAPSED])/1000.0

       	return {
       		'name': jmeter_result[LABEL],
       		'time': str(request_time),
       		'error_message': jmeter_result[RESPONSE_MESSAGE]
       	}

       def create_test_case_attrib(request_attrib):
       	"""Returns a JSON with attributes for a JUnit testcase: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	return {
       		'classname': 'httpSample',
       		'name': request_attrib['name'],
       		'time': request_attrib['time']
       }

       def create_test_suite_attrib(junit_results):
       	"""Returns a JSON with attributes for JUnit testsuite: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	return {
       		'id': '1',
       		'name': 'Load Tests',
       		'package': 'load test',
       		'hostname': 'Azure DevOps',
       		'time': str(junit_results['time']),
       		'tests': str(junit_results['tests']),
       		'failures': str(len(junit_results['requests']['failures'])),
       		'errors': '0'
       	}

       def create_error_test_case_attrib(error_message):
       	"""Returns a JSON with attributes for JUnit testcase for failed requests: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	return {
       		'message': error_message,
       		'type': 'exception'
       	}

       def requests(jmeter_results):
       	"""Returns a JSON with successful and failed HTTP requests.
       	The JMeter JTL file must be in CSV format.
       	"""
       	failed_requests = []
       	successful_requests = []

       	for result in jmeter_results:
       		request_attrib = create_request_attrib(result)

       		if result[SUCCESS] == 'true':
       			successful_requests.append(request_attrib)
       		else:
       			failed_requests.append(request_attrib)

       	return {
       		'success': successful_requests,
       		'failures': failed_requests
       	}

       def total_time_seconds(jmeter_results):
       	"""Returns the total test duration in seconds.
       	The JMeter JTL file must be in CSV format.
       	"""
       	max_timestamp = max(jmeter_results, key=lambda result: int(result[TIMESTAMP]))
       	min_timestamp = min(jmeter_results, key=lambda result: int(result[TIMESTAMP]))
       	total_timestamp = int(max_timestamp[TIMESTAMP]) - int(min_timestamp[TIMESTAMP])

       	return float(total_timestamp)/1000.0

       def create_junit_results(jtl_results_filename):
       	with open(jtl_results_filename) as jmeter_file:
       		jmeter_results = retrieve_jmeter_results(jmeter_file)
       		time = total_time_seconds(jmeter_results)

       		return {
       			'tests': len(jmeter_results),
       			'time': time,
       			'requests': requests(jmeter_results),
       		}

       def create_properties(test_suite):
       	"""Creates a JUnit properties element for testsuite: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	return SubElement(test_suite, 'properties')

       def create_test_suite(test_suites, junit_results):
       	"""Creates a JUnit testsuite: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	test_suite_attrib = create_test_suite_attrib(junit_results)
       	test_suite = SubElement(test_suites, 'testsuite', test_suite_attrib)

       	create_properties(test_suite)

       	failed_requests = len(junit_results['requests']['failures'])
       	successful_requests = len(junit_results['requests']['success'])

       	for success_index in range(successful_requests):
       		successful_request = junit_results['requests']['success'][success_index]
       		create_successful_test_case(test_suite, successful_request)

       	for error_index in range(failed_requests):
       		failed_request = junit_results['requests']['failures'][error_index]
       		create_failed_test_case(test_suite, failed_request)

       def create_failed_test_case(test_suite, failed_request):
       	"""Creates a JUnit test case for failed HTTP requests: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	test_case_attrib = create_test_case_attrib(failed_request)
       	error_test_case_attrib = create_error_test_case_attrib(failed_request['error_message'])

       	test_case = SubElement(test_suite, 'testcase', test_case_attrib)
       	test_case_error = SubElement(test_case, 'error', error_test_case_attrib)

       def create_successful_test_case(test_suite, successful_request):
       	"""Creates a JUnit test case for successful HTTP requests: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	test_case_attrib = create_test_case_attrib(successful_request)
       	test_case = SubElement(test_suite, 'testcase', test_case_attrib)

       def create_test_suites(jtl_results_filename):
       	"""Creates a JUnit testsuites element: https://llg.cubic.org/docs/junit/
       	The JMeter JTL file must be in CSV format.
       	"""
       	test_suites = Element('testsuites')
       	junit_results = create_junit_results(jtl_results_filename)
       	create_test_suite(test_suites, junit_results)

       	return prettify(test_suites)

       def main():
         print('Converting...')

         with open(sys.argv[2], "w") as output_file:
           test_suites = create_test_suites(sys.argv[1])
           output_file.write(test_suites)

         print('Done!')

       if __name__ == '__main__':
       	main()
       ```
3. Integrate JMeter in the CD Pipeline YAML File:
   *   Update your CD pipeline (`cd-pipeline.yml`) as follows:\


       ```yaml
                 # Run the JMeter Performance Test on the Staging Environment
                 - script: |
                     wget -c https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.6.3.tgz
                     tar -xf apache-jmeter-5.6.3.tgz
                   displayName: 'Install the dependency packages'

                 - script: |
                     mkdir -p results
                     apache-jmeter-5.6.3/bin/./jmeter -Jjmeter.reportgenerator.overall_granularity=1000 -n -t $(System.DefaultWorkingDirectory)/LibraryManagementSystem_Demo_PerformanceTest.jmx -l results/results.jtl -j results/output.log -e -o report
                   displayName: 'Run JMeter'

                 - script: |
                     JMETER_RESULTS=results/results.jtl
                     JUNIT_RESULTS=output.xml
                     python3 jtl_junit_converter.py $JMETER_RESULTS $JUNIT_RESULTS
                   displayName: 'RESULTS: Convert JMeter Results to JUnit Format'

                 - task: PublishTestResults@2
                   inputs:
                     testResultsFormat: 'JUnit'
                     testResultsFiles: 'output.xml'
                     failTaskOnFailedTests: true
                   displayName: 'RESULTS: Publish Load Testing Results'

                 - publish: $(System.DefaultWorkingDirectory)/results
                   artifact: jmeter-results
                   condition: succeededOrFailed()
                   displayName: 'RESULTS: Publish Load Test Artifacts'

                 - task: PublishPipelineArtifact@1
                   displayName: 'Publish JMeter HTML Report'
                   inputs:
                     targetPath: report
                     artifact: jmeter-report

       ```



***

### **Step 3: Commit and Push the Updated YAML File**

1.  **Ensure you are on the `feature/add-performance-testing` branch:**

    * Add, commit, and push the changes to the repository.

    ```bash
    git add .
    git commit -m "Integrate JMeter for performance testing in CD pipeline"
    git push origin feature/add-performance-testing
    ```

***

### **Step 4: Create a Pull Request**

1. **Navigate to Azure DevOps:**
   * Go to **Repos** -> **Pull Requests**.
2. **Create a New Pull Request:**
   * **Source branch:** `feature/add-performance-testing`
   * **Target branch:** `dev`
   * **Title:** "JMeter Integration"
   * **Description:** "This PR is for the integration of JMeter for performance testing."
3. **Assign Reviewers:** (Optional)
   * Add team members as reviewers if needed.
4. **Complete Code Review:**
   * Once the review is completed, approve and merge the PR into the `dev` branch using MERGE NO FF.

***

### **Step 5: Sync Branches and Verify JMeter Integration**

**Objective:** Ensure that all branches (`dev`, `staging`, and `master`) are in sync with the latest changes and that JMeter performance testing results are consistently reflected across environments.

1. **Sync `dev` with `staging`:**
   * Create a PR from `dev` to `staging` and complete the review process using REBASE AND FF.
2. **Sync `staging` with `master`:**
   * Create a PR from `staging` to `master` and complete the review process using REBASE AND FF.
3. **Verify JMeter Performance Testing:**
   * Go to your pipeline dashboard and verify that the JMeter performance testing results appear for all branches.

***

### **Step 6: Monitoring and Addressing Issues**

1. **Monitor Identified Performance Issues:**
   * Regularly review the JMeter reports for performance bottlenecks.
   * Address the identified performance issues in a timely manner to ensure the efficiency of your applications.

***

### **Step 7: Testing JMeter Integration**

**Objective: Validate the integration of JMeter by running a performance test on the staging environment and reviewing the findings.**

1.  **Create a Feature Branch for Testing:**

    ```bash
    git checkout dev
    git pull origin dev
    git checkout -b feature/test-jmeter-integration
    ```
2.  **Make a Minor Change:**

    * Open any file in the codebase (e.g., `pom.xml`) and make a small change, such as adding a comment:

    ```xml
    <!-- This is a test comment to trigger JMeter performance testing -->
    ```
3.  **Commit and Push the Changes:**

    ```bash
    git add .
    git commit -m "Test JMeter integration for performance testing"
    git push origin feature/test-jmeter-integration
    ```
4. **Create a Pull Request:**
   * Navigate to **Azure DevOps**: Go to **Repos** -> **Pull Requests**.
   * Create a new Pull Request:
     * **Source branch:** `feature/test-jmeter-integration`
     * **Target branch:** `dev`
     * **Title:** "Test JMeter Integration"
     * **Description:** "This PR is to test the integration of JMeter for performance testing."
   * Assign reviewers if needed, and complete the code review. Approve and merge the PR into the `dev` branch.
5. **Verify JMeter Performance Testing:**
   * Monitor the pipeline run triggered by the merge to ensure that the JMeter performance test runs successfully.
   * Review the `jmeter_report_staging` and `jmeter_report_prod` for any identified performance issues.
6. **Sync Branches:**
   * Sync `dev` with `staging`: Create a PR from `dev` to `staging`, review, approve, and merge using REBASE AND FF. Pull the latest changes from `staging` locally.
   * Sync `staging` with `master`: Create a PR from `staging` to `master`, review, approve, and merge using REBASE AND FF. Pull the latest changes from `master` locally.
   * Ensure all branches are up to date.
7.  **Delete Local Feature Branch:**

    ```bash
    git branch -D feature/test-jmeter-integration
    ```

***

### Conclusion

By completing this live demo, you have now integrated both Dynamic Application Security Testing (DAST) with OWASP ZAP and Performance Testing with JMeter into your CD pipeline. These tools will help you ensure that your applications are both secure and performant under real-world conditions. Regularly running these tests as part of your CI/CD process will help you maintain high standards of security and performance, ultimately leading to more reliable and robust software deployments.
