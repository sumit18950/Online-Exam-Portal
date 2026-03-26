package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired private SubjectRepository subjectRepo;
    @Autowired private ExamsRepository examRepo;

    @Override public Subject saveSubject(Subject subject) { return subjectRepo.save(Objects.requireNonNull(subject)); }

    @Override public List<Subject> getAllSubjects() { return subjectRepo.findAll(); }

    @Override public void deleteSubject(int id) { subjectRepo.deleteById(id); }

    @Override
    public Exams createExam(Exams exam, int subjectId) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        exam.setSubject(subject);
        return examRepo.save(exam);
    }

    @Override public List<Exams> getAllExams() { return examRepo.findAll(); }

    @Override
    public Exams updateExam(int id, Exams details) {
        Exams existing = examRepo.findById(id).orElseThrow(() -> new RuntimeException("Exam not found"));
        existing.setExamTitle(details.getExamTitle());
        existing.setDurationMinutes(details.getDurationMinutes());
        return examRepo.save(existing);
    }

    @Override public void deleteExam(int id) { examRepo.deleteById(id); }
}