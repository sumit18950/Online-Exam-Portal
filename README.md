# Online Exam Portal

A full-stack web application for automating the examination process in educational institutions. The system provides secure, role-based access for **Admin, Teacher, and Student** users and supports user management, subject and question management, exam conduction, and result management.

## 📌 Project Overview

The Online Exam Portal replaces traditional paper-based examinations with a centralized digital platform.

The application provides:

* Secure user registration and login
* JWT-based authentication
* Role-based authorization
* Subject and question management
* Exam creation and publishing
* Online exam attempts
* Automated result generation
* Role-based dashboards
* User and profile management
<img width="1530" height="904" alt="Screenshot 2026-07-01 172834" src="https://github.com/user-attachments/assets/24bd15f0-bb67-45e6-a33b-77f619557fc5" />
<img width="1654" height="882" alt="Screenshot 2026-07-01 170305" src="https://github.com/user-attachments/assets/5c3cfd13-76c2-4403-ab9d-8d5211133b4f" />

## 👥 User Roles

### Admin

* View and manage registered users
* Delete users
* Create Teacher and Admin accounts
* View system-wide results
* Manage overall platform operations

### Teacher

* Create and manage subjects
* Create, update and delete questions
* Manage question options
* Create and publish examinations
* Assign questions to examinations
* View results of managed examinations
* Update profile and password

### Student

* Register and log in
* View available examinations
* Attempt examinations
* Submit answers within the allowed time
* View personal results
* Update profile and password

## 🏗️ System Architecture

The application uses a layered Spring Boot architecture.

```text
                  React.js Frontend
                         |
                         | REST API / Axios
                         v
                Spring Security Layer
                         |
                     JwtFilter
                         |
                         v
                  Controller Layer
                         |
                         v
                   Service Layer
                         |
                         v
                 Repository Layer
                         |
                         v
                Hibernate / JPA
                         |
                         v
                   MySQL Database
```

### Backend Layers

* **Controller** — Handles HTTP requests and responses
* **Service** — Contains business logic
* **Repository** — Handles database operations
* **Entity** — Maps Java objects to database tables
* **DTO** — Transfers required data between client and backend
* **Security** — Handles JWT authentication and authorization
* **Config** — Contains application and security configuration

## 🔐 Authentication and Authorization

The application uses **Spring Security and JWT** for secure authentication.

### Login Flow

```text
User Login
    |
    v
AuthController
    |
    v
UserService
    |
    v
Validate Email & Password
    |
    v
BCrypt Password Verification
    |
    v
JwtUtil
    |
    v
Generate JWT Token
    |
    v
Token returned to Client
```

### Protected Request Flow

```text
Client Request
      |
      v
Authorization: Bearer <JWT>
      |
      v
JwtFilter
      |
      +--> Validate Token
      +--> Check Expiry
      +--> Verify Signature
      +--> Extract Email
      +--> Load User & Role
      |
      v
SecurityContext
      |
      v
Role Authorization
      |
      v
Controller
```

### Role-Based Authorization

The system uses:

```text
ROLE_ADMIN
ROLE_TEACHER
ROLE_STUDENT
```

Examples:

```text
/api/users/all              → ADMIN
/api/users/{id} DELETE     → ADMIN
/api/questions/**          → ADMIN, TEACHER
/api/exams/**              → ADMIN, TEACHER
/api/users/profile          → Authenticated users
```

## 🧩 Main Modules

### 1. Authentication & User Management

Responsibilities:

* Registration
* Login
* JWT generation
* Profile management
* Password change
* Role management
* Admin user management

### 2. Subject Management

Responsibilities:

* Create subjects
* Update subjects
* Delete subjects
* View subjects

### 3. Question Management

Responsibilities:

* Create questions
* Update questions
* Delete questions
* Manage options
* Maintain a reusable question bank

### 4. Exam Management

Responsibilities:

* Create exams
* Assign questions
* Configure subject, date and duration
* Publish exams
* Allow students to attempt exams

### 5. Result Management

Responsibilities:

* Calculate scores
* Generate grades
* Allow students to view their results
* Allow teachers to view results of their exams
* Allow administrators to monitor system-wide results

## 🗄️ Database

The application uses **MySQL** with **Spring Data JPA and Hibernate**.

The database is normalized to reduce redundancy and maintain data integrity.

### Main Tables

```text
roles
users
subjects
questions
options
exams
exam_questions
exam_enrollment
student_answers
results
score_review_history
```

### Important Relationships

```text
Role 1 -------- * User

Subject 1 ----- * Question

Question 1 ---- * Option

Exam * -------- * Question

User 1 -------- * Result
```

## 🛠️ Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* Maven

### Frontend

* React.js
* Axios
* React Router

### Database

* MySQL

### Development & Testing

* IntelliJ IDEA
* Visual Studio Code
* Postman
* Git
* GitHub

## 📁 Project Structure

### Backend

```text
server/
└── src/
    └── main/
        ├── java/
        │   └── com.springboot.online_exam_portal/
        │       ├── config/
        │       ├── controller/
        │       ├── dto/
        │       ├── entity/
        │       ├── repository/
        │       ├── security/
        │       └── service/
        │
        └── resources/
            └── application.properties
```

### Frontend

```text
client/
└── src/
    ├── components/
    ├── pages/
    ├── services/
    ├── utils/
    └── App.js
```

## 🌐 REST API Examples

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### User Management

```http
GET    /api/users/profile
PUT    /api/users/update-profile
POST   /api/users/change-password
GET    /api/users/all
DELETE /api/users/{id}
```

### Other Modules

```text
/api/subjects/**
/api/questions/**
/api/options/**
/api/exams/**
/results/**
```

Access to these endpoints is controlled according to the authenticated user's role.

## 🧪 API Testing

The backend APIs were tested using **Postman**.

Testing covered:

* Successful registration
* Duplicate email validation
* Successful login
* Invalid login credentials
* JWT generation
* JWT validation
* Profile access
* Profile update
* Password change
* Role-based access
* Unauthorized requests
* Admin-only operations

### Important HTTP Status Codes

| Status | Meaning                           |
| ------ | --------------------------------- |
| 200    | Request successful                |
| 201    | Resource created                  |
| 400    | Bad request                       |
| 401    | Authentication required/invalid   |
| 403    | Authenticated but not authorized  |
| 404    | Resource not found                |
| 409    | Conflict, such as duplicate email |

## 🔒 Security Features

* BCrypt password hashing
* JWT-based authentication
* Stateless session management
* Role-based authorization
* Protected REST endpoints
* CORS configuration
* Input validation
* Proper HTTP status codes

## 🎨 Frontend Features

The React frontend provides separate role-based dashboards.

### Admin Dashboard

* User management
* Teacher/Admin account creation
* System monitoring
* Result monitoring

### Teacher Dashboard

* Subject management
* Question bank management
* Exam creation and publishing
* Result viewing

### Student Dashboard

* Available exams
* Exam attempts
* Answer submission
* Result viewing
* Profile management

Protected routes prevent unauthorized users from accessing role-specific pages.

## 🚀 Running the Project

### Prerequisites

Install:

* Java 17
* Maven
* MySQL
* Node.js and npm
* IntelliJ IDEA / VS Code
* Git

### Clone Repository

```bash
git clone <repository-url>
cd online-exam-portal
```

## ▶️ Run Backend

Navigate to the server directory:

```bash
cd server
```

Configure MySQL details in:

```text
src/main/resources/application.properties
```

Example:

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/exam_portal
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=YOUR_JWT_SECRET
```

Run:

```bash
mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

## ▶️ Run Frontend

Open another terminal:

```bash
cd client
npm install
npm start
```

Frontend:

```text
http://localhost:3000
```

## 🔑 Example Login Request

```json
{
  "email": "student@example.com",
  "password": "password"
}
```

After successful authentication, the backend returns a JWT token.

The frontend sends it with protected requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

## 🧑‍💻 Development Methodology

The project followed an **Agile development approach**.

The team divided the system into modules and developed them incrementally.

Typical activities included:

* Requirement analysis
* Sprint planning
* Module development
* Daily team discussions
* API testing
* Integration
* Bug fixing
* Sprint review
* Retrospective

Git and GitHub were used for collaborative development and version control.

## ⚠️ Challenges Faced

### JWT Configuration

JWT dependencies had to be configured manually and the signing key had to be correctly configured.

### Spring Security

Several authentication and authorization issues were identified during development, particularly 401 and 403 responses.

### Role Mapping

Role names had to remain consistent between the database and Spring Security authorities.

### CORS

The React frontend and Spring Boot backend run on different ports during development, requiring proper CORS configuration.

### Database Integration

Entity relationships and foreign keys had to be carefully mapped using JPA/Hibernate.

### Team Integration

Git branches and merges were used to combine contributions from different team members.

## 🔮 Future Enhancements

Possible future improvements include:

* Admin approval workflow for Teacher registration
* Email verification
* OTP-based password reset
* Online proctoring
* Notification system
* Advanced analytics dashboard
* Cloud deployment using Google Cloud Platform
* Docker-based deployment
* Audit logging
* Enhanced examination monitoring



## 👨‍💻 Team Project

This project was developed as a team project during the Cognizant internship/project program.

Each team member contributed to different modules while following a common backend architecture and shared database design.

## 📄 License

This project was developed for educational and internship purposes.

## 👤 Author

**Sumit Kumar**
B.Tech Computer Science and Engineering
Lovely Professional University

---


