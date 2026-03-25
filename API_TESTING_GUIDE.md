# Exam Portal API - Complete Testing Guide

## Overview
This document provides comprehensive API testing instructions for the Online Exam Portal, covering all endpoints with proper role-based access control (ADMIN, TEACHER, STUDENT).

---

## Setup Requirements

### 1. Start the Application
```bash
cd online-exam-portal
./mvnw spring-boot:run
```
Server runs on: `http://localhost:8080`

### 2. Get JWT Token
All protected endpoints require a Bearer token from authentication:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@example.com", "password":"admin123"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## Role-Based Users for Testing

### ADMIN
- Email: `admin@example.com`
- Password: `admin123`
- Permissions: All operations

### TEACHER
- Email: `teacher@example.com`
- Password: `teacher123`
- Permissions: Create/Update/Delete own exams, View students, Manage questions

### STUDENT
- Email: `student@example.com`
- Password: `student123`
- Permissions: Enroll in exams, View exam questions, Submit answers

---

## API Endpoints

### Authentication Endpoints

#### 1. Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin@example.com",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

---

### Subject Management Endpoints

#### 2. Get All Subjects
```bash
GET /api/exams/subjects
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "subjectName": "Java Programming",
    "description": "Core Java concepts"
  }
]
```

#### 3. Get Subject by ID
```bash
GET /api/exams/subjects/{id}
Authorization: Bearer <TOKEN>
```

#### 4. Create Subject (ADMIN/TEACHER only)
```bash
POST /api/exams/subjects
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "subjectName": "Machine Learning",
  "description": "Introduction to ML algorithms"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "subjectName": "Machine Learning",
  "description": "Introduction to ML algorithms"
}
```

**Expected Errors:**
- 403 Forbidden: If user is not ADMIN or TEACHER
- 400 Bad Request: If required fields missing

#### 5. Update Subject (ADMIN/TEACHER only)
```bash
PUT /api/exams/subjects/{id}
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "subjectName": "Advanced Machine Learning",
  "description": "Deep learning and neural networks"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "subjectName": "Advanced Machine Learning",
  "description": "Deep learning and neural networks"
}
```

#### 6. Partial Update Subject (ADMIN/TEACHER only)
```bash
PATCH /api/exams/subjects/{id}
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "description": "Updated description only"
}
```

#### 7. Delete Subject (ADMIN/TEACHER only)
```bash
DELETE /api/exams/subjects/{id}
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
"Subject deleted successfully. X linked exam(s) and Y linked question(s) were also deleted."
```

---

### Exam Management Endpoints

#### 8. Get All Exams
```bash
GET /api/exams
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "examTitle": "Java Basics Final",
    "examDate": "2026-04-15",
    "examTime": "10:00:00",
    "durationMinutes": 60,
    "examType": "MULTIPLE_CHOICE",
    "status": "SCHEDULED",
    "subjectId": 1,
    "subjectName": "Java Programming",
    "createdBy": 1
  }
]
```

#### 9. Get Exam by ID
```bash
GET /api/exams/{id}
Authorization: Bearer <TOKEN>
```

#### 10. Create Exam (ADMIN/TEACHER only)
```bash
POST /api/exams
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "examTitle": "Python Advanced",
  "examDate": "2026-04-20",
  "examTime": "14:00:00",
  "durationMinutes": 90,
  "subjectId": 1,
  "examType": "MIXED"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "examTitle": "Python Advanced",
  "examDate": "2026-04-20",
  "examTime": "14:00:00",
  "durationMinutes": 90,
  "examType": "MIXED",
  "status": "SCHEDULED",
  "subjectId": 1,
  "subjectName": "...",
  "createdBy": 1
}
```

#### 11. Update Exam (ADMIN/TEACHER only)
```bash
PUT /api/exams/{id}
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "examTitle": "Python Advanced Updated",
  "durationMinutes": 120
}
```

#### 12. Partial Update Exam (ADMIN/TEACHER only)
```bash
PATCH /api/exams/{id}
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "examTitle": "New Title"
}
```

#### 13. Delete Exam (ADMIN/TEACHER only)
```bash
DELETE /api/exams/{id}
Authorization: Bearer <TOKEN>
```

---

### Question Management Endpoints

#### 14. Create Question (ADMIN/TEACHER only)
```bash
POST /api/questions
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "questionText": "What is Java?",
  "questionType": "MULTIPLE_CHOICE",
  "marks": 2,
  "examId": 1,
  "subjectId": 1
}
```

#### 15. Get Questions by Exam
```bash
GET /api/questions/exam/{examId}
Authorization: Bearer <TOKEN>
```

#### 16. Update Question (ADMIN/TEACHER only)
```bash
PUT /api/questions/{id}
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "questionText": "Updated question",
  "marks": 3
}
```

#### 17. Delete Question (ADMIN/TEACHER only)
```bash
DELETE /api/questions/{id}
Authorization: Bearer <TOKEN>
```

---

### Exam Enrollment & Conduct Endpoints

#### 18. Enroll in Exam (STUDENT)
```bash
POST /exam/enroll?examId=1&userId=3
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
"Enrollment successful"
```

#### 19. Get Exam Questions (STUDENT - must be enrolled)
```bash
GET /exam/questions/{examId}/{userId}
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "questionId": 1,
    "questionText": "What is OOP?",
    "questionType": "MULTIPLE_CHOICE",
    "marks": 2,
    "options": [
      {
        "optionId": 1,
        "optionText": "Object Oriented Programming"
      },
      {
        "optionId": 2,
        "optionText": "Open Online Platform"
      }
    ]
  }
]
```

#### 20. Get Remaining Exam Time
```bash
GET /exam/timmer/{examId}
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
45
```
(Returns remaining minutes)

#### 21. Submit Answer (STUDENT)
```bash
POST /answers/submit
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "examId": 1,
  "userId": 3,
  "questionId": 1,
  "selectedOptionId": 1
}
```

**Response (200 OK):**
```json
"Answer saved"
```

#### 22. Finish Exam & Get Results (STUDENT)
```bash
POST /answers/finish?examId=1&userId=3
Authorization: Bearer <TOKEN>
```

**Response (200 OK):**
```json
{
  "id": 1,
  "examId": 1,
  "userId": 3,
  "score": 8,
  "grade": "A",
  "evaluatedAt": "2026-03-25T14:30:00"
}
```

---

### User Management Endpoints

#### 23. Get User Profile
```bash
GET /api/users/profile
Authorization: Bearer <TOKEN>
```

#### 24. Update Own Profile
```bash
PUT /api/users/update-profile
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "username": "New Name"
}
```

#### 25. Change Password
```bash
POST /api/users/change-password
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "oldPassword": "current123",
  "newPassword": "newpass456"
}
```

#### 26. Get All Users (ADMIN only)
```bash
GET /api/users/all
Authorization: Bearer <ADMIN_TOKEN>
```

#### 27. Get All Students (TEACHER only)
```bash
GET /api/users/students
Authorization: Bearer <TEACHER_TOKEN>
```

#### 28. Get User by ID (ADMIN/TEACHER)
```bash
GET /api/users/{id}
Authorization: Bearer <TOKEN>
```

#### 29. Update User (ADMIN only)
```bash
PUT /api/users/{id}
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "username": "Updated Name",
  "email": "newemail@example.com",
  "role": "TEACHER"
}
```

#### 30. Delete User (ADMIN only)
```bash
DELETE /api/users/{id}
Authorization: Bearer <ADMIN_TOKEN>
```

---

## Postman Collection

Import this into Postman for easy testing:

```json
{
  "info": {
    "name": "Online Exam Portal API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/auth/login"
          }
        }
      ]
    },
    {
      "name": "Subjects",
      "item": [
        {
          "name": "Get All",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/exams/subjects"
          }
        },
        {
          "name": "Create",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/exams/subjects"
          }
        }
      ]
    }
  ]
}
```

---

## Common Issues & Solutions

### 403 Forbidden on POST /api/exams/subjects
**Problem:** "Forbidden" response when creating subjects
**Solution:** 
- Ensure you're using ADMIN or TEACHER token
- Check Authorization header: `Authorization: Bearer <TOKEN>`
- Verify user role in database

### PUT/PATCH Returns 403
**Problem:** Update endpoints return Forbidden
**Solution:**
- Use correct HTTP method (PUT for full, PATCH for partial)
- Ensure user has ADMIN or TEACHER role
- Verify authentication token is valid

### Subject Update Not Affecting Exams
**Problem:** Updating subject name doesn't update related exams
**Solution:**
- The relationship is maintained through foreign key
- Exams always reference the current subject data
- No manual cascade update needed

### Exam Status Not Updating
**Problem:** Exam status stays "SCHEDULED" even after start time
**Solution:**
- Status is calculated dynamically based on current time
- Check server timezone: it should match your local timezone
- Status updates: SCHEDULED → ONGOING → COMPLETED

### PATCH Returns 405 Method Not Allowed
**Problem:** PATCH requests fail with 405 error
**Solution:**
- Ensure your HTTP client supports PATCH method
- Verify Spring Boot is configured to handle PATCH
- Try using PUT as alternative (full update)

---

## Role-Based Access Control Summary

| Endpoint | ADMIN | TEACHER | STUDENT |
|----------|-------|---------|---------|
| POST /api/exams/subjects | ✓ | ✓ | ✗ |
| PUT /api/exams/subjects/{id} | ✓ | ✓ | ✗ |
| PATCH /api/exams/subjects/{id} | ✓ | ✓ | ✗ |
| DELETE /api/exams/subjects/{id} | ✓ | ✓ | ✗ |
| POST /api/exams | ✓ | ✓ | ✗ |
| PUT /api/exams/{id} | ✓ | ✓ (own) | ✗ |
| PATCH /api/exams/{id} | ✓ | ✓ (own) | ✗ |
| DELETE /api/exams/{id} | ✓ | ✓ (own) | ✗ |
| GET /exam/enroll | ✓ | ✓ | ✓ |
| POST /answers/submit | ✓ | ✓ | ✓ |
| GET /api/users/all | ✓ | ✗ | ✗ |
| GET /api/users/students | ✓ | ✓ | ✗ |
| GET /api/users/{id} | ✓ | ✓ (students only) | ✗ |

---

## Testing Checklist

- [ ] Login with ADMIN credentials
- [ ] Login with TEACHER credentials
- [ ] Login with STUDENT credentials
- [ ] Create subject as TEACHER (should succeed)
- [ ] Create subject as STUDENT (should fail with 403)
- [ ] Create exam as ADMIN
- [ ] Create exam as TEACHER
- [ ] Update exam as creating TEACHER (should succeed)
- [ ] Delete exam as non-creating TEACHER (should fail)
- [ ] Enroll STUDENT in exam
- [ ] Get exam questions as enrolled STUDENT
- [ ] Submit exam answer as STUDENT
- [ ] Finish exam and verify grading
- [ ] Get student list as TEACHER
- [ ] Update user as ADMIN
- [ ] Try to delete ADMIN user as STUDENT (should fail)

---

## Troubleshooting

### Check Server Logs
```bash
tail -f online-exam-portal/app.log
```

### Verify Database Connection
```bash
curl http://localhost:8080/api/exams
```

### Check JWT Token Validity
Use an online JWT decoder: https://jwt.io/

### Enable Debug Logging
Update `application.properties`:
```properties
logging.level.root=INFO
logging.level.com.springboot.online_exam_portal=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

## Performance Notes

- Subject updates are transactional and immediately visible to all linked exams
- Exam status is computed on-the-fly (not stored)
- Exam enrollment tracks attempted exams
- Answer evaluation happens at exam finish time

---

## Version Info
- Spring Boot: 3.x
- Java: 17+
- Database: MySQL/PostgreSQL
- API Version: 1.0


