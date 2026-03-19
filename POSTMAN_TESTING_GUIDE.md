# 🎯 POSTMAN TESTING GUIDE - Complete Summary

## ✅ WHAT WAS UPDATED

### 1. **Port Configuration**
- ✅ Changed back to **9090** in `application.properties`
- ✅ All POSTMAN URLs use port 9090

### 2. **Code Comments Added**
- ✅ Added detailed inline comments to **ExamController.java**
- ✅ Each endpoint has:
  - Purpose explanation
  - What it validates
  - What it returns
  - Access level required
  - Example request/response
  - Error cases

### 3. **Documentation Created**
- ✅ **POSTMAN_API_COLLECTION.md** - Complete API reference
  - All 20 endpoint URLs
  - Request/Response examples
  - Sample data for testing
  - Authorization matrix
  - Error handling guide

---

## 🌐 BASE URL
```
http://localhost:9090/api/exams
```

---

## 📮 COMPLETE POSTMAN URL LIST

### Copy-Paste Ready URLs for All 20 Endpoints

#### **GROUP 1: SUBJECT MANAGEMENT** (3 endpoints)

1. **Create Subject**
   ```
   POST http://localhost:9090/api/exams/subjects
   Header: userId: 1
   ```

2. **Get All Subjects**
   ```
   GET http://localhost:9090/api/exams/subjects
   ```

3. **Delete Subject**
   ```
   DELETE http://localhost:9090/api/exams/subjects/1
   Header: userId: 1
   ```

---

#### **GROUP 2: ADMIN EXAM MANAGEMENT** (7 endpoints)

4. **Create Exam**
   ```
   POST http://localhost:9090/api/exams/admin/create
   Header: userId: 1
   ```

5. **Update Exam**
   ```
   PUT http://localhost:9090/api/exams/admin/update/1
   Header: userId: 1
   ```

6. **Delete Exam**
   ```
   DELETE http://localhost:9090/api/exams/admin/delete/1
   Header: userId: 1
   ```

7. **Get Scheduled Exams**
   ```
   GET http://localhost:9090/api/exams/admin/scheduled
   Header: userId: 1
   ```

8. **Get Ongoing Exams**
   ```
   GET http://localhost:9090/api/exams/admin/ongoing
   Header: userId: 1
   ```

9. **Get Exam Status by ID**
   ```
   GET http://localhost:9090/api/exams/admin/status/1
   Header: userId: 1
   ```

10. **Get All Exams (Admin)**
    ```
    GET http://localhost:9090/api/exams/admin/all
    Header: userId: 1
    ```

---

#### **GROUP 3: TEACHER EXAM MANAGEMENT** (4 endpoints)

11. **Teacher Create Exam**
    ```
    POST http://localhost:9090/api/exams/teacher/create
    Header: userId: 2
    ```

12. **Teacher Update Exam**
    ```
    PUT http://localhost:9090/api/exams/teacher/update/1
    Header: userId: 2
    ```

13. **Teacher Delete Exam**
    ```
    DELETE http://localhost:9090/api/exams/teacher/delete/1
    Header: userId: 2
    ```

14. **Teacher View All Exams**
    ```
    GET http://localhost:9090/api/exams/teacher/all
    Header: userId: 2
    ```

---

#### **GROUP 4: STUDENT EXAM MANAGEMENT** (4 endpoints)

15. **Student Register for Exam**
    ```
    POST http://localhost:9090/api/exams/student/register/1
    Header: userId: 3
    ```

16. **Get Available Exams**
    ```
    GET http://localhost:9090/api/exams/student/available
    Header: userId: 3
    ```

17. **Get Enrolled Exams**
    ```
    GET http://localhost:9090/api/exams/student/enrolled
    Header: userId: 3
    ```

18. **Can Attempt Exam**
    ```
    GET http://localhost:9090/api/exams/student/can-attempt/1
    Header: userId: 3
    ```

---

#### **GROUP 5: PUBLIC EXAM VIEWING** (2 endpoints)

19. **Get Exam Details**
    ```
    GET http://localhost:9090/api/exams/1
    ```

20. **Get All Exams**
    ```
    GET http://localhost:9090/api/exams
    ```

---

## 📌 POSTMAN HEADER SETUP

For **ALL endpoints** (except public ones):

| Key | Value |
|-----|-------|
| userId | 1 (or valid user ID) |
| Content-Type | application/json |

**In Postman:**
1. Go to "Headers" tab
2. Add: `Key: userId` → `Value: 1`
3. Content-Type is auto-added for JSON

---

## 📝 SAMPLE REQUEST BODIES

### Create Subject
```json
{
  "subjectName": "Java Programming",
  "description": "Core and Advanced Java concepts"
}
```

### Create Exam
```json
{
  "examTitle": "Java Advanced Concepts",
  "examDate": "2026-04-20",
  "examTime": "10:00:00",
  "durationMinutes": 120,
  "subjectId": 1,
  "examType": "MULTIPLE_CHOICE"
}
```

### Update Exam
```json
{
  "examTitle": "Java Advanced - Updated",
  "examDate": "2026-04-25",
  "examTime": "14:00:00",
  "durationMinutes": 90,
  "subjectId": 1,
  "examType": "MIXED"
}
```

---

## 🎯 TESTING WORKFLOW IN POSTMAN

### Step 1: Create a Subject (ADMIN)
```
Method: POST
URL: http://localhost:9090/api/exams/subjects
Header: userId: 1
Body: (from sample above)
Expected Response: 201 Created with Subject object
```

### Step 2: Create an Exam (ADMIN)
```
Method: POST
URL: http://localhost:9090/api/exams/admin/create
Header: userId: 1
Body: (from sample above)
Expected Response: 201 Created with Exam object
Note the exam ID in response (e.g., "id": 1)
```

### Step 3: Register Student
```
Method: POST
URL: http://localhost:9090/api/exams/student/register/1
Header: userId: 3
Expected Response: 201 Created with success message
```

### Step 4: Check Student Can Attempt
```
Method: GET
URL: http://localhost:9090/api/exams/student/can-attempt/1
Header: userId: 3
Expected Response: { "examId": 1, "canAttempt": false/true, "message": "..." }
```

### Step 5: Admin Checks Ongoing
```
Method: GET
URL: http://localhost:9090/api/exams/admin/ongoing
Header: userId: 1
Expected Response: List of exams currently running
```

---

## 🔐 USER IDS FOR TESTING

- **Admin**: userId: 1 (Full access)
- **Teacher**: userId: 2 (Create/Update/Delete own exams)
- **Student**: userId: 3 (Register and attempt exams)

Use these IDs in the `userId` header for different roles.

---

## 📊 OPERATION GUIDE

| Operation | Role | Method | Endpoint | Header |
|-----------|------|--------|----------|--------|
| Create Exam | ADMIN/TEACHER | POST | /admin/create | userId: 1/2 |
| Update Exam | ADMIN/TEACHER | PUT | /admin/update/{id} | userId: 1/2 |
| Delete Exam | ADMIN | DELETE | /admin/delete/{id} | userId: 1 |
| View All | ADMIN | GET | /admin/all | userId: 1 |
| View Scheduled | ADMIN | GET | /admin/scheduled | userId: 1 |
| View Ongoing | ADMIN | GET | /admin/ongoing | userId: 1 |
| Register | STUDENT | POST | /student/register/{id} | userId: 3 |
| Check Attempt | STUDENT | GET | /student/can-attempt/{id} | userId: 3 |

---

## ✨ COMMENTS ADDED IN CODE

The **ExamController.java** now includes:

1. **Purpose Comments**: Explain what each endpoint does
2. **Validation Comments**: Show validation logic
3. **Access Comments**: Specify role requirements
4. **Operation Comments**: Explain step-by-step operations
5. **Response Comments**: Show expected responses
6. **Error Comments**: Document error cases

Example:
```java
/**
 * ENDPOINT 15: Student Register for Exam
 * URL: POST http://localhost:9090/api/exams/student/register/{examId}
 * Purpose: Enroll a student in an exam before it starts
 * Access: STUDENT only
 */
@PostMapping("/student/register/{examId}")
public ResponseEntity<?> registerForExam(@PathVariable int examId,
                                          @RequestHeader("userId") Long userId) {
    try {
        // COMMENT: Check if exam has started
        // COMMENT: Check if already enrolled
        // COMMENT: Create enrollment record
        service.registerExam(examId, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createSuccessResponse("Successfully registered for exam"));
    } catch (Exception e) {
        // COMMENT: Return error if exam started or already enrolled
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(e.getMessage()));
    }
}
```

---

## 📂 FILES UPDATED/CREATED

### Updated:
- ✅ `application.properties` - Port changed to 9090
- ✅ `ExamController.java` - Added detailed comments

### Created:
- ✅ `POSTMAN_API_COLLECTION.md` - Complete API reference
- ✅ `POSTMAN_QUICK_REFERENCE.txt` - Quick URL list
- ✅ This file - Postman testing guide

---

## 🚀 READY TO TEST

1. ✅ Port: **9090**
2. ✅ All endpoints: **20 URLs**
3. ✅ Comments: **Added to controller**
4. ✅ Documentation: **Complete**
5. ✅ Compilation: **SUCCESS (31 files)**

**Start testing with Postman now! 🎯**

---

## 📞 REFERENCE FILES

For more information:
- **POSTMAN_API_COLLECTION.md** - Complete endpoint documentation
- **README_EXAM_MANAGEMENT.md** - Project overview
- **EXAM_MANAGEMENT_API_DOCUMENTATION.md** - Detailed API reference
- **EXAM_MANAGEMENT_QUICK_REFERENCE.md** - Quick start guide

---

**All URLs ready for Postman! Port: 9090** 🔥

