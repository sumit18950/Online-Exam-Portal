package com.springboot.online_exam_portal.dto;

public class OptionResponse {

    private Long optionId;
    private String optionText;
    public Long getOptionId() {
        return optionId;
    }
    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }
    public String getOptionText() {
        return optionText;
    }
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
    @Override
    public String toString() {
        return "OptionResponse [optionId=" + optionId + ", optionText=" + optionText + "]";
    }


}
