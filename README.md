# Resource Booking System

A RESTful Resource Booking System built using Spring Boot, Spring Security, JWT, JPA/Hibernate, and MySQL.

The application provides secure authentication, role-based access control, resource management, and reservation management for ADMIN and USER roles.

## Features

- JWT-based authentication
- BCrypt password hashing
- Role-based access control
- ADMIN and USER roles
- Resource CRUD operations
- Reservation creation and management
- JWT-based reservation ownership
- Reservation status management
- Reservation filtering by status
- Reservation filtering by minimum and maximum price
- Pagination
- Sorting
- Request validation
- Global exception handling
- Swagger/OpenAPI documentation
- Automated tests using JUnit 5 and MockMvc
- Initial ADMIN and USER accounts

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger / OpenAPI
- JUnit 5
- MockMvc

## User Roles

### ADMIN

ADMIN users can:

- View resources
- Create resources
- Update resources
- Delete resources
- View all reservations
- Update reservations
- Delete reservations

### USER

USER users can:

- View resources
- Create reservations
- View only their own reservations

Reservation ownership is determined from the authenticated JWT identity.

## Reservation Status

Reservations support the following statuses:

- `PENDING`
- `CONFIRMED`
- `CANCELLED`

New reservations are created with the `PENDING` status by default.

## API Endpoints

### Authentication

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/auth/login` | Public | Authenticate user and return JWT |

### Resources

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/resources` | USER, ADMIN | View resources |
| GET | `/resources/{id}` | USER, ADMIN | View a resource by ID |
| POST | `/resources` | ADMIN | Create a resource |
| PUT | `/resources/{id}` | ADMIN | Update a resource |
| DELETE | `/resources/{id}` | ADMIN | Delete a resource |

### Reservations

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/reservations` | USER, ADMIN | Create a reservation |
| GET | `/reservations` | USER, ADMIN | View reservations |
| PUT | `/reservations/{id}` | ADMIN | Update a reservation |
| DELETE | `/reservations/{id}` | ADMIN | Delete a reservation |

## Authentication

Login using:

```http
POST /auth/login
Content-Type: application/json

Request:
{
  "username": "user",
  "password": "User@123"
}

A successful login returns:
{
  "token": "JWT_TOKEN",
  "username": "user",
  "role": "USER"
}

Use the token for protected endpoints:

Authorization: Bearer JWT_TOKEN
Reservation Filtering

Reservations can be filtered by status and price.

Filter by status
GET /reservations?status=PENDING
Filter by minimum price
GET /reservations?minPrice=100
Filter by maximum price
GET /reservations?maxPrice=1000
Filter by price range
GET /reservations?minPrice=100&maxPrice=1000
Combine filters
GET /reservations?status=PENDING&minPrice=100&maxPrice=1000

For USER requests, only reservations belonging to the authenticated user are returned.

For ADMIN requests, reservations from all users can be viewed.

Pagination

Reservations support pagination using page and size.

Example:

GET /reservations?page=0&size=10
page starts from 0
size must be between 1 and 100
Sorting

Reservations support sorting using sortBy and sortDir.

Example:

GET /reservations?sortBy=price&sortDir=desc

Supported sorting fields:

id
startTime
endTime
price
status
createdAt
updatedAt

Supported directions:

asc
desc
Validation and Error Handling

The application uses Jakarta Bean Validation for request validation.

Examples include:

Resource ID is required for reservations
Start time is required
End time is required
Start time must be in the future
End time must be in the future
Resource name is required
Resource location is required
Resource price must be greater than zero

Global exception handling provides consistent error responses for:

Validation errors
Resource not found
Invalid request parameters
Invalid login credentials
Unexpected server errors

Example error response:

{
  "timestamp": "2026-09-30T10:30:00",
  "status": 404,
  "message": "Resource not found",
  "path": "/resources/10"
}
Database Setup

The application uses MySQL.

Create the database:

CREATE DATABASE resource_booking_db;

Hibernate/JPA creates and updates the required database tables.

Configuration

Configure the application database and JWT settings in:

src/main/resources/application.properties

Before publishing the project, do not commit real passwords or production secrets.

Recommended environment variables:

DB_URL=jdbc:mysql://localhost:3306/resource_booking_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secure_jwt_secret
JWT_EXPIRATION=86400000
Running the Application
Prerequisites
Java 21
Maven
MySQL
Git
1. Clone the repository
git clone <repository-url>
2. Create the database
CREATE DATABASE resource_booking_db;
3. Configure the database

Set the required database and JWT configuration.

4. Run the application
mvn spring-boot:run

The application runs on:

http://localhost:8080
Swagger / OpenAPI

Swagger UI:

http://localhost:8080/swagger-ui/index.html

OpenAPI documentation:

http://localhost:8080/v3/api-docs

Swagger can be used to view and test the REST APIs.

Default Test Users

The application initializes the following users for local testing.

ADMIN
Username: admin
Password: Admin@123
Role: ADMIN
USER
Username: user
Password: User@123
Role: USER

These credentials are intended for local development and assignment testing.

Testing

The project includes automated tests using JUnit 5 and MockMvc.

The test suite covers:

Successful login and JWT generation
Invalid login credentials
Application context loading
USER access to resources
USER restriction from creating resources
USER reservation creation
USER reservation access
ADMIN reservation status update

Run all tests:

mvn clean test

Current test result:

Tests run: 8
Failures: 0
Errors: 0
Skipped: 0
Project Structure
src
└── main
    └── java
        └── com.rohan.booking
            ├── config
            ├── controller
            ├── dto
            │   ├── auth
            │   ├── resource
            │   └── reservation
            ├── entity
            ├── enums
            ├── exception
            ├── repository
            ├── security
            └── service
Security

The application implements:

JWT-based authentication
BCrypt password hashing
Role-based authorization
Protected REST endpoints
JWT-based user identification
Reservation ownership enforcement
ADMIN-only resource modification
ADMIN-only reservation update and deletion
Database Entities
User

Stores:

User ID
Username
Encrypted password
Role
Resource

Stores:

Resource ID
Name
Description
Location
Price
Availability
Reservation

Stores:

Reservation ID
User
Resource
Start time
End time
Price
Status
Created time
Updated time
HTTP Status Codes

The API uses standard HTTP status codes:

200 OK — Successful request
201 Created — Resource or reservation created
204 No Content — Successful deletion
400 Bad Request — Invalid request or validation failure
401 Unauthorized — Authentication failure
403 Forbidden — Insufficient permissions
404 Not Found — Requested resource does not exist
500 Internal Server Error — Unexpected server error
Assignment

This project demonstrates:

REST API development
Spring Boot
Spring Security
JWT authentication
Role-based authorization
JPA/Hibernate persistence
MySQL integration
Request validation
Global exception handling
Filtering
Pagination
Sorting
Automated API testing