package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.entity.Subject;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExamService {
    // Subject CRUD
    Subject saveSubject(Subject subject);
    List<Subject> getAllSubjects();
    Subject getSubjectById(int id);
    String deleteSubject(int id, Authentication authentication);

    // Exam CRUD
    ExamResponse createExam(ExamRequest request);
    List<ExamResponse> getAllExams();
    ExamResponse getExamById(int id);
    ExamResponse updateExam(int id, ExamRequest request);
    String deleteExam(int id, Authentication authentication);
}