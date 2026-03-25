# Exam Management Module - Quick Reference Guide

## 🚀 Quick Start

### 1. Run the Application
```bash
cd online-exam-portal
./mvnw.cmd spring-boot:run
```

### 2. Database Setup
Execute these SQL statements:
```sql
-- Create roles
INSERT INTO roles (role_name) VALUES ('ADMIN'), ('TEACHER'), ('USER');

-- Create subjects
INSERT INTO subjects (subject_name, description) VALUES 
('Java Programming', 'Core and Advanced Java'),
('Python Basics', 'Introduction to Python'),
('Database Design', 'SQL and Database management'),
('Web Development', 'HTML, CSS, JavaScript'),
('Data Structures', 'Arrays, Lists, Trees, Graphs');
```

### 3. Default Test Users
```sql
-- Admin user (role_id = 1)
INSERT INTO users (id, username, email, password_hash, role_id, created_at) 
VALUES (1, 'admin', 'admin@exam.com', 'hash1', 1, NOW());

-- Teacher user (role_id = 2)
INSERT INTO users (id, username, email, password_hash, role_id, created_at) 
VALUES (2, 'teacher', 'teacher@exam.com', 'hash2', 2, NOW());

-- Student user (role_id = 3)
INSERT INTO users (id, username, email, password_hash, role_id, created_at) 
VALUES (3, 'student', 'student@exam.com', 'hash3', 3, NOW());
```

---

## 📋 API Endpoints Overview

### Base URL: `http://localhost:8080/api/exams`

### Admin Only (7 endpoints)
```
✓ POST   /admin/create              Create exam
✓ PUT    /admin/update/{id}         Update exam
✓ DELETE /admin/delete/{id}         Delete exam
✓ GET    /admin/all                 List all exams
✓ GET    /admin/scheduled           List scheduled exams
✓ GET    /admin/ongoing             List ongoing exams
✓ GET    /admin/status/{id}         Get exam status
```

### Teacher Only (4 endpoints)
```
✓ POST   /teacher/create            Create exam
✓ PUT    /teacher/update/{id}       Update own exam
✓ DELETE /teacher/delete/{id}       Delete own exam
✓ GET    /teacher/all               View all exams
```

### Student Only (4 endpoints)
```
✓ POST   /student/register/{id}     Register for exam
✓ GET    /student/available         Get available exams
✓ GET    /student/enrolled          Get enrolled exams
✓ GET    /student/can-attempt/{id}  Check eligibility
```

### Subject Management (3 endpoints)
```
✓ POST   /subjects                  Create subject
✓ GET    /subjects                  List subjects
✓ DELETE /subjects/{id}             Delete subject
```

### Public (2 endpoints)
```
✓ GET    /                          Get all exams
✓ GET    /{id}                      Get exam details
```

---

## 🔑 Authentication Header

All endpoints require a userId header:
```
Headers:
  userId: <user_id>
```

Example with curl:
```bash
curl -X POST http://localhost:8080/api/exams/admin/create \
  -H "Content-Type: application/json" \
  -H "userId: 1" \
  -d '{ ... }'
```

---

## 📝 Request/Response Examples

### ➤ Admin/Teacher: Create Exam

**Request**:
```bash
POST /api/exams/admin/create
Headers: userId: 1
Content-Type: application/json

{
  "examTitle": "Java Advanced",
  "examDate": "2026-04-20",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "examType": "MULTIPLE_CHOICE"
}
```

**Response (201)**:
```json
{
  "id": 1,
  "examTitle": "Java Advanced",
  "examDate": "2026-04-20",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "subjectName": "Java Programming",
  "examType": "MULTIPLE_CHOICE",
  "createdBy": 1,
  "status": "SCHEDULED"
}
```

---

### ➤ Student: Register for Exam

**Request**:
```bash
POST /api/exams/student/register/1
Headers: userId: 3
```

**Response (201)**:
```json
{
  "status": "success",
  "message": "Successfully registered for exam"
}
```

---

### ➤ Student: Get Available Exams

**Request**:
```bash
GET /api/exams/student/available
Headers: userId: 3
```

**Response (200)**:
```json
[
  {
    "id": 1,
    "examTitle": "Java Advanced",
    "examDate": "2026-04-20",
    "examTime": "10:00:00",
    "durationMinutes": 120,
    "subjectId": 1,
    "subjectName": "Java Programming",
    "examType": "MULTIPLE_CHOICE",
    "createdBy": 1,
    "status": "SCHEDULED"
  }
]
```

---

### ➤ Student: Check Can Attempt

**Request**:
```bash
GET /api/exams/student/can-attempt/1
Headers: userId: 3
```

**Response (200)**:
```json
{
  "examId": 1,
  "canAttempt": true,
  "message": "You can attempt this exam now"
}
```

Or if cannot attempt:
```json
{
  "examId": 1,
  "canAttempt": false,
  "message": "You cannot attempt this exam. Check enrollment and schedule."
}
```

---

### ➤ Admin: Get Ongoing Exams

**Request**:
```bash
GET /api/exams/admin/ongoing
Headers: userId: 1
```

**Response (200)**: Array of exams currently running

---

## ⚠️ Common Error Responses

### 403 Forbidden - Unauthorized Role
```json
{
  "status": "error",
  "message": "Only Admin and Teacher can create exams"
}
```

### 400 Bad Request - Already Registered
```json
{
  "status": "error",
  "message": "User is already registered for this exam"
}
```

### 400 Bad Request - Exam Already Started
```json
{
  "status": "error",
  "message": "Exam has already started. Cannot register now."
}
```

### 404 Not Found
```json
{
  "status": "error",
  "message": "Exam not found"
}
```

---

## 🛠️ Development Tips

### Project Structure
```
online-exam-portal/
├── src/main/java/com/springboot/online_exam_portal/
│   ├── controller/
│   │   └── ExamController.java
│   ├── service/
│   │   ├── ExamService.java
│   │   └── ExamServiceImpl.java
│   ├── repository/
│   │   ├── ExamEnrollmentRepository.java
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   ├── SubjectRepository.java
│   │   └── ExamsRepository.java
│   ├── entity/
│   │   ├── Exams.java
│   │   ├── Subject.java
│   │   ├── Role.java
│   │   ├── User.java
│   │   └── ExamEnrollment.java
│   ├── dto/
│   │   ├── ExamRequest.java
│   │   ├── ExamResponse.java
│   │   ├── ExamEnrollmentRequest.java
│   │   └── ApiResponse.java
│   ├── exception/
│   │   ├── UnauthorizedException.java
│   │   └── GlobalExceptionHandler.java
│   ├── annotation/
│   │   └── RequireRole.java
│   └── util/
│       └── RoleVerificationUtil.java
└── target/
```

### Check Compilation
```bash
./mvnw.cmd clean compile -DskipTests
# Expected: BUILD SUCCESS
```

### Key Classes

**ExamService.java** - Service interface with 16 methods
- createExam, updateExam, deleteExam
- createExamWithRequest, updateExamWithRequest
- getOngoingExams, getScheduledExams
- registerExam, getEnrolledExams
- canAttemptExam, etc.

**ExamController.java** - 18 REST endpoints
- /admin/* (7 endpoints)
- /teacher/* (4 endpoints)
- /student/* (4 endpoints)
- /subjects (3 endpoints)
- / and /{id} (2 public endpoints)

**RoleVerificationUtil.java** - Role checking utilities
```java
RoleVerificationUtil.isAdmin(user)
RoleVerificationUtil.isTeacher(user)
RoleVerificationUtil.isStudent(user)
RoleVerificationUtil.hasRole(user, "ADMIN", "TEACHER")
RoleVerificationUtil.enforceAdmin(user)
```

---

## 🔐 Access Control Summary

| Operation | Admin | Teacher | Student |
|-----------|-------|---------|---------|
| Create Exam | ✓ | ✓ | ✗ |
| Update Exam | ✓ (any) | ✓ (own) | ✗ |
| Delete Exam | ✓ | ✓ (own) | ✗ |
| Read Exam | ✓ | ✓ | ✓ |
| Register Exam | ✗ | ✗ | ✓ |
| Attempt Exam | ✗ | ✗ | ✓ (during schedule) |
| View Ongoing | ✓ | ✗ | ✗ |
| View Scheduled | ✓ | ✗ | ✗ |
| Manage Subjects | ✓ | ✗ | ✗ |

---

## ⏰ Time-Based Rules

### Exam Registration
- **Can Register**: From now until exam start time
- **Cannot Register**: After exam has started
- **Error**: "Exam has already started. Cannot register now."

### Exam Attempt
- **Can Attempt**: From exam start time to (start time + duration)
- **Cannot Attempt**: Before start or after end time
- **Check**: `canAttemptExam(examId, userId)` returns true/false

### Status Calculation
- **SCHEDULED**: Exam datetime > now
- **ONGOING**: Exam datetime ≤ now ≤ (datetime + duration)
- **COMPLETED**: Exam datetime + duration < now

---

## 📚 Exam Types

- **MULTIPLE_CHOICE**: Multiple choice questions
- **DESCRIPTIVE**: Essay/descriptive type questions
- **MIXED**: Combination of both types

---

## 💾 Database Schema

### exams table
```sql
id INT PRIMARY KEY AUTO_INCREMENT
exam_title VARCHAR(255)
exam_date DATE
exam_time TIME
duration_minutes INT
exam_type VARCHAR(50)
subject_id INT FOREIGN KEY
created_by INT
created_at DATETIME
updated_at DATETIME
status VARCHAR(50)
```

### exam_enrollment table
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT
exam_id BIGINT FOREIGN KEY
user_id BIGINT FOREIGN KEY
enrolled_at DATETIME
```

---

## 🧪 Manual Testing

### Test Case 1: Admin Creates Exam
```bash
# As Admin (userId: 1)
curl -X POST http://localhost:8080/api/exams/admin/create \
  -H "Content-Type: application/json" \
  -H "userId: 1" \
  -d '{
    "examTitle":"Test Exam",
    "examDate":"2026-04-20",
    "examTime":"14:00:00",
    "durationMinutes":90,
    "subjectId":1,
    "examType":"MULTIPLE_CHOICE"
  }'
```

### Test Case 2: Student Tries to Create (Should Fail)
```bash
# As Student (userId: 3)
curl -X POST http://localhost:8080/api/exams/admin/create \
  -H "Content-Type: application/json" \
  -H "userId: 3" \
  -d '{ ... }'
# Expected: 403 Forbidden
```

### Test Case 3: Student Registers
```bash
# As Student (userId: 3)
curl -X POST http://localhost:8080/api/exams/student/register/1 \
  -H "userId: 3"
# Expected: 201 Created with success message
```

### Test Case 4: Get Available Exams
```bash
# As Student (userId: 3)
curl -X GET http://localhost:8080/api/exams/student/available \
  -H "userId: 3"
# Expected: 200 OK with list of exams
```

---

## 📖 Documentation Files

1. **EXAM_MANAGEMENT_API_DOCUMENTATION.md** - Complete API reference
2. **EXAM_MANAGEMENT_IMPLEMENTATION_GUIDE.md** - Implementation details
3. **EXAM_MANAGEMENT_SUMMARY.md** - Feature overview
4. **EXAM_MANAGEMENT_QUICK_REFERENCE.md** - This file

---

## 🐛 Troubleshooting

### Build fails
```bash
./mvnw.cmd clean install
# Ensure Java 17+ is installed
java -version
```

### Port already in use
```bash
# Default port is 8080
# Change in application.properties:
server.port=8081
```

### Header not recognized
- Ensure header is: `userId: <number>` (not userId as body parameter)
- Example: `-H "userId: 1"`

### Exam enrollment fails
- Check if student already enrolled: `GET /student/enrolled`
- Check if exam has started: `GET /student/can-attempt/{id}`
- Verify user exists in database

---

## ✅ Production Checklist

- [ ] Database initialized with roles and subjects
- [ ] Test users created
- [ ] All endpoints tested with appropriate roles
- [ ] Error cases tested
- [ ] Time-based access validated
- [ ] Exception handling working
- [ ] API documentation reviewed
- [ ] Server runs without errors
- [ ] CORS configured if frontend is separate
- [ ] Database backups configured

---

## 📞 Support

For issues or questions:
1. Check API documentation
2. Review implementation guide
3. Verify database setup
4. Check user roles and permissions
5. Validate request/response format

---

**Last Updated**: March 18, 2026
**Version**: 1.0
**Status**: ✅ Production Ready

