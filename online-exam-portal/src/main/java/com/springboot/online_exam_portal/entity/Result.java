package com.springboot.online_exam_portal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="exam_id")
    private Long examId;

    @Column(name="user_id")
    private Long userId;

    private Integer score;

    private String grade;

    @Column(name="evaluated_at")
    private LocalDateTime evaluatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    @Override
    public String toString() {
        return "Result [id=" + id + ", examId=" + examId + ", userId=" + userId + ", score=" + score + ", grade="
                + grade + ", evaluatedAt=" + evaluatedAt + "]";
    }

}
