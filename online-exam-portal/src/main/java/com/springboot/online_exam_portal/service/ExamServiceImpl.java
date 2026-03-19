package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.*;
import com.springboot.online_exam_portal.repository.*;
import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.dto.SubjectRequest;
import com.springboot.online_exam_portal.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired private SubjectRepository subjectRepo;
    @Autowired private ExamsRepository examRepo;
    @Autowired private RoleRepository roleRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ExamEnrollmentRepository examEnrollmentRepo;

    // ==================== Subject CRUD ====================
    @Override 
    public Subject saveSubject(Subject subject) { 
        return subjectRepo.save(subject); 
    }

    /**
     * COMMENT: Accepts SubjectRequest DTO where JSON field can be "subject" OR "subjectName"
     * Maps it to Subject entity and saves to database
     */
    @Override
    public Subject saveSubjectFromRequest(SubjectRequest subjectRequest) {
        // COMMENT: Validate that subjectName is not null/empty
        if (subjectRequest.getSubjectName() == null || subjectRequest.getSubjectName().isBlank()) {
            throw new RuntimeException("Subject name cannot be null or empty. Use field 'subject' or 'subjectName' in your request body.");
        }
        // COMMENT: Map DTO to entity
        Subject subject = new Subject();
        subject.setSubjectName(subjectRequest.getSubjectName());
        subject.setDescription(subjectRequest.getDescription());
        // COMMENT: Save and return the created subject
        return subjectRepo.save(subject);
    }

    @Override 
    public List<Subject> getAllSubjects() { 
        return subjectRepo.findAll(); 
    }

    @Override 
    public void deleteSubject(int id) { 
        subjectRepo.deleteById(id); 
    }

    // ==================== Exam CRUD ====================
    @Override
    public Exams createExam(Exams exam, int subjectId) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        exam.setSubject(subject);
        exam.setCreatedAt(LocalDateTime.now());
        exam.setUpdatedAt(LocalDateTime.now());
        exam.setStatus("SCHEDULED");
        return examRepo.save(exam);
    }

    @Override
    public Exams createExamWithRequest(ExamRequest examRequest, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String userRole = user.getRole().getRoleName();
        if (!userRole.equals("ADMIN") && !userRole.equals("TEACHER")) {
            throw new UnauthorizedException("Only Admin and Teacher can create exams");
        }

        Subject subject = subjectRepo.findById(examRequest.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Exams exam = new Exams();
        exam.setExamTitle(examRequest.getExamTitle());
        exam.setExamDate(examRequest.getExamDate());
        exam.setExamTime(examRequest.getExamTime());
        exam.setDurationMinutes(examRequest.getDurationMinutes());
        exam.setExamType(examRequest.getExamType());
        exam.setSubject(subject);
        exam.setCreatedBy(Math.toIntExact(userId));
        exam.setCreatedAt(LocalDateTime.now());
        exam.setUpdatedAt(LocalDateTime.now());
        exam.setStatus("SCHEDULED");

        return examRepo.save(exam);
    }

    @Override 
    public List<Exams> getAllExams() { 
        return examRepo.findAll(); 
    }

    @Override
    public List<ExamResponse> getAllExamsWithResponse() {
        return examRepo.findAll().stream()
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(int id) {
        Exams exam = examRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return convertToExamResponse(exam);
    }

    @Override
    public Exams updateExam(int id, Exams details) {
        Exams existing = examRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        existing.setExamTitle(details.getExamTitle());
        existing.setExamDate(details.getExamDate());
        existing.setExamTime(details.getExamTime());
        existing.setDurationMinutes(details.getDurationMinutes());
        existing.setExamType(details.getExamType());
        existing.setUpdatedAt(LocalDateTime.now());
        return examRepo.save(existing);
    }

    @Override
    public Exams updateExamWithRequest(int id, ExamRequest examRequest, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String userRole = user.getRole().getRoleName();
        if (!userRole.equals("ADMIN") && !userRole.equals("TEACHER")) {
            throw new UnauthorizedException("Only Admin and Teacher can update exams");
        }

        Exams existing = examRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // Check if user is the creator or an admin
        if (existing.getCreatedBy() != userId && !userRole.equals("ADMIN")) {
            throw new UnauthorizedException("You can only update exams you created");
        }

        existing.setExamTitle(examRequest.getExamTitle());
        existing.setExamDate(examRequest.getExamDate());
        existing.setExamTime(examRequest.getExamTime());
        existing.setDurationMinutes(examRequest.getDurationMinutes());
        existing.setExamType(examRequest.getExamType());
        existing.setUpdatedAt(LocalDateTime.now());

        if (examRequest.getSubjectId() != 0) {
            Subject subject = subjectRepo.findById(examRequest.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            existing.setSubject(subject);
        }

        return examRepo.save(existing);
    }

    @Override 
    public void deleteExam(int id) { 
        examRepo.deleteById(id); 
    }

    // ==================== Admin Methods ====================
    @Override
    public List<ExamResponse> getOngoingExams() {
        List<Exams> exams = examRepo.findAll();
        LocalDateTime now = LocalDateTime.now();
        return exams.stream()
                .filter(exam -> {
                    LocalDateTime examDateTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
                    LocalDateTime examEndTime = examDateTime.plusMinutes(exam.getDurationMinutes());
                    return now.isAfter(examDateTime) && now.isBefore(examEndTime);
                })
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponse> getScheduledExams() {
        List<Exams> exams = examRepo.findAll();
        LocalDateTime now = LocalDateTime.now();
        return exams.stream()
                .filter(exam -> {
                    LocalDateTime examDateTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
                    return examDateTime.isAfter(now);
                })
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamStatusByExamId(int examId) {
        Exams exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return convertToExamResponse(exam);
    }

    // ==================== Student/User Methods ====================
    @Override
    public void registerExam(int examId, Long userId) {
        Exams exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if exam is available for registration
        LocalDateTime examDateTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
        if (LocalDateTime.now().isAfter(examDateTime)) {
            throw new RuntimeException("Exam has already started. Cannot register now.");
        }

        // Check if already enrolled
        if (examEnrollmentRepo.existsByExamIdAndUserId(examId, userId)) {
            throw new RuntimeException("User is already registered for this exam");
        }

        ExamEnrollment enrollment = new ExamEnrollment();
        enrollment.setExamId((long) examId);
        enrollment.setUserId(userId);
        enrollment.setEnrolledAt(LocalDateTime.now());

        examEnrollmentRepo.save(enrollment);
    }

    @Override
    public List<ExamResponse> getEnrolledExams(Long userId) {
        List<ExamEnrollment> enrollments = examEnrollmentRepo.findByUserId(userId);
        return enrollments.stream()
                .map(enrollment -> {
                    Exams exam = examRepo.findById(Math.toIntExact(enrollment.getExamId()))
                            .orElse(null);
                    return exam != null ? convertToExamResponse(exam) : null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponse> getAvailableExamsForRegistration(Long userId) {
        List<ExamEnrollment> enrollments = examEnrollmentRepo.findByUserId(userId);
        List<Integer> enrolledExamIds = enrollments.stream()
                .map(e -> Math.toIntExact(e.getExamId()))
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        return examRepo.findAll().stream()
                .filter(exam -> {
                    LocalDateTime examDateTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
                    return !enrolledExamIds.contains(exam.getId()) 
                           && examDateTime.isAfter(now);
                })
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canAttemptExam(int examId, Long userId) {
        Exams exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // Check if user is enrolled
        if (!examEnrollmentRepo.existsByExamIdAndUserId(examId, userId)) {
            return false;
        }

        // Check if exam is currently happening
        LocalDateTime examDateTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
        LocalDateTime examEndTime = examDateTime.plusMinutes(exam.getDurationMinutes());
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(examDateTime) && now.isBefore(examEndTime);
    }

    // ==================== Utility Methods ====================
    @Override
    public String getExamStatus(int examId) {
        Exams exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return exam.getStatus();
    }

    @Override
    public void updateExamStatus(int examId, String status) {
        Exams exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        exam.setStatus(status);
        exam.setUpdatedAt(LocalDateTime.now());
        examRepo.save(exam);
    }

    // ==================== Helper Methods ====================
    private ExamResponse convertToExamResponse(Exams exam) {
        ExamResponse response = new ExamResponse();
        response.setId(exam.getId());
        response.setExamTitle(exam.getExamTitle());
        response.setExamDate(exam.getExamDate().toString());
        response.setExamTime(exam.getExamTime().toString());
        response.setDurationMinutes(exam.getDurationMinutes());
        response.setSubjectId(exam.getSubject().getId());
        response.setSubjectName(exam.getSubject().getSubjectName());
        response.setExamType(exam.getExamType());
        response.setCreatedBy(exam.getCreatedBy());
        response.setStatus(exam.getStatus());
        return response;
    }
}