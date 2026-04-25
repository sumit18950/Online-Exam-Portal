package com.springboot.online_exam_portal.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamFeedbackDTO {

    private Long examId;
    private String examTitle;
    private int totalScore;
    private int obtainedScore;
    private String grade;
    private List<QuestionFeedback> questions;

    @Data
    public static class QuestionFeedback {
        private Long questionId;
        private String questionText;
        private int marks;
        private Long selectedOptionId;
        private Long correctOptionId;
        private boolean isCorrect;
        private List<OptionFeedback> options;
    }

    @Data
    public static class OptionFeedback {
        private Long optionId;
        private String optionText;
        private boolean isCorrect;
    }
}
