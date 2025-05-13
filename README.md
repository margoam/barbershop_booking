# Barber Booking System

![Barber Booking System](https://img.shields.io/badge/status-active-success.svg)
![Java](https://img.shields.io/badge/language-Java-blue.svg)
![Spring Boot](https://img.shields.io/badge/framework-Spring%20Boot-green.svg)
![PostgreSQL](https://img.shields.io/badge/database-PostgreSQL-informational.svg)

A comprehensive Barber Booking System API that allows users to book appointments with barbers, manage services, and handle scheduling.

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Setup](#setup)

## Features

- **User Management**: Register, authenticate, and manage user accounts with different roles
- **Barber Operations**: CRUD operations for barbers including ratings and contact information
- **Service Management**: Create and manage barber services with pricing and duration
- **Schedule Management**: Handle barber availability and time slots
- **Booking System**: Complete booking workflow with appointment tracking
- **Authentication**: Secure JWT-based authentication system

## Technologies

- **Backend**: Java 21, Spring Boot 3.x
- **Database**: PostgreSQL
- **API Documentation**: Postman Collection
- **Build Tool**: Maven
- **Authentication**: JWT (JSON Web Tokens)

## Database Schema

The system uses the following database structure:

Key tables:
- `user` - Stores user information and credentials
- `barber` - Contains barber profiles
- `service` - Lists available barber services
- `booking` - Manages appointment records
- `barber_schedule` - Tracks barber availability

See full [DDL script](docker/init/db_ddl.sql) in the repository.

## API Endpoints

### User Management
- `POST /api/users/create` - Register new user
- `POST /api/users/admin` - Admin create user (admin only)
- `GET /api/users/all` - Get all users (admin only)
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Barber Operations
- `GET /api/barbers/all` - Get all barbers
- `GET /api/barbers/{id}` - Get barber by ID
- `POST /api/barbers/create` - Create new barber
- `PUT /api/barbers/{id}` - Update barber
- `DELETE /api/barbers/{id}` - Delete barber

### Service Management
- `GET /api/services/all` - Get all services
- `POST /api/services/create` - Create new service
- `PUT /api/services/{id}` - Update service
- `DELETE /api/services/{id}` - Delete service

### Schedule Management
- `GET /api/schedules/all` - Get all schedules
- `GET /api/schedules/available-slots` - Get available time slots
- `POST /api/schedules/create` - Create schedule
- `PUT /api/schedules/{id}` - Update schedule
- `DELETE /api/schedules/{id}` - Delete schedule

### Booking Operations
- `GET /api/bookings/all` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `POST /api/bookings/create` - Create booking
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Delete booking

### Authentication
- `POST /api/auth/login` - User login (returns JWT token)

### Test users
- Admin - test@test.com, password = 1234567891011

## Swagger and actuator usage

- Swagger is available http://localhost:8080/swagger-ui/index.html
- Actuator is available http://localhost:8080/actuator
- Creds: admin/admin

## Testing

- Postman collection is available here [collection](TMS_Booking_Project/src/main/resources/Barber_Booking_Diploma.postman_collection.json)

## Setup

This project comes with Docker support for easy deployment and development. Follow these steps to set up the system using Docker:

### Prerequisites
- Docker Engine 20.10+
- Docker Compose 1.29+
- JDK 21 (only needed if developing outside Docker)

### Quick Start
1. Clone the repository:
   ```bash
   git clone https://github.com/margoam/barbershop_booking.git
   cd barbershop_booking
2. Build and start the containers:
    ```bash
   docker-compose up -d
3. The application will be available at:
   http://localhost:8080

Enjoy!
