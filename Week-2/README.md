# Connecting Hands - User Management Service

This project provides a comprehensive User Management microservice built with Java, Spring Boot, and PostgreSQL. It features a secure REST API with JWT-based authentication for handling user registration, login, and full CRUD operations.

## Features

- **User Authentication**: Secure registration and login endpoints with JWT.
- **Role-Based Access Control (RBAC)**: Supports `USER` and `ADMIN` roles, with endpoints protected accordingly.
- **User Management**: Full CRUD (Create, Read, Update, Delete) functionality for user profiles.
- **Database Migrations**: Uses Flyway for managing database schema evolution.
- **API Documentation**: Integrated Swagger UI for easy API exploration and testing.
- **Unit-Tested**: Comprehensive unit tests for services and controllers.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Flyway**
- **JWT (JSON Web Tokens)**
- **Lombok**
- **ModelMapper**
- **JUnit 5 & Mockito**

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for running PostgreSQL)
- An IDE like IntelliJ IDEA or VS Code

## Getting Started

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd <repository-directory>
```

### 2. Configure the Database

This project uses PostgreSQL as its database. You can run a PostgreSQL instance using Docker Compose.

1.  Make sure Docker is running on your machine.
2.  Navigate to the project root and run:
    ```bash
    docker-compose up -d
    ```
    This will start a PostgreSQL container with the required database and user configured in `docker-compose.yml`.

### 3. Configure Application Properties

Open the `src/main/resources/application.properties` file and ensure the database connection settings match your environment. The default settings are configured to work with the provided Docker Compose setup.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.url=${spring.datasource.url}
spring.flyway.user=${spring.datasource.username}
spring.flyway.password=${spring.datasource.password}
```

### 4. Build and Run the Application

You can build and run the application using Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Endpoints

Once the application is running, you can access the API endpoints.

### Authentication

-   `POST /api/v1/auth/register`: Register a new user.
-   `POST /api/v1/auth/login`: Log in and receive a JWT token.

### User Management

-   `GET /api/v1/users`: Get a list of all users (Admin only).
-   `GET /api/v1/users/{id}`: Get a user by their ID.
-   `PUT /api/v1/users/{id}`: Update a user's profile (Admin or the user themselves).
-   `DELETE /api/v1/users/{id}`: Delete a user (Admin only).

### API Documentation (Swagger)

To explore the API using Swagger UI, navigate to:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Running Tests

To run the unit tests for the application, execute the following Maven command:

```bash
mvn test
``` 