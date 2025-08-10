# AnyMeal Backend

A Spring Boot REST API backend for a meal planning and recipe management application. This university project provides comprehensive meal planning functionality with user authentication, recipe management, favorites, and shopping list generation.

## Main Features

- **User Authentication & Authorization**: JWT-based secure login and registration system
- **Recipe Management**: Browse and view detailed recipe information with search functionality
- **Meal Planning**: Create and manage weekly meal plans with customizable daily entries
- **Favorites System**: Save and manage favorite recipes for quick access
- **Shopping List Generation**: Automatically generate shopping lists from meal plans and manage shopping items
- **User Profile Management**: Update user profiles and change passwords securely

## Technologies Used

- **Java 24**: Latest Java version for modern language features
- **Spring Boot 3.5.3**: Main framework for REST API development
- **Spring Security**: Authentication and authorization with JWT tokens
- **Spring Data JPA**: Database operations and ORM mapping
- **Hibernate**: JPA implementation with MySQL dialect
- **MySQL**: Primary relational database
- **JWT (JSON Web Tokens)**: Stateless authentication mechanism
- **Lombok**: Reduces boilerplate code in models and DTOs
- **Maven**: Dependency management and build automation

## Architecture & Folder Structure

The project follows a standard Spring Boot layered architecture:

```
src/main/java/com/anymeal/backend/
├── config/          # Security, JWT, and application configuration
├── controller/      # REST API endpoints and request handling
├── dto/            # Data Transfer Objects for API communication
├── model/          # JPA entities representing database tables
├── repository/     # Data access layer with JPA repositories
└── service/        # Business logic and service layer
```

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic and coordinates between controllers and repositories
- **Repository Layer**: Manages database operations using Spring Data JPA
- **Model Layer**: Defines entity classes that map to database tables
- **DTO Layer**: Provides structured data objects for API communication

## API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Authentication** |
| POST | `/auth/login` | User login with credentials |
| POST | `/auth/register` | New user registration |
| **User Management** |
| GET | `/api/v1/user/profile` | Get current user profile |
| PUT | `/api/v1/user/profile` | Update user profile |
| PUT | `/api/v1/user/change-password` | Change user password |
| **Recipes** |
| GET | `/api/v1/recipes` | Search recipes (with optional query parameter) |
| GET | `/api/v1/recipes/{id}` | Get detailed recipe information |
| **Favorites** |
| GET | `/api/v1/favorites` | Get user's favorite recipes |
| POST | `/api/v1/favorites` | Add recipe to favorites |
| DELETE | `/api/v1/favorites/{recipeId}` | Remove recipe from favorites |
| **Meal Planning** |
| GET | `/api/v1/plans` | Get weekly meal plan (with startDate parameter) |
| POST | `/api/v1/plans` | Add recipe to meal plan |
| DELETE | `/api/v1/plans/{planId}` | Remove recipe from meal plan |
| PUT | `/api/v1/plans/{planId}/notes` | Update meal plan notes |
| **Shopping List** |
| POST | `/api/v1/shopping-list/generate` | Generate shopping list from meal plan |
| GET | `/api/v1/shopping-list` | Get current shopping list |
| POST | `/api/v1/shopping-list/items` | Add item to shopping list |
| PUT | `/api/v1/shopping-list/items/{itemId}` | Update shopping list item |
| DELETE | `/api/v1/shopping-list/clear-purchased` | Clear purchased items |

## Installation & Running

### Prerequisites
- Java 24 or higher
- Maven 3.6+
- MySQL 8.0+
- XAMPP (recommended) or standalone MySQL server

### Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd anymeal-backend
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure database** (see Database Setup section)

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The server will start on `http://localhost:8080`

## Environment Variables

Create an `application.properties` file or set the following environment variables:

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `server.port` | Server port | `8080` |
| `spring.datasource.url` | Database URL | `jdbc:mysql://localhost:3306/anymeal_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `spring.datasource.username` | Database username | `root` |
| `spring.datasource.password` | Database password | *(empty)* |
| `jwt.secret` | JWT signing secret | *(provided in config)* |
| `jwt.expiration.ms` | JWT expiration time | `86400000` (24 hours) |

## Database Setup

### Using XAMPP (Recommended)

1. **Start XAMPP**
   - Start Apache and MySQL services

2. **Create Database**
   - Open phpMyAdmin (`http://localhost/phpmyadmin`)
   - Create a new database named `anymeal_db`

3. **Automatic Table Creation**
   - Tables will be created automatically when you run the application
   - The app uses `spring.jpa.hibernate.ddl-auto=update` for schema management

### Manual MySQL Setup

1. **Install MySQL** and start the service

2. **Create Database**
   ```sql
   CREATE DATABASE anymeal_db;
   ```

3. **Update Configuration**
   - Modify `application.properties` with your MySQL credentials
   - Ensure the database URL points to your MySQL instance

### Database Schema

The application automatically creates the following main tables:
- `users` - User accounts and authentication
- `recipes` - Recipe information
- `daily_plans` - User meal plans
- `plan_entries` - Individual plan entries
- `favorite_recipes` - User favorite recipes
- `shopping_list_items` - Shopping list items
- `ingredients` - Recipe ingredients

---

**Note**: This is a university project. For production use, ensure proper security configurations, environment variable management, and database security practices.
