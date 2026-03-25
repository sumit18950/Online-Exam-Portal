package com.springboot.online_exam_portal.resultAnalysis.dto;

public class ResultReviewRequestDTO {

    private Long resultId;

    private Double newScore;
    private String newGrade;

    private String reviewReason;

    private Long reviewedBy;

    public ResultReviewRequestDTO() {
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Double getNewScore() {
        return newScore;
    }

    public void setNewScore(Double newScore) {
        this.newScore = newScore;
    }

    public String getNewGrade() {
        return newGrade;
    }

    public void setNewGrade(String newGrade) {
        this.newGrade = newGrade;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}
