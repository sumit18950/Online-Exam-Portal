package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.dto.SubjectRequest;
import com.springboot.online_exam_portal.dto.SubjectResponse;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.UserRepository;
import com.springboot.online_exam_portal.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService service;
    private final UserRepository userRepository;

    public ExamController(ExamService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    // ╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç Subject Endpoints ╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç

    // GET /api/exams/subjects        ╬ô├Ñ├å list all subjects
    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponse>> listSubjects() {
        List<SubjectResponse> subjects = service.getAllSubjects().stream()
                .map(this::toSubjectResponse)
                .toList();
        return ResponseEntity.ok(subjects);
    }

    // GET /api/exams/subjects/{id}   ╬ô├Ñ├å get one subject by id
    @GetMapping("/subjects/{id}")
    public ResponseEntity<SubjectResponse> getSubject(@PathVariable int id) {
        return ResponseEntity.ok(toSubjectResponse(service.getSubjectById(id)));
    }

    // POST /api/exams/subjects       ╬ô├Ñ├å add a new subject
    @PostMapping("/subjects")
    public ResponseEntity<SubjectResponse> addSubject(@RequestBody SubjectRequest request,
                                                      @RequestHeader(value = "userId", required = false) Long userId,
                                                      Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        Subject saved = service.saveSubject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toSubjectResponse(saved));
    }

    // PUT /api/exams/subjects/{id}   ╬ô├Ñ├å update a subject (full update)
    @PutMapping("/subjects/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable int id,
                                                         @RequestBody SubjectRequest request,
                                                         @RequestHeader(value = "userId", required = false) Long userId,
                                                         Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        return ResponseEntity.ok(toSubjectResponse(service.updateSubject(id, request)));
    }

    // PATCH /api/exams/subjects/{id} ╬ô├Ñ├å partial update a subject
    @PatchMapping("/subjects/{id}")
    public ResponseEntity<SubjectResponse> patchSubject(@PathVariable int id,
                                                        @RequestBody SubjectRequest request,
                                                        @RequestHeader(value = "userId", required = false) Long userId,
                                                        Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        return ResponseEntity.ok(toSubjectResponse(service.updateSubject(id, request)));
    }

    // DELETE /api/exams/subjects/{id} ╬ô├Ñ├å delete subject (cascades to its exams)
    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable int id,
                                                @RequestHeader(value = "userId", required = false) Long userId,
                                                Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        return ResponseEntity.ok(service.deleteSubject(id, toAuthentication(actor)));
    }

    // ╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç Exam Endpoints ╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç╬ô├╢├ç

    // GET /api/exams                 ╬ô├Ñ├å list all exams
    @GetMapping
    public ResponseEntity<List<ExamResponse>> listExams() {
        return ResponseEntity.ok(service.getAllExams());
    }

    // GET /api/exams/{id}            ╬ô├Ñ├å get one exam by id
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getExam(@PathVariable int id) {
        return ResponseEntity.ok(service.getExamById(id));
    }

    // POST /api/exams                ╬ô├Ñ├å create a new exam
    @PostMapping
    public ResponseEntity<ExamResponse> addExam(@RequestBody ExamRequest request,
                                                @RequestHeader(value = "userId", required = false) Long userId,
                                                Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        request.setCreatedBy(actor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createExam(request));
    }

    // POST /api/exams/admin/create   ╬ô├Ñ├å compatibility route for documented admin API
    @PostMapping("/admin/create")
    public ResponseEntity<ExamResponse> addExamAdmin(@RequestBody ExamRequest request,
                                                     @RequestHeader(value = "userId", required = false) Long userId,
                                                     Authentication authentication) {
        return addExam(request, userId, authentication);
    }

    // POST /api/exams/teacher/create ╬ô├Ñ├å compatibility route for documented teacher API
    @PostMapping("/teacher/create")
    public ResponseEntity<ExamResponse> addExamTeacher(@RequestBody ExamRequest request,
                                                       @RequestHeader(value = "userId", required = false) Long userId,
                                                       Authentication authentication) {
        return addExam(request, userId, authentication);
    }

    // PUT /api/exams/{id}            ╬ô├Ñ├å update an existing exam
    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> updateExam(@PathVariable int id,
                                                    @RequestBody ExamRequest request,
                                                    @RequestHeader(value = "userId", required = false) Long userId,
                                                    Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        // Prevent ownership tampering through update payload.
        request.setCreatedBy(null);
        return ResponseEntity.ok(service.updateExam(id, request));
    }

    // PATCH /api/exams/{id}          ╬ô├Ñ├å partial update an existing exam
    @PatchMapping("/{id}")
    public ResponseEntity<ExamResponse> patchExam(@PathVariable int id,
                                                   @RequestBody ExamRequest request,
                                                   @RequestHeader(value = "userId", required = false) Long userId,
                                                   Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        request.setCreatedBy(null);
        return ResponseEntity.ok(service.updateExam(id, request));
    }

    // PUT /api/exams/admin/update/{id}   ╬ô├Ñ├å compatibility route for documented admin API
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ExamResponse> updateExamAdmin(@PathVariable int id,
                                                        @RequestBody ExamRequest request,
                                                        @RequestHeader(value = "userId", required = false) Long userId,
                                                        Authentication authentication) {
        return updateExam(id, request, userId, authentication);
    }

    // PATCH /api/exams/admin/update/{id} ╬ô├Ñ├å compatibility route for documented admin API
    @PatchMapping("/admin/update/{id}")
    public ResponseEntity<ExamResponse> patchExamAdmin(@PathVariable int id,
                                                       @RequestBody ExamRequest request,
                                                       @RequestHeader(value = "userId", required = false) Long userId,
                                                       Authentication authentication) {
        return updateExam(id, request, userId, authentication);
    }

    // PUT /api/exams/teacher/update/{id} ╬ô├Ñ├å compatibility route for documented teacher API
    @PutMapping("/teacher/update/{id}")
    public ResponseEntity<ExamResponse> updateExamTeacher(@PathVariable int id,
                                                          @RequestBody ExamRequest request,
                                                          @RequestHeader(value = "userId", required = false) Long userId,
                                                          Authentication authentication) {
        return updateExam(id, request, userId, authentication);
    }

    // PATCH /api/exams/teacher/update/{id} ╬ô├Ñ├å compatibility route for documented teacher API
    @PatchMapping("/teacher/update/{id}")
    public ResponseEntity<ExamResponse> patchExamTeacher(@PathVariable int id,
                                                         @RequestBody ExamRequest request,
                                                         @RequestHeader(value = "userId", required = false) Long userId,
                                                         Authentication authentication) {
        return updateExam(id, request, userId, authentication);
    }

    // DELETE /api/exams/{id}         ╬ô├Ñ├å delete an exam
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExam(@PathVariable int id,
                                             @RequestHeader(value = "userId", required = false) Long userId,
                                             Authentication authentication) {
        User actor = resolveCurrentUser(authentication, userId);
        enforceAdminOrTeacher(actor);
        return ResponseEntity.ok(service.deleteExam(id, toAuthentication(actor)));
    }

    // DELETE /api/exams/admin/delete/{id}   ╬ô├Ñ├å compatibility route for documented admin API
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteExamAdmin(@PathVariable int id,
                                                  @RequestHeader(value = "userId", required = false) Long userId,
                                                  Authentication authentication) {
        return deleteExam(id, userId, authentication);
    }

    // DELETE /api/exams/teacher/delete/{id} ╬ô├Ñ├å compatibility route for documented teacher API
    @DeleteMapping("/teacher/delete/{id}")
    public ResponseEntity<String> deleteExamTeacher(@PathVariable int id,
                                                    @RequestHeader(value = "userId", required = false) Long userId,
                                                    Authentication authentication) {
        return deleteExam(id, userId, authentication);
    }

    // GET /api/exams/admin/all ╬ô├Ñ├å compatibility route for documented admin API
    @GetMapping("/admin/all")
    public ResponseEntity<List<ExamResponse>> listExamsAdmin() {
        return listExams();
    }

    // GET /api/exams/teacher/all ╬ô├Ñ├å compatibility route for documented teacher API
    @GetMapping("/teacher/all")
    public ResponseEntity<List<ExamResponse>> listExamsTeacher() {
        return listExams();
    }

    private User resolveCurrentUser(Authentication authentication, Long userId) {
        if (authentication != null && authentication.getName() != null && !authentication.getName().isBlank()) {
            return userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
        }

        if (userId != null) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found for userId header"));
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Provide Bearer token or userId header");
    }

    private void enforceAdminOrTeacher(User user) {
        String role = normalizeRole(user);
        boolean allowed = "ADMIN".equals(role) || "TEACHER".equals(role);

        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN or TEACHER can perform this action");
        }
    }

    private Authentication toAuthentication(User user) {
        String role = normalizeRole(user);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        String principal = user.getEmail() != null && !user.getEmail().isBlank() ? user.getEmail() : String.valueOf(user.getId());
        return new UsernamePasswordAuthenticationToken(principal, null, Collections.singletonList(authority));
    }

    private String normalizeRole(User user) {
        if (user == null || user.getRole() == null || user.getRole().getRoleName() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User role is not assigned");
        }
        String role = user.getRole().getRoleName().trim().toUpperCase(Locale.ROOT);
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }

    private SubjectResponse toSubjectResponse(Subject subject) {
        return new SubjectResponse(subject.getId(), subject.getSubjectName(), subject.getDescription());
    }
}
