package com.springboot.online_exam_portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty("isCorrect")
        private boolean correct;
        private List<OptionFeedback> options;
    }

    @Data
    public static class OptionFeedback {
        private Long optionId;
        private String optionText;
        @JsonProperty("isCorrect")
        private boolean correct;
    }
}
