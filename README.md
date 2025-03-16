Playkart MicroServices
This repository contains the backend services for Playkart application, implemented using a microservices architecture with Spring Boot. The services include Product, Cart, and User-related functionalities, designed to support a large-scale e-commerce platform.

Services
1. Product Service
The Product Service is responsible for managing products, including:

Storing product data.
Managing product reviews.
Database: MongoDB is used to store product data.

2. Cart Service
The Cart Service handles all shopping cart operations:

Adding/removing items from the cart.
Managing cart details.
Handling orders and processing them.
Database: MySQL is used to store cart and order data.

3. User Service
The User Service manages user-related operations, such as:

User authentication and authorization.
Storing user profiles and account details.
Database: MySQL is used to store user data.

Technologies Used
Spring Boot for building the microservices.
MySQL for storing user and cart data.
MongoDB for storing product data.
REST APIs for communication between services.

Getting Started
Follow these steps to get the project up and running:

1. Clone the Repository:
git clone https://github.com/nightcrawler0112/Playkart_MicroServices.git

3. Set Up the Database:
MongoDB: Set up MongoDB to store product-related data for the Product Service.
MySQL: Set up MySQL to store User and Cart data for the User Service and Cart Service.

5. Configure Application Properties:
Modify the application.properties files for each service to match your database configurations (MongoDB for Product Service, MySQL for User and Cart Services).

6. Build and Run the Services:
Each microservice can be built and run separately using Maven or Gradle.

mvn clean install
mvn spring-boot:run

Or if you're using Gradle:

gradle build
gradle bootRun

Once the services are running, they should be accessible on the respective ports defined in your configuration files.
