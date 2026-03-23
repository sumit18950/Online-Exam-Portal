package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired private ExamService service;

    // ─── Subject Endpoints ──────────────────────────────────────────────────

    // GET /api/exams/subjects        → list all subjects
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> listSubjects() {
        return ResponseEntity.ok(service.getAllSubjects());
    }

    // GET /api/exams/subjects/{id}   → get one subject by id
    @GetMapping("/subjects/{id}")
    public ResponseEntity<Subject> getSubject(@PathVariable int id) {
        return ResponseEntity.ok(service.getSubjectById(id));
    }

    // POST /api/exams/subjects       → add a new subject
    @PostMapping("/subjects")
    public ResponseEntity<Subject> addSubject(@RequestBody Subject s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveSubject(s));
    }

    // DELETE /api/exams/subjects/{id} → delete subject (cascades to its exams)
    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable int id, Authentication authentication) {
        return ResponseEntity.ok(service.deleteSubject(id, authentication));
    }

    // ─── Exam Endpoints ─────────────────────────────────────────────────────

    // GET /api/exams                 → list all exams
    @GetMapping
    public ResponseEntity<List<ExamResponse>> listExams() {
        return ResponseEntity.ok(service.getAllExams());
    }

    // GET /api/exams/{id}            → get one exam by id
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getExam(@PathVariable int id) {
        return ResponseEntity.ok(service.getExamById(id));
    }

    // POST /api/exams                → create a new exam
    @PostMapping
    public ResponseEntity<ExamResponse> addExam(@RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createExam(request));
    }

    // PUT /api/exams/{id}            → update an existing exam
    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> updateExam(@PathVariable int id,
                                                    @RequestBody ExamRequest request) {
        return ResponseEntity.ok(service.updateExam(id, request));
    }

    // DELETE /api/exams/{id}         → delete an exam
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExam(@PathVariable int id, Authentication authentication) {
        return ResponseEntity.ok(service.deleteExam(id, authentication));
    }
}