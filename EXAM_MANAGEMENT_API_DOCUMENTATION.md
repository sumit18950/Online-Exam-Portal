# Exam Management Module - API Documentation

## Overview
This document describes the Exam Management Module for the Online Exam Portal with three-level role-based access control:
- **ADMIN**: Full control over all exams and system management
- **TEACHER**: Create, read, update, and delete exams (own exams)
- **USER/STUDENT**: Register for exams and attempt exams within scheduled time

---

## Database Schema

### Tables Structure

#### 1. **subjects** table
```sql
id (PK)
subject_name (VARCHAR)
description (TEXT)
```

#### 2. **exams** table
```sql
id (PK)
exam_title (VARCHAR)
exam_date (DATE)
exam_time (TIME)
duration_minutes (INT)
exam_type (VARCHAR) -- MULTIPLE_CHOICE, DESCRIPTIVE, MIXED
subject_id (FK)
created_by (INT) -- User ID who created the exam
created_at (DATETIME)
updated_at (DATETIME)
status (VARCHAR) -- SCHEDULED, ONGOING, COMPLETED
```

#### 3. **roles** table
```sql
id (PK)
role_name (VARCHAR) -- ADMIN, TEACHER, USER
```

#### 4. **users** table
```sql
id (PK)
username (VARCHAR)
email (VARCHAR)
password_hash (VARCHAR)
role_id (FK) -- Links to roles table
created_at (DATETIME)
```

#### 5. **exam_enrollment** table
```sql
id (PK)
exam_id (BIGINT, FK)
user_id (BIGINT, FK)
enrolled_at (DATETIME)
```

---

## API Endpoints

### Base URL: `/api/exams`

---

## ADMIN ENDPOINTS

### 1. Create Exam
**Endpoint:** `POST /api/exams/admin/create`  
**Role Required:** ADMIN, TEACHER  
**Headers:** `userId: <Long>`  
**Request Body:**
```json
{
  "examTitle": "Java Advanced Concepts",
  "examDate": "2026-04-15",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "examType": "MULTIPLE_CHOICE"
}
```
**Response (201 Created):**
```json
{
  "id": 1,
  "examTitle": "Java Advanced Concepts",
  "examDate": "2026-04-15",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "exams_type": "MULTIPLE_CHOICE",
  "subject": {...},
  "createdBy": 1,
  "createdAt": "2026-03-18T10:30:00",
  "status": "SCHEDULED"
}
```

### 2. Update Exam
**Endpoint:** `PUT /api/exams/admin/update/{id}`  
**Role Required:** ADMIN, TEACHER (only own exams)  
**Headers:** `userId: <Long>`  
**Request Body:** Same as Create Exam  
**Response (200 OK):** Updated exam object

### 3. Delete Exam
**Endpoint:** `DELETE /api/exams/admin/delete/{id}`  
**Role Required:** ADMIN  
**Response (200 OK):**
```json
{
  "status": "success",
  "message": "Exam deleted successfully"
}
```

### 4. Get All Scheduled Exams
**Endpoint:** `GET /api/exams/admin/scheduled`  
**Role Required:** ADMIN  
**Description:** Returns all exams that are scheduled to run in the future  
**Response (200 OK):**
```json
[
  {
    "id": 1,
    "examTitle": "Java Advanced Concepts",
    "examDate": "2026-04-15",
    "examTime": "10:00:00",
    "durationMinutes": 120,
    "subjectId": 1,
    "subjectName": "Java",
    "examType": "MULTIPLE_CHOICE",
    "createdBy": 1,
    "status": "SCHEDULED"
  }
]
```

### 5. Get All Ongoing Exams
**Endpoint:** `GET /api/exams/admin/ongoing`  
**Role Required:** ADMIN  
**Description:** Returns exams currently in progress  
**Response (200 OK):** Array of ongoing exam objects

### 6. Get Exam Status by ID
**Endpoint:** `GET /api/exams/admin/status/{id}`  
**Role Required:** ADMIN  
**Description:** View specific exam details and current status by exam ID  
**Response (200 OK):** Single exam response object

### 7. Get All Exams with Details
**Endpoint:** `GET /api/exams/admin/all`  
**Role Required:** ADMIN  
**Description:** Complete list of all exams with full details  
**Response (200 OK):** Array of all exams

---

## TEACHER ENDPOINTS

### 1. Create Exam
**Endpoint:** `POST /api/exams/teacher/create`  
**Role Required:** TEACHER  
**Headers:** `userId: <Long>`  
**Request Body:** Same as Admin create  
**Response (201 Created):** Created exam object

**Note:** Teachers must provide:
- examTitle
- examDate
- examTime
- durationMinutes (timing limit)
- subjectId (must select a subject)
- examType (must specify type: MULTIPLE_CHOICE, DESCRIPTIVE, MIXED)

### 2. Update Exam
**Endpoint:** `PUT /api/exams/teacher/update/{id}`  
**Role Required:** TEACHER  
**Headers:** `userId: <Long>`  
**Request Body:** Same as Create Exam  
**Response (200 OK):** Updated exam object  
**Note:** Teachers can only update exams they created

### 3. Delete Exam
**Endpoint:** `DELETE /api/exams/teacher/delete/{id}`  
**Role Required:** TEACHER  
**Response (200 OK):** Success message  
**Note:** Teachers can only delete exams they created

### 4. View All Exams
**Endpoint:** `GET /api/exams/teacher/all`  
**Role Required:** TEACHER  
**Description:** Teachers can view all exams  
**Response (200 OK):** Array of all exams

---

## STUDENT/USER ENDPOINTS

### 1. Register for Exam
**Endpoint:** `POST /api/exams/student/register/{examId}`  
**Role Required:** USER/STUDENT  
**Headers:** `userId: <Long>`  
**Description:** Register for an exam before it starts  
**Constraint:** Only available before exam start time  
**Response (201 Created):**
```json
{
  "status": "success",
  "message": "Successfully registered for exam"
}
```
**Error Cases:**
- 400 Bad Request: "Exam has already started. Cannot register now."
- 400 Bad Request: "User is already registered for this exam"

### 2. Get Available Exams for Registration
**Endpoint:** `GET /api/exams/student/available`  
**Role Required:** USER/STUDENT  
**Headers:** `userId: <Long>`  
**Description:** View exams that haven't started yet and student is not registered for  
**Response (200 OK):** Array of available exams

### 3. Get Enrolled Exams
**Endpoint:** `GET /api/exams/student/enrolled`  
**Role Required:** USER/STUDENT  
**Headers:** `userId: <Long>`  
**Description:** View exams that student has registered for  
**Response (200 OK):** Array of enrolled exams

### 4. Check if Can Attempt Exam
**Endpoint:** `GET /api/exams/student/can-attempt/{examId}`  
**Role Required:** USER/STUDENT  
**Headers:** `userId: <Long>`  
**Description:** Verify if student can attempt an exam (enrolled + within scheduled time)  
**Response (200 OK):**
```json
{
  "examId": 1,
  "canAttempt": true,
  "message": "You can attempt this exam now"
}
```

---

## PUBLIC ENDPOINTS (No Authentication Required)

### 1. Get Exam Details
**Endpoint:** `GET /api/exams/{id}`  
**Description:** View exam details by exam ID  
**Response (200 OK):** Single exam response object

### 2. Get All Exams
**Endpoint:** `GET /api/exams`  
**Description:** View all available exams (for students to browse)  
**Response (200 OK):** Array of all exams

---

## SUBJECT MANAGEMENT

### 1. Create Subject
**Endpoint:** `POST /api/exams/subjects`  
**Role Required:** ADMIN  
**Request Body:**
```json
{
  "subjectName": "Java Programming",
  "description": "Advanced concepts of Java"
}
```
**Response (201 Created):** Created subject object

### 2. Get All Subjects
**Endpoint:** `GET /api/exams/subjects`  
**Description:** View all available subjects  
**Response (200 OK):** Array of all subjects

### 3. Delete Subject
**Endpoint:** `DELETE /api/exams/subjects/{id}`  
**Role Required:** ADMIN  
**Response (200 OK):**
```json
{
  "status": "success",
  "message": "Subject deleted successfully"
}
```

---

## Access Control Summary

| Feature | ADMIN | TEACHER | STUDENT |
|---------|-------|---------|---------|
| Create Exam | ✓ | ✓ (own) | ✗ |
| Read Exam | ✓ | ✓ | ✓ |
| Update Exam | ✓ | ✓ (own) | ✗ |
| Delete Exam | ✓ | ✓ (own) | ✗ |
| Register for Exam | ✗ | ✗ | ✓ |
| Attempt Exam | ✗ | ✗ | ✓ (if within schedule) |
| View Ongoing Exams | ✓ | ✗ | ✗ |
| View Scheduled Exams | ✓ | ✗ | ✗ |
| Manage Subjects | ✓ | ✗ | ✗ |

---

## Error Handling

### Common Error Responses

**401 Unauthorized:**
```json
{
  "status": "error",
  "message": "Only Admin and Teacher can create exams"
}
```

**403 Forbidden:**
```json
{
  "status": "error",
  "message": "You can only update exams you created"
}
```

**404 Not Found:**
```json
{
  "status": "error",
  "message": "Exam not found"
}
```

**400 Bad Request:**
```json
{
  "status": "error",
  "message": "Failed to register for exam: Exam has already started"
}
```

---

## Exam Types
- **MULTIPLE_CHOICE**: Multiple choice questions
- **DESCRIPTIVE**: Descriptive/essay type questions
- **MIXED**: Mix of both types

---

## Exam Status Values
- **SCHEDULED**: Exam is scheduled to happen in the future
- **ONGOING**: Exam is currently in progress
- **COMPLETED**: Exam has been completed

---

## Implementation Notes

1. **Time-based Access Control**: Students can only register for exams before they start and can only attempt exams during the scheduled time window.

2. **Role-based Enforcement**: The system validates user roles before allowing operations. Authorization exceptions are thrown for unauthorized access attempts.

3. **Subject Selection**: Both Admin and Teacher must select a subject when creating an exam from predefined subject list.

4. **Exam Type Specification**: When creating exams, the type must be specified (MULTIPLE_CHOICE, DESCRIPTIVE, or MIXED).

5. **Creator Tracking**: Each exam tracks who created it (createdBy field), allowing teachers to update/delete only their own exams.

6. **Enrollment Tracking**: The exam_enrollment table maintains student registrations with enrollment timestamp.

7. **Timing Validation**: System validates exam timing to ensure:
   - Registration is only allowed before exam starts
   - Attempts are only allowed during scheduled duration
   - Duration includes start time + durationMinutes

---

## Database Initialization Script

```sql
-- Insert Roles
INSERT INTO roles (role_name) VALUES ('ADMIN');
INSERT INTO roles (role_name) VALUES ('TEACHER');
INSERT INTO roles (role_name) VALUES ('USER');

-- Insert Subjects
INSERT INTO subjects (subject_name, description) VALUES 
('Java Programming', 'Core and Advanced Java concepts'),
('Python Basics', 'Introduction to Python programming'),
('Database Design', 'SQL and Database management'),
('Web Development', 'HTML, CSS, JavaScript, and frameworks'),
('Data Structures', 'Arrays, Linked Lists, Trees, Graphs');
```

