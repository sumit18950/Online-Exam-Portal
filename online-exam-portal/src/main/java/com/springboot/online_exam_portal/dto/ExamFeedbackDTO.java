package com.springboot.online_exam_portal.dto;

import java.util.List;

public class ExamFeedbackDTO {

    private Long examId;
    private String examTitle;
    private int totalScore;
    private int obtainedScore;
    private String grade;
    private List<QuestionFeedback> questions;

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public int getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(int obtainedScore) { this.obtainedScore = obtainedScore; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public List<QuestionFeedback> getQuestions() { return questions; }
    public void setQuestions(List<QuestionFeedback> questions) { this.questions = questions; }

    public static class QuestionFeedback {
        private Long questionId;
        private String questionText;
        private int marks;
        private Long selectedOptionId;
        private Long correctOptionId;
        private boolean isCorrect;
        private List<OptionFeedback> options;

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }

        public int getMarks() { return marks; }
        public void setMarks(int marks) { this.marks = marks; }

        public Long getSelectedOptionId() { return selectedOptionId; }
        public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }

        public Long getCorrectOptionId() { return correctOptionId; }
        public void setCorrectOptionId(Long correctOptionId) { this.correctOptionId = correctOptionId; }

        public boolean getIsCorrect() { return isCorrect; }
        public void setIsCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }

        public List<OptionFeedback> getOptions() { return options; }
        public void setOptions(List<OptionFeedback> options) { this.options = options; }
    }

    public static class OptionFeedback {
        private Long optionId;
        private String optionText;
        private boolean isCorrect;

        public Long getOptionId() { return optionId; }
        public void setOptionId(Long optionId) { this.optionId = optionId; }

        public String getOptionText() { return optionText; }
        public void setOptionText(String optionText) { this.optionText = optionText; }

        public boolean getIsCorrect() { return isCorrect; }
        public void setIsCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }
    }
}
