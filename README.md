# Junior-Java-Developer-BVP-Software
***
## Spring Boot + PostgreSQL Dockerized

### Creating a docker network
```shell
docker network create my_network
```

### Postgresql docker setup

```shell
#Build PostfresSQL image (From the position of the Dockerfile)
docker build . -t postgres_db
```

```shell
#Run Postgres container from image
docker run --name my_database --network my_network postgres_db
```

### Spring Boot docker setup

```shell
#Build Spring boot image (From the position of the Dockerfile)
docker build . -t spring_boot_service
```

```shell
#Run backend service and hos tit on localhost port 8080
docker run -d -p 8080:8080 --name backend --network my_network spring_boot_service
```

***
## Project Documentation

This is a brief documentation of your project describing the requirements for running the application, initializing the database, launching the application, and interacting with the endpoints.

### Requirements
- Java SDK version 21
- A suitable Java IDE like IntelliJ IDEA
- Spring Boot compatible with your JDK version
- Lombok plugin installed in your IDE if you use Lombok in your project
- A MySQL server for the database

### How to Initialize the Database

This project uses Spring Data JPA which makes database interactions easier and reduces boilerplate code. The database and all the tables required are created automatically if they do not exist.

You will need to specify the following properties in your application.properties or application.yml file:

- spring.datasource.url (your database url)
- spring.datasource.username (your database username)
- spring.datasource.password (your database password)

The property spring.jpa.hibernate.ddl-auto should be set to update or create for automatic creation of the tables.
***
## How to Launch the Application

To run the application:

1. Import the project into your IDE
2. Wait for dependencies specified in the pom.xml/build.gradle to be downloaded
3. Run the application by running the main class, usually annotated with @SpringBootApplication
4. The application will start on the default port 8080 unless specified otherwise in the application.properties or application.yml file with server.port

***
#   B V P  
 