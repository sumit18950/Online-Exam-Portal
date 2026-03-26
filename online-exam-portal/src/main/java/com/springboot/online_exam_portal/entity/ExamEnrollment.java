package com.springboot.online_exam_portal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_enrollment")
public class ExamEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="exam_id")
    private Long examId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="enrolled_at")
    private LocalDateTime enrolledAt;

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

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    @Override
    public String toString() {
        return "ExamEnrollment{" +
                "id=" + id +
                ", examId=" + examId +
                ", userId=" + userId +
                ", enrolledAt=" + enrolledAt +
                '}';
    }
}
