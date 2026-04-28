package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.*;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import com.springboot.online_exam_portal.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock private QuestionRepository questionRepo;
    @Mock private ExamsRepository examsRepo;
    @Mock private SubjectRepository subjectRepo;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private Exams exam;
    private Subject subject;

    @BeforeEach
    void setUp() {
        exam = new Exams();
        exam.setId(1);
        exam.setExamTitle("Java Basics");

        subject = new Subject();
        subject.setId(1);
        subject.setSubjectName("Java");
    }

    @Test
    void saveQuestionAndOptions_Success() {
        Questions question = new Questions();
        question.setQuestionText("What is JVM?");
        question.setQuestionType("MULTIPLE_CHOICE");
        question.setMarks(1);
        question.setExam(exam);
        question.setSubject(subject);

        Option opt1 = new Option(); opt1.setOptionText("Virtual Machine"); opt1.setIsCorrect(true);
        Option opt2 = new Option(); opt2.setOptionText("Compiler"); opt2.setIsCorrect(false);
        question.setOptions(new ArrayList<>(Arrays.asList(opt1, opt2)));

        when(examsRepo.findById(1)).thenReturn(Optional.of(exam));
        when(subjectRepo.findById(1)).thenReturn(Optional.of(subject));
        when(questionRepo.save(any(Questions.class))).thenAnswer(inv -> {
            Questions q = inv.getArgument(0);
            q.setId(1);
            return q;
        });

        Questions result = questionService.saveQuestionAndOptions(question);

        assertNotNull(result);
        assertEquals("What is JVM?", result.getQuestionText());
        assertEquals(2, result.getOptions().size());
        // verify options are linked back to the question
        result.getOptions().forEach(opt -> assertEquals(result, opt.getQuestion()));
    }

    @Test
    void saveQuestion_ExamNotFound_ThrowsException() {
        Questions question = new Questions();
        Exams fakeExam = new Exams(); fakeExam.setId(999);
        question.setExam(fakeExam);
        question.setSubject(subject);

        when(examsRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> questionService.saveQuestionAndOptions(question));
    }

    @Test
    void getQuestionById_Found() {
        Questions question = new Questions();
        question.setId(1);
        question.setQuestionText("What is OOP?");

        when(questionRepo.findById(1)).thenReturn(Optional.of(question));

        Questions result = questionService.getQuestionById(1);

        assertEquals("What is OOP?", result.getQuestionText());
    }

    @Test
    void getQuestionById_NotFound_ThrowsException() {
        when(questionRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> questionService.getQuestionById(99));
    }

    @Test
    void getQuestionsByExamId_ReturnsList() {
        Questions q1 = new Questions(); q1.setId(1); q1.setQuestionText("Q1");
        Questions q2 = new Questions(); q2.setId(2); q2.setQuestionText("Q2");

        when(questionRepo.findByExamId(1L)).thenReturn(Arrays.asList(q1, q2));

        List<Questions> result = questionService.getQuestionsByExamId(1L);

        assertEquals(2, result.size());
    }

    @Test
    void deleteQuestion_CallsRepository() {
        questionService.deleteQuestion(1);

        verify(questionRepo).deleteById(1);
    }
}
