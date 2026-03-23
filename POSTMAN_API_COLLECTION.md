# 📮 POSTMAN API COLLECTION - Exam Management Module

## 🌐 Base URL
```
http://localhost:9090/api/exams
```

---

## 📋 Required Header for All Endpoints (except public ones)
```
Header Name: userId
Header Value: 1  (or any valid user ID)
Content-Type: application/json
```

---

## ✅ 20 ENDPOINTS - COMPLETE POSTMAN URLS

### ═════════════════════════════════════════════════════════════════════════
### GROUP 1: SUBJECT MANAGEMENT (3 endpoints)
### Purpose: Create, view, and delete exam subjects
### ═════════════════════════════════════════════════════════════════════════

#### ENDPOINT 1: Create Subject
```
Method: POST
URL: http://localhost:9090/api/exams/subjects
Headers: 
  - userId: 1
  - Content-Type: application/json

Request Body:
{
  "subjectName": "Java Programming",
  "description": "Core and Advanced Java concepts"
}

Purpose: Create a new subject that exams can be associated with
Access: ADMIN
Response: Subject object with ID
```

#### ENDPOINT 2: Get All Subjects
```
Method: GET
URL: http://localhost:9090/api/exams/subjects
Headers: (No userId required - PUBLIC)
  - Content-Type: application/json

Purpose: Retrieve all available subjects in the system
Access: PUBLIC
Response: List of Subject objects
```

#### ENDPOINT 3: Delete Subject
```
Method: DELETE
URL: http://localhost:9090/api/exams/subjects/{id}
Example: http://localhost:9090/api/exams/subjects/1
Headers:
  - userId: 1
  - Content-Type: application/json

Purpose: Delete a subject and all associated exams (cascade)
Access: ADMIN
Response: {"status": "success", "message": "Subject deleted successfully"}
```

---

### ═════════════════════════════════════════════════════════════════════════
### GROUP 2: ADMIN EXAM MANAGEMENT (7 endpoints)
### Purpose: Full exam management and monitoring for administrators
### ═════════════════════════════════════════════════════════════════════════

#### ENDPOINT 4: Admin Create Exam
```
Method: POST
URL: http://localhost:9090/api/exams/admin/create
Headers:
  - userId: 1  (ADMIN user)
  - Content-Type: application/json

Request Body:
{
  "examTitle": "Java Advanced Concepts",
  "examDate": "2026-04-20",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "examType": "MULTIPLE_CHOICE"
}

Purpose: Create a new exam with all parameters (date, time, duration, type, subject)
Access: ADMIN and TEACHER
Response: Complete Exams object with auto-assigned ID and status
Status: 201 Created (or 403 Forbidden if not authorized)
```

#### ENDPOINT 5: Admin Update Exam
```
Method: PUT
URL: http://localhost:9090/api/exams/admin/update/{id}
Example: http://localhost:9090/api/exams/admin/update/1
Headers:
  - userId: 1
  - Content-Type: application/json

Request Body:
{
  "examTitle": "Java Advanced Concepts - Updated",
  "examDate": "2026-04-25",
  "examTime": "14:00:00",
  "durationMinutes": 90,
  "subjectId": 1,
  "examType": "MIXED"
}

Purpose: Modify exam details (ADMIN can update all, TEACHER only own)
Access: ADMIN (all exams), TEACHER (own exams)
Response: Updated Exams object
Status: 200 OK (or 403 Forbidden if not authorized)
```

#### ENDPOINT 6: Admin Delete Exam
```
Method: DELETE
URL: http://localhost:9090/api/exams/admin/delete/{id}
Example: http://localhost:9090/api/exams/admin/delete/1
Headers:
  - userId: 1  (ADMIN user)
  - Content-Type: application/json

Purpose: Permanently remove an exam from the system
Access: ADMIN only
Response: {"status": "success", "message": "Exam deleted successfully"}
Status: 200 OK
```

#### ENDPOINT 7: Admin Get Scheduled Exams
```
Method: GET
URL: http://localhost:9090/api/exams/admin/scheduled
Headers:
  - userId: 1  (ADMIN user)

Purpose: View all exams scheduled to run in the future (start time > now)
Access: ADMIN only
Response: List of ExamResponse objects (future exams only)
Example Response:
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

#### ENDPOINT 8: Admin Get Ongoing Exams
```
Method: GET
URL: http://localhost:9090/api/exams/admin/ongoing
Headers:
  - userId: 1  (ADMIN user)

Purpose: Real-time monitoring of exams currently in progress
Purpose Details:
  - Only returns exams where: current_time >= exam_start AND current_time <= (exam_start + duration)
  - Useful for monitoring active exams
Access: ADMIN only
Response: List of ExamResponse objects (ongoing exams only)
```

#### ENDPOINT 9: Admin Get Exam Status by ID
```
Method: GET
URL: http://localhost:9090/api/exams/admin/status/{id}
Example: http://localhost:9090/api/exams/admin/status/1
Headers:
  - userId: 1  (ADMIN user)

Purpose: Get detailed status information for a specific exam
Purpose Details:
  - Returns exam with calculated status (SCHEDULED/ONGOING/COMPLETED)
  - Useful for checking specific exam details by ID
Access: ADMIN only
Response: Single ExamResponse object with status
Status: 200 OK (or 404 Not Found)
```

#### ENDPOINT 10: Admin Get All Exams
```
Method: GET
URL: http://localhost:9090/api/exams/admin/all
Headers:
  - userId: 1  (ADMIN user)

Purpose: View complete list of ALL exams in the system
Purpose Details:
  - Returns all exams regardless of status or schedule
  - Includes past, present, and future exams
  - Includes SCHEDULED, ONGOING, and COMPLETED exams
Access: ADMIN only
Response: List of all ExamResponse objects
```

---

### ═════════════════════════════════════════════════════════════════════════
### GROUP 3: TEACHER EXAM MANAGEMENT (4 endpoints)
### Purpose: Teachers can manage only their own created exams
### ═════════════════════════════════════════════════════════════════════════

#### ENDPOINT 11: Teacher Create Exam
```
Method: POST
URL: http://localhost:9090/api/exams/teacher/create
Headers:
  - userId: 2  (TEACHER user)
  - Content-Type: application/json

Request Body:
{
  "examTitle": "Python Basics",
  "examDate": "2026-05-10",
  "examTime": "09:00:00",
  "durationMinutes": 60,
  "subjectId": 2,
  "examType": "MULTIPLE_CHOICE"
}

Purpose: Create a new exam with subject and timing requirements
Purpose Details:
  - Must specify subject from predefined list
  - Must specify exam type (MULTIPLE_CHOICE, DESCRIPTIVE, MIXED)
  - Must set date, time, and duration limits
  - Exam is automatically assigned to the teacher (createdBy = userId)
Access: TEACHER only
Response: Complete Exams object
Status: 201 Created
```

#### ENDPOINT 12: Teacher Update Exam
```
Method: PUT
URL: http://localhost:9090/api/exams/teacher/update/{id}
Example: http://localhost:9090/api/exams/teacher/update/2
Headers:
  - userId: 2  (Teacher who created the exam)
  - Content-Type: application/json

Request Body:
{
  "examTitle": "Python Basics - Updated",
  "examDate": "2026-05-15",
  "examTime": "10:00:00",
  "durationMinutes": 90,
  "subjectId": 2,
  "examType": "DESCRIPTIVE"
}

Purpose: Modify details of an exam the teacher created
Purpose Details:
  - Teacher can only update exams they created (createdBy == userId)
  - Cannot update exams created by other teachers
Access: TEACHER (own exams only)
Response: Updated Exams object
Status: 200 OK (or 403 Forbidden if not the creator)
```

#### ENDPOINT 13: Teacher Delete Exam
```
Method: DELETE
URL: http://localhost:9090/api/exams/teacher/delete/{id}
Example: http://localhost:9090/api/exams/teacher/delete/2
Headers:
  - userId: 2  (Teacher who created the exam)

Purpose: Remove an exam the teacher created
Purpose Details:
  - Teacher can only delete exams they created
  - Deleting removes exam and all enrollments (cascade)
Access: TEACHER (own exams only)
Response: {"status": "success", "message": "Exam deleted successfully"}
Status: 200 OK
```

#### ENDPOINT 14: Teacher View All Exams
```
Method: GET
URL: http://localhost:9090/api/exams/teacher/all
Headers:
  - userId: 2  (TEACHER user)

Purpose: View all exams in the system for reference
Purpose Details:
  - Teachers can view all exams but can only edit their own
  - Useful for seeing what other teachers have created
Access: TEACHER
Response: List of all ExamResponse objects
```

---

### ═════════════════════════════════════════════════════════════════════════
### GROUP 4: STUDENT EXAM MANAGEMENT (4 endpoints)
### Purpose: Students register for and attempt exams within time windows
### ═════════════════════════════════════════════════════════════════════════

#### ENDPOINT 15: Student Register for Exam
```
Method: POST
URL: http://localhost:9090/api/exams/student/register/{examId}
Example: http://localhost:9090/api/exams/student/register/1
Headers:
  - userId: 3  (STUDENT user)

Purpose: Enroll a student in an exam before it starts
Purpose Details:
  - Creates enrollment record with timestamp
  - Validates exam has not started yet
  - Prevents duplicate enrollments
  - Can only register if exam start time > current time
Access: STUDENT only
Response: {"status": "success", "message": "Successfully registered for exam"}
Status: 201 Created (or 400 Bad Request if exam started/already enrolled)
Error Cases:
  - "Exam has already started. Cannot register now."
  - "User is already registered for this exam"
```

#### ENDPOINT 16: Student Get Available Exams for Registration
```
Method: GET
URL: http://localhost:9090/api/exams/student/available
Headers:
  - userId: 3  (STUDENT user)

Purpose: View exams student can register for
Purpose Details:
  - Returns only exams that haven't started yet (start > now)
  - Excludes exams student is already enrolled in
  - Useful for "Browse and Register" feature
Access: STUDENT only
Response: List of ExamResponse objects for available exams
```

#### ENDPOINT 17: Student Get Enrolled Exams
```
Method: GET
URL: http://localhost:9090/api/exams/student/enrolled
Headers:
  - userId: 3  (STUDENT user)

Purpose: View all exams the student has registered for
Purpose Details:
  - Returns exams from exam_enrollment table for this user
  - Shows past, present, and future enrolled exams
  - Useful for student dashboard showing registered exams
Access: STUDENT only
Response: List of ExamResponse objects for enrolled exams
```

#### ENDPOINT 18: Student Check Can Attempt Exam
```
Method: GET
URL: http://localhost:9090/api/exams/student/can-attempt/{examId}
Example: http://localhost:9090/api/exams/student/can-attempt/1
Headers:
  - userId: 3  (STUDENT user)

Purpose: Verify if student can attempt the exam RIGHT NOW
Purpose Details:
  - Checks if student is enrolled in the exam
  - Checks if current time is within exam window (start <= now <= end)
  - Returns detailed message explaining why if cannot attempt
  - Used before showing exam interface
Access: STUDENT only
Response:
{
  "examId": 1,
  "canAttempt": true,
  "message": "You can attempt this exam now"
}
OR
{
  "examId": 1,
  "canAttempt": false,
  "message": "You cannot attempt this exam. Check enrollment and schedule."
}
Status: 200 OK
Time Validation Logic:
  1. Check: Student enrolled? (examId, userId in exam_enrollment table)
  2. Check: Now >= exam start? (current_time >= exam_date + exam_time)
  3. Check: Now <= exam end? (current_time <= exam_date + exam_time + duration)
  Returns true only if all 3 pass
```

---

### ═════════════════════════════════════════════════════════════════════════
### GROUP 5: PUBLIC EXAM VIEWING (2 endpoints)
### Purpose: Anyone can browse and view exam information
### ═════════════════════════════════════════════════════════════════════════

#### ENDPOINT 19: Get Exam Details by ID
```
Method: GET
URL: http://localhost:9090/api/exams/{id}
Example: http://localhost:9090/api/exams/1
Headers: (No userId required - PUBLIC)

Purpose: View detailed information about a specific exam
Purpose Details:
  - Anyone can view exam details without authentication
  - Returns full exam information including timing and type
Access: PUBLIC
Response: Single ExamResponse object
Example Response:
{
  "id": 1,
  "examTitle": "Java Advanced Concepts",
  "examDate": "2026-04-20",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "subjectName": "Java Programming",
  "examType": "MULTIPLE_CHOICE",
  "createdBy": 1,
  "status": "SCHEDULED"
}
Status: 200 OK (or 404 Not Found)
```

#### ENDPOINT 20: Get All Exams
```
Method: GET
URL: http://localhost:9090/api/exams
Headers: (No userId required - PUBLIC)

Purpose: View complete list of all exams in the system
Purpose Details:
  - Anyone can browse all available exams
  - Returns all exams with basic information
  - Useful for browsing and discovering exams
Access: PUBLIC
Response: List of ExamResponse objects for ALL exams
```

---

## 🔑 SAMPLE DATA FOR TESTING

### Create Sample Subjects First:
```
POST http://localhost:9090/api/exams/subjects
{
  "subjectName": "Java Programming",
  "description": "Core and Advanced Java concepts"
}

POST http://localhost:9090/api/exams/subjects
{
  "subjectName": "Python Basics",
  "description": "Introduction to Python programming"
}

POST http://localhost:9090/api/exams/subjects
{
  "subjectName": "Database Design",
  "description": "SQL and Database management"
}
```

### Sample Exam Creation:
```
POST http://localhost:9090/api/exams/admin/create
Headers: userId: 1
{
  "examTitle": "Java Advanced Concepts Final",
  "examDate": "2026-04-20",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "examType": "MULTIPLE_CHOICE"
}
```

---

## 📊 QUICK REFERENCE TABLE

| # | Endpoint | Method | URL | Access | Purpose |
|---|----------|--------|-----|--------|---------|
| 1 | Create Subject | POST | /subjects | ADMIN | Create exam subject |
| 2 | Get All Subjects | GET | /subjects | PUBLIC | View all subjects |
| 3 | Delete Subject | DELETE | /subjects/{id} | ADMIN | Delete subject |
| 4 | Admin Create Exam | POST | /admin/create | ADMIN/TEACHER | Create exam |
| 5 | Admin Update Exam | PUT | /admin/update/{id} | ADMIN/TEACHER | Update exam |
| 6 | Admin Delete Exam | DELETE | /admin/delete/{id} | ADMIN | Delete exam |
| 7 | Get Scheduled Exams | GET | /admin/scheduled | ADMIN | View future exams |
| 8 | Get Ongoing Exams | GET | /admin/ongoing | ADMIN | View running exams |
| 9 | Get Exam Status | GET | /admin/status/{id} | ADMIN | View exam status |
| 10 | Get All Exams (Admin) | GET | /admin/all | ADMIN | View all exams |
| 11 | Teacher Create Exam | POST | /teacher/create | TEACHER | Create exam |
| 12 | Teacher Update Exam | PUT | /teacher/update/{id} | TEACHER | Update own exam |
| 13 | Teacher Delete Exam | DELETE | /teacher/delete/{id} | TEACHER | Delete own exam |
| 14 | Teacher View All | GET | /teacher/all | TEACHER | View all exams |
| 15 | Student Register | POST | /student/register/{id} | STUDENT | Register for exam |
| 16 | Get Available Exams | GET | /student/available | STUDENT | View open exams |
| 17 | Get Enrolled Exams | GET | /student/enrolled | STUDENT | View my exams |
| 18 | Can Attempt Exam | GET | /student/can-attempt/{id} | STUDENT | Check eligibility |
| 19 | Get Exam Details | GET | /{id} | PUBLIC | View exam details |
| 20 | Get All Exams | GET | / | PUBLIC | Browse all exams |

---

## 🔒 AUTHORIZATION SUMMARY

| Role | Create | Update | Delete | View All | View Own | Register | Attempt |
|------|--------|--------|--------|----------|----------|----------|---------|
| ADMIN | ✓ | ✓ (all) | ✓ | ✓ | - | ✗ | ✗ |
| TEACHER | ✓ | ✓ (own) | ✓ (own) | ✓ | - | ✗ | ✗ |
| STUDENT | ✗ | ✗ | ✗ | ✓ | ✓ | ✓ | ✓* |

*STUDENT can attempt only if enrolled + within time window

---

## 💡 USAGE WORKFLOW

### For ADMIN:
1. Create Subjects (POST /subjects)
2. Create Exams (POST /admin/create)
3. View Scheduled Exams (GET /admin/scheduled)
4. Monitor Ongoing Exams (GET /admin/ongoing)
5. Update/Delete as needed

### For TEACHER:
1. View Available Subjects (GET /subjects)
2. Create Exams (POST /teacher/create)
3. Update Own Exams (PUT /teacher/update/{id})
4. Delete Own Exams (DELETE /teacher/delete/{id})

### For STUDENT:
1. Browse Exams (GET /exams or GET /exams/{id})
2. Register for Exam (POST /student/register/{id})
3. View Enrolled Exams (GET /student/enrolled)
4. Check Attempt Eligibility (GET /student/can-attempt/{id})
5. Attempt Exam (when eligible)

---

## ⚠️ ERROR HANDLING

### Common Error Responses:

**403 Forbidden:**
```
{
  "status": "error",
  "message": "Only Admin and Teacher can create exams"
}
```

**400 Bad Request:**
```
{
  "status": "error",
  "message": "Exam has already started. Cannot register now."
}
```

**404 Not Found:**
```
{
  "status": "error",
  "message": "Exam not found"
}
```

---

**Ready to test with Postman! 🚀**
Port: 9090

