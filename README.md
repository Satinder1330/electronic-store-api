# Electronics Store REST API

A **Spring Boot RESTful API** for managing an electronics store with users, categories, products, carts, and orders. Secured with **JWT**, with pagination and global exception handling.Fully containerized with Docker and deployed on AWS (EC2) and Railway, with comprehensive JUnit 6/Mockito test coverage."

## Features

* User registration and login
* CRUD for categories and products
* Cart management and order placement
* JWT-based security
* Pagination & sorting
* Centralized exception handling

## Live Demos & API Documentation

*AWS (EC2): http://52.60.84.118:9090/swagger-ui/index.html

*Railway : https://electronic-store-api-production-ceac.up.railway.app/swagger-ui/index.html

##  Quality Assurance & Testing
I prioritized code reliability by implementing a comprehensive testing suite for the Service Layer and Controller Layer.

* Unit Testing: Leveraged JUnit 5 and Mockito to isolate business logic from infrastructure.
* Used @MockitoBean to bypass database dependencies for fast, reliable tests.

## Tech Stack

* Java, Spring Boot, Spring Security, Spring Data JPA
* MySQL
* Postman
* Maven, Lombok
* Swagger
* Testing: JUnit 5, Mockito
* Docker
* Deployment on AWS and Railway
* Postman (for API testing)

## Getting Started

1. Clone repo and configure `application.properties` for DB.
2. Build and run:
3. API available at `http://localhost:8080`


## Author

**Satinder Singh** – Java / Spring Boot Developer
