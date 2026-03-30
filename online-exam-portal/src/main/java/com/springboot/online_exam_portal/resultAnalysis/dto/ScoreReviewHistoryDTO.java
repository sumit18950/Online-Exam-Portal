package com.springboot.online_exam_portal.resultAnalysis.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScoreReviewHistoryDTO {

    private Long id;
    private Long resultId;
    private Long reviewedBy;
    private Double oldScore;
    private Double newScore;
    private String oldGrade;
    private String newGrade;
    private String reviewReason;
    private LocalDateTime reviewedAt;

    // getters setters
}
