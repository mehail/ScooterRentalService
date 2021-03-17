<img src="https://github.com/mehail/ScooterRentalService/blob/MD/other/Logo/logo.png"/>

# General description

*Electric scooter rental management system*

## Program name

*Scooter Rental Service*

## The purpose of the application

This software was written as part of the final assignment for Java development courses from SENLA. 
The main goal of creating this application was the practical application of the knowledge I have accumulated about 
creating applications using the Spring framework, Hibernate and relational databases.

# Description of functionality

## Implementation information

* **Programming language:** Java
* **Java version:** "11"

RESTful application is written using technologies:

<ul>
  <li>Spring Boot</li>
  <li>Spring Web</li>
  <li>Spring Security</li>
  <li>Spring Data JPA</li>
  <li>Hibernate</li>
</ul>

Object-relational database management system
* PostgreSQL

## Starting

### To run you need install:

<table>
    <thead>
        <tr>
            <th>Software</th>
            <th>Version min.</th>
            <th>deb based linux</th>
            <th>Windows</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Java</td>
            <td align="center">11</td>
            <td><i>sudo apt-get install openjdk-11-jdk</i></td>
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
            <td><i>sudo apt-get install maven</i></td>
            <td><a href="https://maven.apache.org/download.cgi">maven.apache.org</a></td>
        </tr>
        <tr>
            <td>Tomcat</td>
            <td align="center">10</td>
            <td colspan="2"><a href="https://tomcat.apache.org/download-10.cgi">tomcat.apache.org</a></td>
        </tr>
    </tbody>
</table>

### Clone repository:

```
  git clone https://github.com/mehail/ScooterRentalService.git
```
### Or download archive:
<a href="https://github.com/mehail/ScooterRentalService/archive/master.zip">ScooterRentalService</a>

## Application launch options
* Run the runner.bat file  
  

* From the command line / terminal
```
mvn clean
mvn install
mvn spring-boot:run
```

## The main functionality of the program

* User registration in the system (administrators and clients)
* Editing the user's personal information
* Hierarchical list of rental points with geographical reference
* Ability to add / remove / edit scooters and rental points
* View detailed information about the rental point
* Charging for the use of scooters (hourly, subscription, the ability to set a price, discount)

## Contacts

* **Author:** Mihail Artugin
* **E-mail:** mehailpost@gmail.com