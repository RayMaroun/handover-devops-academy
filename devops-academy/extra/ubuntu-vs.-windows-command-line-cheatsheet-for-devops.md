---
icon: bolt-lightning
cover: >-
  https://images.unsplash.com/photo-1520764588094-8fc067746a8e?crop=entropy&cs=srgb&fm=jpg&ixid=M3wxOTcwMjR8MHwxfHNlYXJjaHwyfHxjaGVhdCUyMHNoZWV0fGVufDB8fHx8MTcyNDk4MzE1OXww&ixlib=rb-4.0.3&q=85
coverY: 0
---

# Ubuntu vs. Windows Command Line Cheatsheet for DevOps

### **Introduction**

In DevOps, knowing how to navigate both Ubuntu and Windows environments is crucial. This cheatsheet offers a quick reference to essential commands across these platforms, helping participants efficiently manage pipelines, scripts, and tasks in their projects.

***

### **Cheatsheet**

#### **Basic Navigation and File Management**

| **Action**                           | **Ubuntu (Linux)**             | **Windows (Command Prompt / PowerShell)** |
| ------------------------------------ | ------------------------------ | ----------------------------------------- |
| **Current Directory**                | `pwd`                          | `cd`                                      |
| **List Files (detailed view)**       | `ls -al`                       | `dir /Q`                                  |
| **Change Directory**                 | `cd <directory>`               | `cd <directory>`                          |
| **Create Directory**                 | `mkdir <directory>`            | `mkdir <directory>`                       |
| **Delete Directory (with contents)** | `rm -rf <directory>`           | `rmdir /S /Q <directory>`                 |
| **Copy File/Directory**              | `cp -r <source> <destination>` | `xcopy <source> <destination> /E /I`      |
| **Move/Rename File/Directory**       | `mv <source> <destination>`    | `move <source> <destination>`             |
| **Delete File**                      | `rm <file>`                    | `del <file>`                              |
| **View File Contents**               | `cat <file>`                   | `type <file>`                             |
| **Edit a File**                      | `nano <file>`                  | `notepad <file>` or `notepad++ <file>`    |

#### **Networking and Troubleshooting**

| **Action**                        | **Ubuntu (Linux)**         | **Windows (Command Prompt / PowerShell)**                  |
| --------------------------------- | -------------------------- | ---------------------------------------------------------- |
| **Ping a Host**                   | `ping <hostname>`          | `ping <hostname>`                                          |
| **Traceroute**                    | `traceroute <hostname>`    | `tracert <hostname>`                                       |
| **Check DNS Records**             | `nslookup <hostname>`      | `nslookup <hostname>`                                      |
| **Check Open Ports**              | `netstat -tuln`            | `netstat -an`                                              |
| **Network Configuration**         | `ifconfig` or `ip a`       | `ipconfig`                                                 |
| **Test Connectivity with a Port** | `telnet <hostname> <port>` | `Test-NetConnection -ComputerName <hostname> -Port <port>` |

#### **System Information and Management**

| **Action**                 | **Ubuntu (Linux)**                  | **Windows (Command Prompt / PowerShell)**     |
| -------------------------- | ----------------------------------- | --------------------------------------------- |
| **Check Disk Usage**       | `df -h`                             | `wmic logicaldisk get size,freespace,caption` |
| **Check Memory Usage**     | `free -h`                           | \`systeminfo                                  |
| **View Running Processes** | `ps aux`                            | `tasklist`                                    |
| **Kill a Process**         | `kill <PID>`                        | `taskkill /PID <PID>`                         |
| **Check System Uptime**    | `uptime`                            | \`systeminfo                                  |
| **Shutdown / Restart**     | `sudo shutdown now` / `sudo reboot` | `shutdown /s /f /t 0` / `shutdown /r /f /t 0` |

#### **Package and Dependency Management**

| **Action**                     | **Ubuntu (Linux)**                       | **Windows (Command Prompt / PowerShell)** |
| ------------------------------ | ---------------------------------------- | ----------------------------------------- |
| **Update Package Index**       | `sudo apt update`                        | `winget upgrade`                          |
| **Upgrade Installed Packages** | `sudo apt upgrade`                       | `winget upgrade --all`                    |
| **Install a Package**          | `sudo apt install <package>`             | `winget install <package>`                |
| **Remove a Package**           | `sudo apt remove <package>`              | `winget uninstall <package>`              |
| **Install Python Packages**    | `pip install <package>`                  | `pip install <package>`                   |
| **Install Node.js Packages**   | `npm install <package>`                  | `npm install <package>`                   |
| **Manage Java Versions**       | `sudo update-alternatives --config java` | `java -version` (for verification)        |

#### **CI/CD Pipeline Tasks**

| **Action**                           | **Ubuntu (Linux)**                                                           | **Windows (Command Prompt / PowerShell)**                                        |
| ------------------------------------ | ---------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| **Grant Execute Permissions**        | `chmod +x <script>`                                                          | Not needed in Windows                                                            |
| **Run a Script**                     | `./<script>.sh`                                                              | `<script>.bat` or `powershell -File <script>.ps1`                                |
| **Install JDK**                      | `sudo apt install openjdk-17-jdk`                                            | `choco install openjdk` (if Chocolatey is installed)                             |
| **Verify Java Installation**         | `java -version`                                                              | `java -version`                                                                  |
| **Install Maven**                    | `sudo apt install maven`                                                     | `choco install maven`                                                            |
| **Run Maven Build**                  | `mvn clean install`                                                          | `mvn clean install`                                                              |
| **Run Unit Tests with Maven**        | `mvn test -Dgroups=unit`                                                     | `mvn test -Dgroups=unit`                                                         |
| **Run Integration Tests with Maven** | `mvn test -Dgroups=integration`                                              | `mvn test -Dgroups=integration`                                                  |
| **Run JMeter Tests**                 | `./apache-jmeter-<version>/bin/jmeter -n -t <testfile>.jmx -l <logfile>.jtl` | `.\apache-jmeter-<version>\bin\jmeter.bat -n -t <testfile>.jmx -l <logfile>.jtl` |
| **Run OWASP ZAP**                    | `./zap.sh -cmd -quickurl <url>`                                              | `zap.bat -cmd -quickurl <url>`                                                   |

#### **Security and Permissions**

| **Action**                  | **Ubuntu (Linux)**                 | **Windows (Command Prompt / PowerShell)**   |
| --------------------------- | ---------------------------------- | ------------------------------------------- |
| **Change File Permissions** | `chmod <permissions> <file>`       | `icacls <file> /grant <User>:<permissions>` |
| **Change File Ownership**   | `chown <user>:<group> <file>`      | N/A (handled via file properties)           |
| **Check Sudo Access**       | `sudo -l`                          | N/A                                         |
| **Add a User to Sudoers**   | `sudo usermod -aG sudo <username>` | N/A                                         |

***

### **Conclusion**

Mastering command-line tasks in both Ubuntu and Windows enhances your versatility in DevOps. Use this cheatsheet to streamline your workflow, whether you're building, testing, or deploying applications across different environments.
