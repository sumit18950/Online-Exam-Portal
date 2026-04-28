package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Result;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import com.springboot.online_exam_portal.resultAnalysis.dto.ResultResponseDTO;
import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import com.springboot.online_exam_portal.resultAnalysis.repository.ScoreReviewHistoryRepository;
import com.springboot.online_exam_portal.resultAnalysis.service.Impl.ResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultAnalysisServiceTest {

    @Mock private ResultRepository resultRepository;
    @Mock private UserRepository userRepository;
    @Mock private ExamsRepository examsRepository;
    @Mock private ScoreReviewHistoryRepository scoreReviewHistoryRepository;

    @InjectMocks
    private ResultServiceImpl resultService;

    private Result sampleResult;
    private User sampleUser;
    private Exams sampleExam;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("student1");

        sampleExam = new Exams();
        sampleExam.setId(1);
        sampleExam.setExamTitle("Java Exam");
        sampleExam.setExamDate(LocalDate.of(2026, 5, 1));

        sampleResult = new Result();
        sampleResult.setId(1L);
        sampleResult.setExamId(1L);
        sampleResult.setUserId(1L);
        sampleResult.setScore(85);
        sampleResult.setGrade("A+");
        sampleResult.setEvaluatedAt(LocalDateTime.now());
    }

    @Test
    void getAllResults_ReturnsList() {
        when(resultRepository.findAll()).thenReturn(Arrays.asList(sampleResult));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(examsRepository.findById(1)).thenReturn(Optional.of(sampleExam));

        List<ResultResponseDTO> results = resultService.getAllResults();

        assertEquals(1, results.size());
        assertEquals("student1", results.get(0).getUsername());
        assertEquals("Java Exam", results.get(0).getExamTitle());
    }

    @Test
    void getResultById_Found() {
        when(resultRepository.findById(1L)).thenReturn(Optional.of(sampleResult));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(examsRepository.findById(1)).thenReturn(Optional.of(sampleExam));

        ResultResponseDTO dto = resultService.getResultById(1L);

        assertEquals(85, dto.getScore());
        assertEquals("A+", dto.getGrade());
        assertEquals("student1", dto.getUsername());
    }

    @Test
    void getResultById_NotFound_ThrowsException() {
        when(resultRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> resultService.getResultById(99L));
    }

    @Test
    void getResultsByUserId_ReturnsList() {
        when(resultRepository.findByUserId(1L)).thenReturn(Arrays.asList(sampleResult));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(examsRepository.findById(1)).thenReturn(Optional.of(sampleExam));

        List<ResultResponseDTO> results = resultService.getResultsByUserId(1L);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getExamId());
    }

    @Test
    void getAllResults_DeletedExam_ShowsDeletedExam() {
        when(resultRepository.findAll()).thenReturn(Arrays.asList(sampleResult));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(examsRepository.findById(1)).thenReturn(Optional.empty());

        List<ResultResponseDTO> results = resultService.getAllResults();

        assertEquals(1, results.size());
        assertEquals("Deleted Exam", results.get(0).getExamTitle());
    }

    @Test
    void getResultsByExamId_ReturnsList() {
        when(resultRepository.findByExamId(1L)).thenReturn(Arrays.asList(sampleResult));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(examsRepository.findById(1)).thenReturn(Optional.of(sampleExam));

        List<ResultResponseDTO> results = resultService.getResultsByExamId(1L);

        assertEquals(1, results.size());
        assertEquals("Java Exam", results.get(0).getExamTitle());
    }
}
