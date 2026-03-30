package com.springboot.online_exam_portal.resultAnalysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "score_review_history")
@Getter
@Setter
@ToString
public class ScoreReviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resultId;

    private Long reviewedBy;

    private Double oldScore;

    private Double newScore;

    private String oldGrade;

    private String newGrade;

    private String reviewReason;

    private LocalDateTime reviewedAt;
}