# install docker, docker-compose and httpie up front
docker-compose up #starts mysql
mvn package #builds the service
java -jar target/uuid-mysql-hibernate-1.jar & #start the service
http POST localhost:8080/products name=paul #creates a product. Hibernate generate a UUID
http GET localhost:8080/products #get all products to see the UUIDs


# Employee API - Coding Challenge 
Goal Your goal is to implement an application that handles the employees of a company. The application must expose a REST API, so that other services can communicate with it easily. 
 
Requirements The application must be able to: 1. Create an employee with the following properties ○ Uuid (generated automatically) ○ E-mail ○ Full name (first and last name) ○ Birthday (format YYYY-MM-DD) ○ List of hobbies (for example, "soccer", "music", etc) 2. Get a list of all employees (response in JSON Array format) 3. Get a specific employee by uuid (response in JSON Object format) 4. Update an employee 5. Delete an employee 
 
Restriction The email field is unique, i.e. 2 employees cannot have the same email 
 
##Bonus (+) Run the database you’ve chosen to store the employee's data in a Docker container (instead of in-memory database) 
 
Notes 
 - Include unit and integration tests 
 -Commit all your development using Git and share your project on GitHub 
 ● Include a README file explaining how to build/run your application and any comments you think it is important 
 