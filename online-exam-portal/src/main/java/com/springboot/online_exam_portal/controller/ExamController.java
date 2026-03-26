package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management")
public class ExamController {

    @Autowired private ExamService service;

    // Subject Endpoints
    @PostMapping("/subjects")
    public Subject addSubject(@RequestBody Subject s) { return service.saveSubject(s); }

    @DeleteMapping("/subjects/{id}")
    public String deleteSub(@PathVariable int id) {
        service.deleteSubject(id);
        return "Subject and its exams deleted.";
    }

    // Exam Endpoints
    @PostMapping("/exams/{subjectId}")
    public Exams addExam(@RequestBody Exams e, @PathVariable int subjectId) {
        return service.createExam(e, subjectId);
    }

    @GetMapping("/exams")
    public List<Exams> listExams() { return service.getAllExams(); }
}