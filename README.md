# Resource Booking System

A RESTful Resource Booking System built using Spring Boot, Spring Security, JWT, JPA/Hibernate, and MySQL.

The application provides secure authentication, role-based access control, resource management, and reservation management for ADMIN and USER roles.

## Features

- JWT-based authentication
- BCrypt password hashing
- Stateless JWT security
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
- Environment-based configuration
- Development-only test user initialization

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