package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.ExamRequest;
import com.springboot.online_exam_portal.dto.ExamResponse;
import com.springboot.online_exam_portal.dto.SubjectRequest;
import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.*;
import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamAndSubjectServiceTest {

    @Mock private SubjectRepository subjectRepo;
    @Mock private ExamsRepository examRepo;
    @Mock private QuestionRepository questionRepo;
    @Mock private UserRepository userRepo;
    @Mock private StudentAnswerRepository studentAnswerRepo;
    @Mock private ExamEnrollmentRepository examEnrollmentRepo;
    @Mock private ResultRepository resultRepo;

    @InjectMocks
    private ExamServiceImpl examService;

    private Subject subject;
    private User teacher;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(1);
        subject.setSubjectName("Java");
        subject.setDescription("Core Java");

        teacher = new User();
        teacher.setId(10L);
        teacher.setUsername("teacher1");
    }

    @Test
    void saveSubject_Success() {
        SubjectRequest request = new SubjectRequest();
        request.setSubjectName("Java");
        request.setDescription("Core Java");

        when(subjectRepo.save(any(Subject.class))).thenAnswer(inv -> {
            Subject s = inv.getArgument(0);
            s.setId(1);
            return s;
        });

        Subject result = examService.saveSubject(request);

        assertNotNull(result);
        assertEquals("Java", result.getSubjectName());
        verify(subjectRepo).save(any(Subject.class));
    }

    @Test
    void getAllSubjects_ReturnsList() {
        Subject s1 = new Subject(); s1.setId(1); s1.setSubjectName("Java");
        Subject s2 = new Subject(); s2.setId(2); s2.setSubjectName("Python");

        when(subjectRepo.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Subject> result = examService.getAllSubjects();

        assertEquals(2, result.size());
        assertEquals("Python", result.get(1).getSubjectName());
    }

    @Test
    void getSubjectById_Found() {
        when(subjectRepo.findById(1)).thenReturn(Optional.of(subject));

        Subject result = examService.getSubjectById(1);

        assertEquals("Java", result.getSubjectName());
    }

    @Test
    void getSubjectById_NotFound_ThrowsException() {
        when(subjectRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> examService.getSubjectById(99));
    }

    @Test
    void createExam_Success() {
        ExamRequest request = new ExamRequest();
        request.setExamTitle("Java Basics");
        request.setExamDate(LocalDate.of(2026, 5, 10));
        request.setExamTime(LocalTime.of(10, 0));
        request.setDurationMinutes(60);
        request.setSubjectId(1);
        request.setCreatedBy(10L);
        request.setExamType("MULTIPLE_CHOICE");

        when(subjectRepo.findById(1)).thenReturn(Optional.of(subject));
        when(userRepo.findById(10L)).thenReturn(Optional.of(teacher));
        when(examRepo.save(any(Exams.class))).thenAnswer(inv -> {
            Exams e = inv.getArgument(0);
            e.setId(1);
            return e;
        });

        ExamResponse response = examService.createExam(request);

        assertNotNull(response);
        assertEquals("Java Basics", response.getExamTitle());
        assertEquals("SCHEDULED", response.getStatus());
        verify(examRepo).save(any(Exams.class));
    }

    @Test
    void createExam_SubjectNotFound_ThrowsException() {
        ExamRequest request = new ExamRequest();
        request.setSubjectId(999);
        request.setCreatedBy(10L);

        when(subjectRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> examService.createExam(request));
    }
}
