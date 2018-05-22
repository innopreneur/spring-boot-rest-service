# Spring-boot-rest-service

### Employee Resource API structure
- **GET** - http://localhost:8080/api/employees
- **GET** - http://localhost:8080/api/employees/{id}
- **POST** - http://localhost:8080/api/employees
- **PUT** - http://localhost:8080/api/employees/{id}
- **DELETE** - http://localhost:8080/api/employees/{id}


### Project Structure 
```
+---src
|   +---main
|   |   +---docker
|   |   |       DockerFile
|   |   |       Wrapper.sh
|   |   |       
|   |   +---java
|   |   |   \---com
|   |   |       \---company
|   |   |           \---services
|   |   |               \---web
|   |   |                   |   EmployeeServiceApplication.java
|   |   |                   |   SwaggerConfiguration.java
|   |   |                   |   
|   |   |                   +---controller
|   |   |                   |       EmployeeController.java
|   |   |                   |       
|   |   |                   +---exception
|   |   |                   |       CustomizedResponseEntityExceptionHandler.java
|   |   |                   |       EmployeeNotFoundException.java
|   |   |                   |       ExceptionResponse.java
|   |   |                   |       
|   |   |                   +---model
|   |   |                   |       Employee.java
|   |   |                   |       
|   |   |                   +---repository
|   |   |                   |       EmployeeRepository.java
|   |   |                   |       
|   |   |                   \---service
|   |   |                           EmployeeService.java
|   |   |                           
|   |   \---resources
|   |           application.properties
|   |           create_employees_table.sql
|   |           create_hobbies_table.sql
|   |           
|   \---test
|       \---java
|           \---com
|               \---company
|                   \---services
|                       \---web
|                           +---controller
|                           |       EmployeeControllerIntegrationTest.java
|                           |       
|                           +---employeeservice
|                           |       EmployeeServiceApplicationTests.java
|                           |       
|                           \---service
|                                   EmployeeServiceTest.java
|                                   
\---target
|
|
\---docker-compose.yml
|
|
\---pom.xml
 ```

### Important files/folders in this project
#### **docker-compose.yml** 
docker compose file to orchestrate and run containers for *employee-rest-servicej.ar* and *mysql server*

#### **Wrapper.sh**
shell script wait for *mysql server* to get started before starting *employee-rest-service.jar*

### **DockerFile**
docker file to build image for *employee-rest-service.jar*

#### **SwaggerConfiguration.java**
To enable support for Swagger docs and UI. For more details refer https://swagger.io/
 
####  **exception/**
 This package contains customized exceptions using **@ControllerAdvice** to apply same exception handling across all controllers(in our case only one)

#### **resources/**
This folder contains properties for this spring boot application. It also contains **.sql** files to create tables for the mysql server docker container.

#### **src/test/java**
This package contains all tests. There are two types of tests :
	- **Unit Tests** - Covered for *EmployeeService.java*
	- **Integration Tests** - Covered for *EmployeeController.java*
> ***Note*** - Due to time unavailability, I have not covered all tests extensively, but current coverage handles common use cases). Ideally, there can be lot other tests for negative scenarios and fields validations.

## Add-Ons
Apart from the required functionalities, I have also tried to add few useful utilities as below :
-	Hal Browser (https://docs.spring.io/spring-data/rest/docs/current/reference/html/#_the_hal_browser)
-	Spring boot Actuator (http://www.baeldung.com/spring-boot-actuators)
-	Swagger (https://swagger.io/)
-	HATEOS (https://en.wikipedia.org/wiki/HATEOAS)
-	Custom Exception Handling
-	Basic Authentication (credentials in application.properties)
-	Content Negotiation XML support (Accept: application/xml)
-	Dev Tools integration (Auto Server restart on change)

#### Some Key Endpoints
>Provide credentials from **application.properties** if asked
- Swagger API - http://localhost:8080/v2/api-docs
- Swagger UI - http://localhost:8080/swagger-ui.html
- HAL browser - http://localhost:8080/browser/index.html
- Actuator -  http://localhost:8080/actuator
 


## How to set up ?
### without Docker
------
***PreRequisites*** 
- Java 8
- Maven 3
- MySQL Server
- Eclipse IDE (or similar)
- Postman (or SoapUI) (Optional)
 -----
1. Install and start MySQL server.
2. Set password for **root** user as **password**
3. Create database named **company**.
4. Use .sql scripts in src/main/resources/ package, and create tables **employees** and **hobbies**.
5. run **mvn clean package**
6. from **{basedir}/target**, start **employee-rest-service.jar**, using command *java -jar employee-rest-service.jar*
7. REST api is exposed and ready to be consumed using any clients like Postman (or even integrated swagger or hal browser)
---
### with Docker
---
***PreRequisites*** 
- Java 8
- Maven 3
- Docker 17.03 (or above)
- Eclipse IDE (or similar)
- Postman (or SoapUI) (Optional)
---
1. Build the docker container for our *employee-rest-service, using command  ***mvn clean package docker:build***
2. Once above command has run successfully, run ***docker-compose up***. This will run our *docker-compose.yml* file. 
3.  Now verify if both containers (employee-rest-service.jar) and (mysql-server) are up and running using command - **docker container ls**
4. If you see 2 containers then required REST api is exposed and ready to be consumed using any clients like Postman (or even integrated swagger or hal browser)
> Tests can be executed by command - **mvn test**

### Assumptions
* Employee Id is auto generated and incremented. Since, it was unclear to me what type of id is needed for employee (**java.util.UUID** or auto generated **java.lang.Long** id), I have used both as Employee attributes. But, employee has java.util.Long as primary key,but it also has java.util.UUID randomly generated.