package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import java.util.List;

public interface ExamService {
    // Subject CRUD
    Subject saveSubject(Subject subject);
    List<Subject> getAllSubjects();
    void deleteSubject(int id);

    // Exam CRUD
    Exams createExam(Exams exam, int subjectId);
    List<Exams> getAllExams();
    Exams updateExam(int id, Exams examDetails);
    void deleteExam(int id);
}