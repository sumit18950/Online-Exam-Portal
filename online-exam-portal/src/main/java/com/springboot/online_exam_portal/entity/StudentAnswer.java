package com.springboot.online_exam_portal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="student_answers")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="exam_id")
    private Long examId;

    @Column(name="question_id")
    private Long questionId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="selected_option_id")
    private Long selectedOptionId;

    @Column(name="answer_text")
    private String answerText;

    @Column(name="submitted_at")
    private LocalDateTime submittedAt;

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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(Long selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public String toString() {
        return "StudentAnswer [id=" + id + ", examId=" + examId + ", questionId=" + questionId + ", userId=" + userId
                + ", selectedOptionId=" + selectedOptionId + ", answerText=" + answerText + ", submittedAt="
                + submittedAt + "]";
    }


}
