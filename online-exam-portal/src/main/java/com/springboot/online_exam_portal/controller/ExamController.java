package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.dto.SubjectRequest;
import com.springboot.online_exam_portal.dto.SubjectResponse;
import com.springboot.online_exam_portal.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExamController - RESTful API Controller for Exam Management Module
 * BASE URL: http://localhost:9090/api/exams
 */
@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired 
    private ExamService service;

    // ===========================================================================================
    // SUBJECT MANAGEMENT ENDPOINTS
    // ===========================================================================================

    /**
     * ENDPOINT 1: Create Subject
     * URL: POST http://localhost:9090/api/exams/subjects
     *
     * COMMENT: Accepts BOTH "subject" and "subjectName" as the name field.
     * Both formats work in Postman:
     *   Format 1: { "subject": "Maths", "description": "This is maths subject" }
     *   Format 2: { "subjectName": "Maths", "description": "This is maths subject" }
     *
     * Response: 201 Created with the saved subject object (includes auto-generated id)
     */
    @PostMapping("/subjects")
    public ResponseEntity<?> createSubject(@RequestBody SubjectRequest subjectRequest) {
        try {
            // COMMENT: Map SubjectRequest DTO → Subject entity, then save to database
            Subject savedSubject = service.saveSubjectFromRequest(subjectRequest);
            // COMMENT: Return flat DTO to avoid lazy-loading issues on Subject.exams during JSON conversion
            return ResponseEntity.status(HttpStatus.CREATED).body(toSubjectResponse(savedSubject));
        } catch (Exception e) {
            // COMMENT: Return error details if validation or DB error occurs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to create subject: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT 2: Get All Subjects
     * URL: GET http://localhost:9090/api/exams/subjects
     * Purpose: Retrieve list of all available subjects
     * Access: PUBLIC
     * Response: List of Subject objects
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        // COMMENT: Return flat DTO list (id, subjectName, description) to prevent lazy-init exception
        List<SubjectResponse> subjects = service.getAllSubjects().stream()
                .map(this::toSubjectResponse)
                .toList();
        return ResponseEntity.ok(subjects);
    }

    /**
     * ENDPOINT 3: Delete Subject
     * URL: DELETE http://localhost:9090/api/exams/subjects/{id}
     * Purpose: Delete a subject and all associated exams (cascade delete)
     * Access: ADMIN only
     * Path Parameter: id - Subject ID to delete
     * Response: Success message
     */
    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable int id) {
        try {
            // COMMENT: Delete subject and cascading exams
            service.deleteSubject(id);
            return ResponseEntity.ok(createSuccessResponse("Subject deleted successfully"));
        } catch (Exception e) {
            // COMMENT: Handle deletion errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to delete subject: " + e.getMessage()));
        }
    }

    // ===========================================================================================
    // ADMIN EXAM MANAGEMENT ENDPOINTS (7 endpoints)
    // Purpose: Admin operations for exam management, scheduling, monitoring, and administration
    // ===========================================================================================
    
    /**
     * ENDPOINT 4: Admin Create Exam
     * URL: POST http://localhost:9090/api/exams/admin/create
     * Purpose: Create a new exam with all parameters (date, time, duration, type, subject)
     * Access: ADMIN and TEACHER
     * Headers Required: userId (Long)
     * Request Body: {
     *   "examTitle": "Java Advanced Concepts",
     *   "examDate": "2026-04-20",
     *   "examTime": "10:00:00",
     *   "durationMinutes": 120,
     *   "subjectId": 1,
     *   "examType": "MULTIPLE_CHOICE"
     * }
     * Response: Created Exams object with ID and status
     */
    @PostMapping("/admin/create")
    public ResponseEntity<?> createExamByAdmin(@RequestBody ExamRequest examRequest, 
                                                @RequestHeader("userId") Long userId) {
        try {
            // COMMENT: Validate user role (ADMIN/TEACHER), check authorization, then create exam
            Exams exam = service.createExamWithRequest(examRequest, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(exam);
        } catch (Exception e) {
            // COMMENT: Return 403 Forbidden if user not authorized
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 5: Admin Update Exam
     * URL: PUT http://localhost:9090/api/exams/admin/update/{id}
     * Purpose: Modify exam details (title, date, time, duration, type, subject)
     * Access: ADMIN (all exams), TEACHER (own exams only)
     * Headers Required: userId (Long)
     * Path Parameter: id - Exam ID to update
     * Request Body: Same as create exam
     * Response: Updated Exams object
     */
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateExamByAdmin(@PathVariable int id, 
                                                @RequestBody ExamRequest examRequest,
                                                @RequestHeader("userId") Long userId) {
        try {
            // COMMENT: Validate ownership (teachers can only update own exams), then update
            Exams updatedExam = service.updateExamWithRequest(id, examRequest, userId);
            return ResponseEntity.ok(updatedExam);
        } catch (Exception e) {
            // COMMENT: Return error if unauthorized or exam not found
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 6: Admin Delete Exam
     * URL: DELETE http://localhost:9090/api/exams/admin/delete/{id}
     * Purpose: Remove an exam from the system completely
     * Access: ADMIN only
     * Path Parameter: id - Exam ID to delete
     * Response: Success message
     */
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteExamByAdmin(@PathVariable int id) {
        try {
            // COMMENT: Delete exam from database (also removes all enrollments due to cascade)
            service.deleteExam(id);
            return ResponseEntity.ok(createSuccessResponse("Exam deleted successfully"));
        } catch (Exception e) {
            // COMMENT: Handle deletion errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to delete exam: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT 7: Admin Get Scheduled Exams
     * URL: GET http://localhost:9090/api/exams/admin/scheduled
     * Purpose: View all exams that are scheduled to run in the future
     * Access: ADMIN only
     * Response: List of ExamResponse objects for future exams only
     */
    @GetMapping("/admin/scheduled")
    public ResponseEntity<List<ExamResponse>> getScheduledExams() {
        // COMMENT: Fetch exams with start time > current time (future exams)
        return ResponseEntity.ok(service.getScheduledExams());
    }

    /**
     * ENDPOINT 8: Admin Get Ongoing Exams
     * URL: GET http://localhost:9090/api/exams/admin/ongoing
     * Purpose: Monitor exams currently in progress in real-time
     * Access: ADMIN only
     * Response: List of ExamResponse objects for exams within scheduled time window
     */
    @GetMapping("/admin/ongoing")
    public ResponseEntity<List<ExamResponse>> getOngoingExams() {
        // COMMENT: Fetch exams where current time is between start and end time
        // COMMENT: Calculation: start <= now <= (start + duration)
        return ResponseEntity.ok(service.getOngoingExams());
    }

    /**
     * ENDPOINT 9: Admin Get Exam Status by ID
     * URL: GET http://localhost:9090/api/exams/admin/status/{id}
     * Purpose: Get detailed status and information for a specific exam by ID
     * Access: ADMIN only
     * Path Parameter: id - Exam ID
     * Response: Complete ExamResponse with status (SCHEDULED/ONGOING/COMPLETED)
     */
    @GetMapping("/admin/status/{id}")
    public ResponseEntity<?> getExamStatusByAdmin(@PathVariable int id) {
        try {
            // COMMENT: Retrieve specific exam with calculated status based on current time
            ExamResponse examResponse = service.getExamStatusByExamId(id);
            return ResponseEntity.ok(examResponse);
        } catch (Exception e) {
            // COMMENT: Return 404 if exam not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 10: Admin Get All Exams
     * URL: GET http://localhost:9090/api/exams/admin/all
     * Purpose: View complete list of all exams in the system with full details
     * Access: ADMIN only
     * Response: List of all ExamResponse objects
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<ExamResponse>> getAllExamsByAdmin() {
        // COMMENT: Fetch all exams regardless of status or schedule
        return ResponseEntity.ok(service.getAllExamsWithResponse());
    }

    // ===========================================================================================
    // TEACHER EXAM MANAGEMENT ENDPOINTS (4 endpoints)
    // Purpose: Teacher operations to manage their own exams (create, update, delete, view)
    // ===========================================================================================
    
    /**
     * ENDPOINT 11: Teacher Create Exam
     * URL: POST http://localhost:9090/api/exams/teacher/create
     * Purpose: Create a new exam with subject and timing requirements
     * Access: TEACHER only
     * Headers Required: userId (Long)
     * Request Body: Same as admin create (must specify subject, type, timing)
     * Response: Created Exams object
     */
    @PostMapping("/teacher/create")
    public ResponseEntity<?> createExamByTeacher(@RequestBody ExamRequest examRequest,
                                                  @RequestHeader("userId") Long userId) {
        try {
            // COMMENT: Verify user is TEACHER role, then create exam
            Exams exam = service.createExamWithRequest(examRequest, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(exam);
        } catch (Exception e) {
            // COMMENT: Reject if user is not TEACHER role
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 12: Teacher Update Exam
     * URL: PUT http://localhost:9090/api/exams/teacher/update/{id}
     * Purpose: Modify details of an exam the teacher created
     * Access: TEACHER (only own exams)
     * Headers Required: userId (Long)
     * Path Parameter: id - Exam ID to update
     * Request Body: Updated exam details
     * Response: Updated Exams object
     */
    @PutMapping("/teacher/update/{id}")
    public ResponseEntity<?> updateExamByTeacher(@PathVariable int id,
                                                  @RequestBody ExamRequest examRequest,
                                                  @RequestHeader("userId") Long userId) {
        try {
            // COMMENT: Verify teacher owns this exam (createdBy == userId), then update
            Exams updatedExam = service.updateExamWithRequest(id, examRequest, userId);
            return ResponseEntity.ok(updatedExam);
        } catch (Exception e) {
            // COMMENT: Reject if teacher doesn't own the exam
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 13: Teacher Delete Exam
     * URL: DELETE http://localhost:9090/api/exams/teacher/delete/{id}
     * Purpose: Remove an exam the teacher created from the system
     * Access: TEACHER (only own exams)
     * Path Parameter: id - Exam ID to delete
     * Response: Success message
     */
    @DeleteMapping("/teacher/delete/{id}")
    public ResponseEntity<?> deleteExamByTeacher(@PathVariable int id) {
        try {
            // COMMENT: Delete exam (authorization checked in service layer)
            service.deleteExam(id);
            return ResponseEntity.ok(createSuccessResponse("Exam deleted successfully"));
        } catch (Exception e) {
            // COMMENT: Handle any deletion errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to delete exam: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT 14: Teacher View All Exams
     * URL: GET http://localhost:9090/api/exams/teacher/all
     * Purpose: View all exams in the system for reference (not limited to own exams)
     * Access: TEACHER
     * Response: List of all ExamResponse objects
     */
    @GetMapping("/teacher/all")
    public ResponseEntity<List<ExamResponse>> getAllExamsByTeacher() {
        // COMMENT: Teachers can view all exams but can only modify their own
        return ResponseEntity.ok(service.getAllExamsWithResponse());
    }

    // ===========================================================================================
    // STUDENT EXAM MANAGEMENT ENDPOINTS (4 endpoints)
    // Purpose: Student operations to register for and attempt exams within time windows
    // ===========================================================================================
    
    /**
     * ENDPOINT 15: Student Register for Exam
     * URL: POST http://localhost:9090/api/exams/student/register/{examId}
     * Purpose: Enroll a student in an exam before it starts
     * Access: STUDENT only
     * Headers Required: userId (Long)
     * Path Parameter: examId - ID of exam to register for
     * Validation: Exam must not have started yet
     * Response: Success message with enrollment confirmation
     */
    @PostMapping("/student/register/{examId}")
    public ResponseEntity<?> registerForExam(@PathVariable int examId,
                                              @RequestHeader("userId") Long userId) {
        try {
            // COMMENT: Check if exam has started, prevent registration if true
            // COMMENT: Check if already enrolled, prevent duplicate enrollment
            // COMMENT: Create ExamEnrollment record with current timestamp
            service.registerExam(examId, userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createSuccessResponse("Successfully registered for exam"));
        } catch (Exception e) {
            // COMMENT: Return error if exam started or already enrolled
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 16: Student Get Available Exams for Registration
     * URL: GET http://localhost:9090/api/exams/student/available
     * Purpose: View exams that haven't started yet and student is not registered for
     * Access: STUDENT only
     * Headers Required: userId (Long)
     * Response: List of ExamResponse objects for exams student can register for
     */
    @GetMapping("/student/available")
    public ResponseEntity<List<ExamResponse>> getAvailableExamsForRegistration(
            @RequestHeader("userId") Long userId) {
        // COMMENT: Fetch exams where:
        // COMMENT: 1. Start time > current time (not started)
        // COMMENT: 2. Student not already enrolled
        return ResponseEntity.ok(service.getAvailableExamsForRegistration(userId));
    }

    /**
     * ENDPOINT 17: Student Get Enrolled Exams
     * URL: GET http://localhost:9090/api/exams/student/enrolled
     * Purpose: View all exams the student has registered for
     * Access: STUDENT only
     * Headers Required: userId (Long)
     * Response: List of ExamResponse objects for exams student is enrolled in
     */
    @GetMapping("/student/enrolled")
    public ResponseEntity<List<ExamResponse>> getEnrolledExams(@RequestHeader("userId") Long userId) {
        // COMMENT: Query exam_enrollment table for user's enrollments
        // COMMENT: Join with exams table to get full exam details
        return ResponseEntity.ok(service.getEnrolledExams(userId));
    }

    /**
     * ENDPOINT 18: Student Check Can Attempt Exam
     * URL: GET http://localhost:9090/api/exams/student/can-attempt/{examId}
     * Purpose: Verify if student is eligible to attempt exam NOW (enrolled + within time window)
     * Access: STUDENT only
     * Headers Required: userId (Long)
     * Path Parameter: examId - ID of exam to check
     * Validation:
     *   1. Student must be enrolled in the exam
     *   2. Current time must be >= exam start time
     *   3. Current time must be <= exam start time + duration
     * Response: { "examId": 1, "canAttempt": true/false, "message": "..." }
     */
    @GetMapping("/student/can-attempt/{examId}")
    public ResponseEntity<?> canAttemptExam(@PathVariable int examId,
                                             @RequestHeader("userId") Long userId) {
        try {
            // COMMENT: Check enrollment status
            // COMMENT: Check if within scheduled time window
            // COMMENT: Return boolean result with detailed message
            boolean canAttempt = service.canAttemptExam(examId, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("examId", examId);
            response.put("canAttempt", canAttempt);
            response.put("message", canAttempt ? 
                    "You can attempt this exam now" : 
                    "You cannot attempt this exam. Check enrollment and schedule.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // COMMENT: Return error if exam not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // ===========================================================================================
    // PUBLIC EXAM VIEWING ENDPOINTS (2 endpoints)
    // Purpose: Public endpoints for anyone to browse and view exam information
    // ===========================================================================================
    
    /**
     * ENDPOINT 19: Get Exam Details by ID
     * URL: GET http://localhost:9090/api/exams/{id}
     * Purpose: View detailed information about a specific exam
     * Access: PUBLIC (anyone can view)
     * Path Parameter: id - Exam ID to retrieve
     * Response: ExamResponse object with all exam details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getExamDetails(@PathVariable int id) {
        try {
            // COMMENT: Fetch exam by ID and convert to response DTO
            ExamResponse examResponse = service.getExamById(id);
            return ResponseEntity.ok(examResponse);
        } catch (Exception e) {
            // COMMENT: Return 404 if exam not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * ENDPOINT 20: Get All Exams
     * URL: GET http://localhost:9090/api/exams
     * Purpose: View complete list of all exams available in the system
     * Access: PUBLIC (anyone can view)
     * Response: List of ExamResponse objects for all exams
     */
    @GetMapping
    public ResponseEntity<List<ExamResponse>> getAllExams() {
        // COMMENT: Fetch all exams and return as response DTOs
        return ResponseEntity.ok(service.getAllExamsWithResponse());
    }

    // ===========================================================================================
    // HELPER METHODS
    // Purpose: Utility methods to create standardized response formats
    // ===========================================================================================
    
    /**
     * Helper Method: Create Error Response
     * Purpose: Standardize error responses with status and message
     * Returns: Map with "status": "error" and "message": error_message
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }

    /**
     * Helper Method: Create Success Response
     * Purpose: Standardize success responses with status and message
     * Returns: Map with "status": "success" and "message": success_message
     */
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        return response;
    }

    /**
     * COMMENT: Subject entity -> SubjectResponse DTO mapper.
     * Keeps /subjects responses flat and avoids serializing lazy collections.
     */
    private SubjectResponse toSubjectResponse(Subject subject) {
        return new SubjectResponse(subject.getId(), subject.getSubjectName(), subject.getDescription());
    }
}



