# my-budget

> A Java web application

# Build setup

Gradle is used as build tool for the project.

### Prerequisites

You need to install and setup JDK if you didn't already **(used version JDK 8)**

You need to install Gradle **(used version 4.6)**

You need to install MySQL 5.7 and setup the database for the project. To do this you need to:

- Install MySQL 5.7 - https://dev.mysql.com/downloads/mysql/
- Run *createDB.sql* script from the *sql* folder of the project, in order to create the database and the user with access to it
- Run *init.sql* script from the *sql* folder of the project, in order to create the initial tables and data

1. To start the application use:
    ``` bash
    gradlew appRun
    ```
    The application should be available on: 
    > http://localhost:8080/my-budget