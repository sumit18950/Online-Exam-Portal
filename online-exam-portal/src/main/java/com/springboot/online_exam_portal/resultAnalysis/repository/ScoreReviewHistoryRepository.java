package com.springboot.online_exam_portal.resultAnalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.online_exam_portal.resultAnalysis.entity.ScoreReviewHistory;

import java.util.List;

public interface ScoreReviewHistoryRepository
        extends JpaRepository<ScoreReviewHistory, Long> {

   // ResultResponseDTO reviewResult(ResultReviewRequestDTO request);
    List<ScoreReviewHistory> findByResultId(Long resultId);
}