package com.springboot.online_exam_portal.dto;

import java.util.List;

public class QuestionResponse {

    private Long questionId;
    private String questionText;
    private String questionType;
    private Integer marks;

    private List<OptionResponse> options;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponse> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "QuestionResponse [questionId=" + questionId + ", questionText=" + questionText + ", questionType="
                + questionType + ", marks=" + marks + ", options=" + options + "]";
    }


}

