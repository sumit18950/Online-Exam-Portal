package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.dto.SubjectRequest;
import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import com.springboot.online_exam_portal.repository.SubjectRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class ExamServiceImpl implements ExamService {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_TEACHER = "ROLE_TEACHER";

    @Autowired private SubjectRepository subjectRepo;
    @Autowired private ExamsRepository examRepo;
    @Autowired private QuestionRepository questionRepo;
    @Autowired private UserRepository userRepo;

    // ─── Subject ────────────────────────────────────────────────────────────

    @Override
    public Subject saveSubject(SubjectRequest request) {
        Objects.requireNonNull(request, "Subject request cannot be null");
        Subject subject = new Subject();
        subject.setSubjectName(request.getSubjectName());
        subject.setDescription(request.getDescription());
        return subjectRepo.save(subject);
    }

    @Override
    @Transactional
    public Subject updateSubject(int id, SubjectRequest request) {
        Subject existing = subjectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found with id: " + id));
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject payload is required");
        }
        if (request.getSubjectName() != null && !request.getSubjectName().isBlank()) {
            existing.setSubjectName(request.getSubjectName());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }
        Subject saved = subjectRepo.save(existing);
        subjectRepo.flush();
        
        // Cascade update: notify all linked exams about subject change
        // (In a real system, you might emit an event or update a denormalized field)
        // For now, the relationship is maintained through the foreign key
        
        return saved;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepo.findAll();
    }

    @Override
    public Subject getSubjectById(int id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    @Override
    @Transactional
    public String deleteSubject(int id, Authentication authentication) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found with id: " + id));

        User currentUser = getCurrentUser(authentication);
        long linkedExamCount = examRepo.countBySubject_Id(id);
        long linkedQuestionCount = questionRepo.countBySubject_Id(id);

        if (hasRole(authentication, ROLE_ADMIN)) {
            if (linkedQuestionCount > 0) {
                questionRepo.deleteBySubject_Id(id);
            }
            subjectRepo.delete(subject);
            return "Subject deleted successfully. " + linkedExamCount + " linked exam(s) and "
                    + linkedQuestionCount + " linked question(s) were also deleted.";
        }

        if (!hasRole(authentication, ROLE_TEACHER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN or TEACHER can delete subjects");
        }

        long teacherOwnedExamCount = examRepo.countBySubject_IdAndCreatedBy_Id(id, currentUser.getId());
        if (linkedExamCount != teacherOwnedExamCount) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Teachers can delete only subjects whose linked exams were created by them");
        }

        if (linkedQuestionCount > 0) {
            questionRepo.deleteBySubject_Id(id);
        }
        subjectRepo.delete(subject);
        return "Subject deleted successfully. " + linkedExamCount + " linked exam(s) and "
                + linkedQuestionCount + " linked question(s) created under this subject were also deleted.";
    }

    // ─── Exam ────────────────────────────────────────────────────────────────

    @Override
    public ExamResponse createExam(ExamRequest request) {
        Subject subject = subjectRepo.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + request.getSubjectId()));

        Long createdById = Objects.requireNonNull(request.getCreatedBy(), "createdBy is required");
        User createdBy = userRepo.findById(createdById)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + createdById));

        Exams exam = new Exams();
        exam.setExamTitle(request.getExamTitle());
        exam.setExamDate(request.getExamDate());
        exam.setExamTime(request.getExamTime());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setExamType(request.getExamType());
        exam.setCreatedBy(createdBy);
        exam.setSubject(subject);
        exam.setStatus("SCHEDULED");

        return toResponse(examRepo.save(exam));
    }

    @Override
    public List<ExamResponse> getAllExams() {
        return examRepo.findAll().stream()
                .map(exam -> {
                    updateExamStatus(exam);
                    return toResponse(exam);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(int id) {
        Exams exam = examRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
        updateExamStatus(exam);
        return toResponse(exam);
    }

    @Override
    public ExamResponse updateExam(int id, ExamRequest request) {
        Exams existing = examRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));

        // Update subject only if provided
        if (request.getSubjectId() > 0) {
            Subject subject = subjectRepo.findById(request.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + request.getSubjectId()));
            existing.setSubject(subject);
        }

        // Update creator only if provided
        if (request.getCreatedBy() != null && request.getCreatedBy() > 0) {
            User createdBy = userRepo.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getCreatedBy()));
            existing.setCreatedBy(createdBy);
        }

        // Update all other fields if provided
        if (request.getExamTitle() != null && !request.getExamTitle().isBlank()) {
            existing.setExamTitle(request.getExamTitle());
        }
        if (request.getExamDate() != null) {
            existing.setExamDate(request.getExamDate());
        }
        if (request.getExamTime() != null) {
            existing.setExamTime(request.getExamTime());
        }
        if (request.getDurationMinutes() > 0) {
            existing.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getExamType() != null && !request.getExamType().isBlank()) {
            existing.setExamType(request.getExamType());
        }

        Exams saved = examRepo.save(existing);
        updateExamStatus(saved);
        return toResponse(saved);
    }

    @Override
    public String deleteExam(int id, Authentication authentication) {
        Exams exam = examRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found with id: " + id));

        if (hasRole(authentication, ROLE_ADMIN)) {
            examRepo.delete(exam);
            return "Exam deleted successfully by admin.";
        }

        if (!hasRole(authentication, ROLE_TEACHER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN or TEACHER can delete exams");
        }

        User currentUser = getCurrentUser(authentication);
        Long examOwnerId = exam.getCreatedBy() != null ? exam.getCreatedBy().getId() : null;
        if (!Objects.equals(examOwnerId, currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Teachers can delete only exams created by them");
        }

        examRepo.delete(exam);
        return "Exam deleted successfully.";
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    /**
     * Updates exam status based on current time and persists to database
     */
    private void updateExamStatus(Exams exam) {
        if (exam == null || exam.getExamDate() == null || exam.getExamTime() == null || exam.getDurationMinutes() <= 0) {
            exam.setStatus("SCHEDULED");
            examRepo.save(exam);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime examStart = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
        LocalDateTime examEnd = examStart.plusMinutes(exam.getDurationMinutes());

        String newStatus;
        if (now.isBefore(examStart)) {
            newStatus = "SCHEDULED";
        } else if (now.isAfter(examEnd)) {
            newStatus = "COMPLETED";
        } else {
            newStatus = "ONGOING";
        }

        if (!newStatus.equals(exam.getStatus())) {
            exam.setStatus(newStatus);
            examRepo.save(exam);
        }
    }

    private ExamResponse toResponse(Exams exam) {
        ExamResponse r = new ExamResponse();
        r.setId(exam.getId());
        r.setExamTitle(exam.getExamTitle());
        r.setExamDate(exam.getExamDate() != null ? exam.getExamDate().toString() : null);
        r.setExamTime(exam.getExamTime() != null ? exam.getExamTime().toString() : null);
        r.setDurationMinutes(exam.getDurationMinutes());
        r.setExamType(exam.getExamType());
        r.setCreatedBy(exam.getCreatedBy() != null ? exam.getCreatedBy().getId() : null);
        r.setStatus(exam.getStatus());
        if (exam.getSubject() != null) {
            r.setSubjectId(exam.getSubject().getId());
            r.setSubjectName(exam.getSubject().getSubjectName());
        }
        return r;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(a -> role.equals(a.getAuthority().toUpperCase(Locale.ROOT)));
    }
}