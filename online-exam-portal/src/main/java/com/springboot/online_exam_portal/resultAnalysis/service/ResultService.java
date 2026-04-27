package com.springboot.online_exam_portal.resultAnalysis.service;

import java.util.List;

import com.springboot.online_exam_portal.resultAnalysis.dto.*;

public interface ResultService {

    List<ResultResponseDTO> getAllResults();

    ResultResponseDTO getResultById(Long id);

    List<ResultResponseDTO> getResultsByUserId(Long userId);

    List<ResultResponseDTO> getResultsByExamId(Long examId);

    List<LeaderboardDTO> getLeaderboardByExamId(Long examId);

    ResultResponseDTO reviewResult(ResultReviewRequestDTO request);

    List<ScoreReviewHistoryDTO> getReviewHistory(Long resultId);

    ResultCertificateDTO getCertificate(Long resultId);


    byte[] exportResultsByExam(Long examId);
}