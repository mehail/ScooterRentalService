<img src="https://github.com/mehail/ScooterRentalService/blob/master/other/logo/logo.png"/>

____

# General description

*Electric scooter rental management system*

## Program name

*Scooter Rental Service*

## The purpose of the application

This software was written as part of the final assignment for Java development courses from SENLA. The main goal of
creating this application was the practical application of the knowledge I have accumulated about creating applications
using the Spring framework, Hibernate and relational databases.

## Implementation information

* **Programming language:** Java
* **Java version:** "11"

RESTful application is written using technologies:

* Spring Boot
* Spring Web
* Spring Security
* Spring Data JPA
* Hibernate
* Swagger

Object-relational database management system

* PostgreSQL

Containerization system

* Docker
* Docker-compose

___

# Description of functionality

* User registration in the system (administrators and clients)
* Editing the user's personal information
* Hierarchical list of rental points with geographical reference
* Ability to add / remove / edit scooters and rental points
* View detailed information about the rental point
* Charging for the use of scooters (hourly, subscription, the ability to set a price, discount)

___

# Launching the app

## 1. Install:

<table>
    <thead>
        <tr>
            <th>Software</th>
            <th>Version min.</th>
            <th>Deb based linux</th>
            <th>Windows</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td colspan="4" align="center"><i>Without containerization system</i></td>
        </tr>
        <tr>
            <td>Java</td>
            <td align="center">11</td>
            <td><i>sudo apt install openjdk-11-jdk</i></td>
            <td><a href="https://jdk.java.net/java-se-ri/11">jdk.java.net</a></td>
        </tr>
        <tr>
            <td>PostgreSQL</td>
            <td align="center">13</td>
            <td><i>sudo apt install postgresql</i></td>
            <td><a href="https://www.postgresql.org/download/windows/">postgresql.org</a></td>
        </tr>
        <tr>
            <td>Maven</td>
            <td align="center">3.6.3</td>
            <td><i>sudo apt install maven</i></td>
            <td><a href="https://maven.apache.org/download.cgi">maven.apache.org</a></td>
        </tr>
        <tr>
            <td colspan="4" align="center"><i><sup>*</sup> With containerization system</i></td>
        </tr>
        <tr>
            <td>Java</td>
            <td align="center">11</td>
            <td><i>sudo apt install openjdk-11-jdk</i></td>
            <td><a href="https://jdk.java.net/java-se-ri/11">jdk.java.net</a></td>
        </tr>
        <tr>
            <td>PostgreSQL</td>
            <td align="center">13</td>
            <td><i>sudo apt install postgresql</i></td>
            <td><a href="https://www.postgresql.org/download/windows/">postgresql.org</a></td>
        </tr>
        <tr>
            <td>Docker <sup>(optional)</sup></td>
            <td align="center">20.10 <sup>Linux</sup>
                <br>3.5 <sup>Win</sup></td>
            <td><a href="https://docs.docker.com/docker-for-windows/install/">docker.com</a></td>
            <td><a href="https://docs.docker.com/engine/install/ubuntu/">docker.com</a></td>
        </tr>
        <tr>
            <td>Docker Compose <sup>(optional)</sup></td>
            <td align="center">1.29</td>
            <td colspan="2" align="center"><a href="https://docs.docker.com/compose/install/">docker.com</a></td>
        </tr>
    </tbody>
</table>

## 2. Clone repository or download [archive](https://github.com/mehail/ScooterRentalService/archive/master.zip):

```
  git clone https://github.com/mehail/ScooterRentalService.git
```

## 3. Edit the [application.properties](core/src/main/resources/application.properties) file according to your privacy settings

## 4. Launch the application

* Run the [LinRunner.sh](LinRunner.sh) file for Linux
* Run the [WinRunner.bat](WinRunner.bat) file for Windows


* Run with Docker Compose the [LinDockerRunner.sh](LinDockerRunner.sh) file for Linux
* Run with Docker Compose the [WinDockerRunner.sh](WinDockerRunner.bat) file for Linux


* From the command line / terminal:
```
mvn clean install spring-boot:run
```



## 5. Technical description

After starting the
application <a href="http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/">Swagger UI</a>
available on localhost with the ability to test functionality using the web interface
____

# Contacts

* **Author:** Mihail Artyugin
* **E-mail:** mehailpost@gmail.com