package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.*;
import com.springboot.online_exam_portal.repository.OptionRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import com.springboot.online_exam_portal.repository.StudentAnswerRepository;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamAttemptServiceTest {

    @Mock private StudentAnswerRepository answerRepo;
    @Mock private OptionRepository optionRepo;
    @Mock private ResultRepository resultRepo;
    @Mock private QuestionRepository questionRepo;
    @Mock private ExamsRepository examsRepo;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private StudentAnswer sampleAnswer;

    @BeforeEach
    void setUp() {
        sampleAnswer = new StudentAnswer();
        sampleAnswer.setExamId(1L);
        sampleAnswer.setQuestionId(1L);
        sampleAnswer.setUserId(1L);
        sampleAnswer.setSelectedOptionId(10L);
    }

    @Test
    void submitAnswer_NewAnswer_SavesSuccessfully() {
        when(answerRepo.findByExamIdAndQuestionIdAndUserId(1L, 1L, 1L)).thenReturn(null);
        when(answerRepo.save(any(StudentAnswer.class))).thenReturn(sampleAnswer);

        String result = answerService.submitAnswer(sampleAnswer);

        assertEquals("Answer saved", result);
        verify(answerRepo).save(sampleAnswer);
    }

    @Test
    void submitAnswer_ExistingAnswer_UpdatesSuccessfully() {
        StudentAnswer existing = new StudentAnswer();
        existing.setId(5L);
        existing.setExamId(1L);
        existing.setQuestionId(1L);
        existing.setUserId(1L);
        existing.setSelectedOptionId(9L);

        when(answerRepo.findByExamIdAndQuestionIdAndUserId(1L, 1L, 1L)).thenReturn(existing);
        when(answerRepo.save(any(StudentAnswer.class))).thenReturn(existing);

        String result = answerService.submitAnswer(sampleAnswer);

        assertEquals("Answer updated", result);
        assertEquals(10L, existing.getSelectedOptionId());
    }

    @Test
    void evaluateExam_AllCorrect_FullScore() {
        Questions q1 = new Questions(); q1.setId(1); q1.setMarks(5);
        Questions q2 = new Questions(); q2.setId(2); q2.setMarks(5);

        Option correctOpt1 = new Option(); correctOpt1.setIsCorrect(true);
        Option correctOpt2 = new Option(); correctOpt2.setIsCorrect(true);

        StudentAnswer a1 = new StudentAnswer(); a1.setSelectedOptionId(10L); a1.setQuestionId(1L);
        StudentAnswer a2 = new StudentAnswer(); a2.setSelectedOptionId(20L); a2.setQuestionId(2L);

        when(answerRepo.findByExamIdAndUserId(1L, 1L)).thenReturn(Arrays.asList(a1, a2));
        when(optionRepo.findById(10)).thenReturn(Optional.of(correctOpt1));
        when(optionRepo.findById(20)).thenReturn(Optional.of(correctOpt2));
        when(questionRepo.findById(1)).thenReturn(Optional.of(q1));
        when(questionRepo.findById(2)).thenReturn(Optional.of(q2));
        when(resultRepo.save(any(Result.class))).thenAnswer(inv -> inv.getArgument(0));

        Result result = answerService.evaluateExam(1L, 1L);

        assertEquals(10, result.getScore());
        assertEquals("O", result.getGrade());
    }

    @Test
    void evaluateExam_AllWrong_FailGrade() {
        Questions q1 = new Questions(); q1.setId(1); q1.setMarks(5);

        Option wrongOpt = new Option(); wrongOpt.setIsCorrect(false);

        StudentAnswer a1 = new StudentAnswer(); a1.setSelectedOptionId(10L); a1.setQuestionId(1L);

        when(answerRepo.findByExamIdAndUserId(1L, 1L)).thenReturn(Arrays.asList(a1));
        when(optionRepo.findById(10)).thenReturn(Optional.of(wrongOpt));
        when(questionRepo.findById(1)).thenReturn(Optional.of(q1));
        when(resultRepo.save(any(Result.class))).thenAnswer(inv -> inv.getArgument(0));

        Result result = answerService.evaluateExam(1L, 1L);

        assertEquals(0, result.getScore());
        assertEquals("FAIL", result.getGrade());
    }

    @Test
    void evaluateExam_PartialScore_CorrectGrade() {
        // 7 out of 10 = 70% = Grade A
        Questions q1 = new Questions(); q1.setId(1); q1.setMarks(7);
        Questions q2 = new Questions(); q2.setId(2); q2.setMarks(3);

        Option correctOpt = new Option(); correctOpt.setIsCorrect(true);
        Option wrongOpt = new Option(); wrongOpt.setIsCorrect(false);

        StudentAnswer a1 = new StudentAnswer(); a1.setSelectedOptionId(10L); a1.setQuestionId(1L);
        StudentAnswer a2 = new StudentAnswer(); a2.setSelectedOptionId(20L); a2.setQuestionId(2L);

        when(answerRepo.findByExamIdAndUserId(1L, 1L)).thenReturn(Arrays.asList(a1, a2));
        when(optionRepo.findById(10)).thenReturn(Optional.of(correctOpt));
        when(optionRepo.findById(20)).thenReturn(Optional.of(wrongOpt));
        when(questionRepo.findById(1)).thenReturn(Optional.of(q1));
        when(questionRepo.findById(2)).thenReturn(Optional.of(q2));
        when(resultRepo.save(any(Result.class))).thenAnswer(inv -> inv.getArgument(0));

        Result result = answerService.evaluateExam(1L, 1L);

        assertEquals(7, result.getScore());
        assertEquals("A", result.getGrade());
    }
}
