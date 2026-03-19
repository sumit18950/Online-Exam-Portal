package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.dto.SubjectRequest;
import java.util.List;

public interface ExamService {
    // Subject CRUD
    Subject saveSubject(Subject subject);
    // COMMENT: Accepts SubjectRequest DTO - supports both "subject" and "subjectName" JSON fields
    Subject saveSubjectFromRequest(SubjectRequest subjectRequest);
    List<Subject> getAllSubjects();
    void deleteSubject(int id);

    // Exam CRUD with Role-based Access
    Exams createExam(Exams exam, int subjectId);
    Exams createExamWithRequest(ExamRequest examRequest, Long userId);
    List<Exams> getAllExams();
    List<ExamResponse> getAllExamsWithResponse();
    ExamResponse getExamById(int id);
    Exams updateExam(int id, Exams examDetails);
    Exams updateExamWithRequest(int id, ExamRequest examRequest, Long userId);
    void deleteExam(int id);
    
    // Admin-specific methods
    List<ExamResponse> getOngoingExams();
    List<ExamResponse> getScheduledExams();
    ExamResponse getExamStatusByExamId(int examId);
    
    // Student/User methods
    void registerExam(int examId, Long userId);
    List<ExamResponse> getEnrolledExams(Long userId);
    List<ExamResponse> getAvailableExamsForRegistration(Long userId);
    boolean canAttemptExam(int examId, Long userId);
    
    // Utility methods
    String getExamStatus(int examId);
    void updateExamStatus(int examId, String status);
}


