# Task Management API

A RESTful API for managing tasks and projects with JWT authentication, built with Spring Boot and PostgreSQL.

## Features

- **JWT Authentication** - Secure user registration and login
- **Task Management** - Create, read, update, delete tasks with full CRUD operations
- **Project Organization** - Group tasks into projects with Many-to-One relationships
- **Advanced Filtering** - Filter tasks by status, priority, and project
- **Pagination & Sorting** - Efficient data retrieval with customizable page sizes and sorting
- **Input Validation** - Comprehensive request validation with detailed error messages
- **DTO Pattern** - Clean separation between API contracts and database entities

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.0**
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Spring Validation
- **PostgreSQL** - Relational database
- **JWT (jjwt)** - Token-based authentication
- **Maven** - Dependency management

## Prerequisites

- JDK 21 or higher
- PostgreSQL 15 or higher
- Maven 3.6+ (or use included Maven wrapper)
- Postman (for API testing)

## Database Setup

1. **Create PostgreSQL database:**
```sql
CREATE DATABASE taskdb;
```

2. **Update database credentials:**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Tables are auto-created** on first run via JPA (Hibernate DDL auto)

## Running the Application

**Using Maven wrapper (recommended):**
```bash
./mvnw spring-boot:run
```

**Or using installed Maven:**
```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### Project Endpoints

All project endpoints require JWT authentication via `Authorization: Bearer {token}` header.

#### Create Project
```http
POST /api/projects
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Website Redesign",
  "description": "Q1 2025 website overhaul"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Website Redesign",
  "description": "Q1 2025 website overhaul",
  "tasks": [],
  "createdAt": "2024-12-05T10:30:00",
  "updatedAt": "2024-12-05T10:30:00"
}
```

#### Get All Projects
```http
GET /api/projects
Authorization: Bearer {token}
```

#### Get Project by ID
```http
GET /api/projects/{id}
Authorization: Bearer {token}
```

#### Update Project
```http
PUT /api/projects/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Project Name",
  "description": "Updated description"
}
```

#### Delete Project
```http
DELETE /api/projects/{id}
Authorization: Bearer {token}
```

**Response:** `204 No Content`

---

### Task Endpoints

All task endpoints require JWT authentication.

#### Create Task
```http
POST /api/tasks
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Design homepage mockup",
  "description": "Create Figma mockups for new homepage",
  "status": "TODO",
  "priority": "HIGH",
  "projectId": 1
}
```

**Field Constraints:**
- `title`: Required, 1-100 characters
- `description`: Optional, max 500 characters
- `status`: Required, one of: `TODO`, `IN_PROGRESS`, `DONE`
- `priority`: Required, one of: `LOW`, `MEDIUM`, `HIGH`
- `projectId`: Optional (task can exist without a project)

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Design homepage mockup",
  "description": "Create Figma mockups for new homepage",
  "status": "TODO",
  "priority": "HIGH",
  "project": {
    "id": 1,
    "name": "Website Redesign",
    ...
  },
  "createdAt": "2024-12-05T10:35:00",
  "updatedAt": "2024-12-05T10:35:00"
}
```

#### Get All Tasks (with Filtering, Pagination, Sorting)
```http
GET /api/tasks?status=IN_PROGRESS&priority=HIGH&projectId=1&page=0&size=10&sortBy=priority&sortDir=desc
Authorization: Bearer {token}
```

**Query Parameters (all optional):**
- `status` - Filter by status (`TODO`, `IN_PROGRESS`, `DONE`)
- `priority` - Filter by priority (`LOW`, `MEDIUM`, `HIGH`)
- `projectId` - Filter by project ID
- `page` - Page number (default: `0`)
- `size` - Items per page (default: `10`)
- `sortBy` - Sort field (default: `id`) - options: `id`, `title`, `status`, `priority`, `createdAt`, `updatedAt`
- `sortDir` - Sort direction (default: `asc`) - options: `asc`, `desc`

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Design homepage mockup",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    ...
  },
  "totalElements": 25,
  "totalPages": 3,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

#### Get Task by ID
```http
GET /api/tasks/{id}
Authorization: Bearer {token}
```

#### Update Task
```http
PUT /api/tasks/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "projectId": 1
}
```

**Note:** All fields are required in update request.

#### Delete Task
```http
DELETE /api/tasks/{id}
Authorization: Bearer {token}
```

**Response:** `204 No Content`

---

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "title": "must not be blank",
  "status": "must not be null"
}
```

### Unauthorized (403 Forbidden)
```json
{
  "error": "Forbidden",
  "message": "Access Denied"
}
```

### Resource Not Found (404 Not Found)
```json
{
  "error": "Not Found",
  "message": "Task not found with id: 999"
}
```

---

## Project Structure
```
src/main/java/com/taskmanager/
├── config/              # Configuration classes
│   └── SecurityConfig.java
├── controller/          # REST API endpoints
│   ├── AuthController.java
│   ├── ProjectController.java
│   └── TaskController.java
├── dto/                 # Data Transfer Objects
│   ├── CreateTaskRequest.java
│   ├── UpdateTaskRequest.java
│   ├── LoginRequest.java
│   └── AuthResponse.java
├── exception/           # Custom exceptions and error handling
│   ├── GlobalExceptionHandler.java
│   ├── TaskNotFoundException.java
│   └── ErrorResponse.java
├── model/               # JPA entities
│   ├── Task.java
│   ├── Project.java
│   ├── User.java
│   ├── Status.java (enum)
│   └── Priority.java (enum)
├── repository/          # Data access layer
│   ├── TaskRepository.java
│   ├── ProjectRepository.java
│   └── UserRepository.java
├── security/            # JWT and security components
│   ├── JwtAuthenticationFilter.java
│   └── JwtUtil.java
└── service/             # Business logic layer
    ├── TaskService.java
    ├── ProjectService.java
    └── AuthService.java
```

---

## Architecture

The application follows a **3-tier layered architecture**:

1. **Controller Layer** - Handles HTTP requests, uses DTOs for request/response
2. **Service Layer** - Contains business logic, validates data, manages transactions
3. **Repository Layer** - Data access using Spring Data JPA

**Key Design Patterns:**
- **DTO Pattern** - Separates API contracts from database entities
- **Repository Pattern** - Abstracts data access
- **Dependency Injection** - Spring manages component lifecycle

---

## Known Issues

### Priority Sorting
- **Issue:** Priority enum sorts alphabetically (HIGH, LOW, MEDIUM) instead of logically (HIGH, MEDIUM, LOW)
- **Workaround:** Use `sortBy=priority&sortDir=desc` for HIGH→MEDIUM→LOW order
- **Future Fix:** Store priority as integer in database (requires migration)

---

## Testing

### Postman Collection
Import the included Postman collection for comprehensive API testing:
- `Task-Management-API.postman_collection.json`
- `Task-API-Local.postman_environment.json`

### Test Coverage
- ✅ CRUD operations for tasks and projects
- ✅ JWT authentication flow
- ✅ Filtering by status, priority, and project
- ✅ Pagination and sorting
- ✅ Validation error handling
- ✅ Edge cases (non-existent IDs, missing auth, etc.)

---

## Future Enhancements

- [ ] Task due dates and reminders
- [ ] Task comments and activity history
- [ ] File attachments for tasks
- [ ] Task assignments to multiple users
- [ ] Email notifications
- [ ] Task templates
- [ ] Search functionality (full-text search)
- [ ] API rate limiting
- [ ] Swagger/OpenAPI documentation
- [ ] Unit and integration tests
- [ ] Docker containerization

---

## Development Timeline

**Week 1 (Nov 25-29):** Basic CRUD, validation, error handling, enums, timestamps  
**Week 2 (Dec 2-6):** Filtering, pagination, JWT authentication, projects feature, documentation  
**Status:** ✅ Complete and deployed

---

## Author

**Woo Seok** | [GitHub: @Froderic](https://github.com/Froderic)  
Backend Developer | Career Transition Project  
December 2025

---

## License

This project is for educational and portfolio purposes.
