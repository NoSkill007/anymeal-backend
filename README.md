# AnyMeal Backend

A Spring Boot REST API backend for a meal planning and recipe management application. This university project provides comprehensive meal planning functionality with user authentication, recipe management, favorites, and shopping list generation.

## Features

- **User Authentication & Authorization**: JWT-based authentication with secure login and registration
- **Recipe Management**: Browse and view detailed recipe information
- **Meal Planning**: Create and manage daily meal plans with customizable entries
- **Favorites System**: Save and manage favorite recipes
- **Shopping List**: Generate shopping lists from meal plans and manage shopping items
- **User Profile Management**: Update user profiles and change passwords

## Technologies Used

- **Java 24**: Latest Java version for modern language features
- **Spring Boot 3.5.3**: Main framework for REST API development
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations and ORM
- **Hibernate**: JPA implementation with MySQL dialect
- **MySQL**: Primary database
- **JWT (JSON Web Tokens)**: Stateless authentication
- **Lombok**: Reduces boilerplate code
- **Maven**: Dependency management and build tool

## API Documentation

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | User login |
| POST | `/auth/register` | User registration |

### User Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/user/profile` | Get user profile |
| PUT | `/api/v1/user/profile` | Update user profile |
| PUT | `/api/v1/user/password` | Change user password |

### Recipe Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/recipes` | Get all recipes |
| GET | `/api/v1/recipes/{id}` | Get recipe details |

### Meal Planning
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/plans` | Get user meal plans |
| POST | `/api/v1/plans` | Create new meal plan |
| DELETE | `/api/v1/plans/entries/{entryId}` | Delete plan entry |
| PUT | `/api/v1/plans/{planId}/notes` | Update plan notes |

### Favorites
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/favorites` | Get user favorites |
| POST | `/api/v1/favorites` | Add recipe to favorites |
| DELETE | `/api/v1/favorites/{recipeId}` | Remove from favorites |

### Shopping List
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/shopping-list` | Get shopping list |
| POST | `/api/v1/shopping-list` | Add shopping item |
| POST | `/api/v1/shopping-list/generate` | Generate list from plans |
| PUT | `/api/v1/shopping-list/{itemId}` | Update shopping item |
| PUT | `/api/v1/shopping-list/{itemId}/edit` | Edit shopping item |
| DELETE | `/api/v1/shopping-list/{itemId}` | Delete shopping item |
| POST | `/api/v1/shopping-list/clear-checked` | Clear checked items |

## Architecture & Folder Structure

```
src/main/java/com/anymeal/backend/
├── config/           # Security and application configuration
├── controller/       # REST API endpoints
├── dto/             # Data Transfer Objects for API requests/responses
├── model/           # JPA entities (database models)
├── repository/      # Data access layer (JPA repositories)
└── service/         # Business logic layer
```

### Key Components

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic and coordinate between controllers and repositories
- **Repositories**: Data access layer using Spring Data JPA
- **Models**: JPA entities representing database tables (User, Recipe, DailyPlan, etc.)
- **DTOs**: Request/response objects for API communication
- **Config**: Security configuration including JWT authentication filter

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
