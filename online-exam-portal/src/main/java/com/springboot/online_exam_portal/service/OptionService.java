package com.springboot.online_exam_portal.service;


import com.springboot.online_exam_portal.entity.Option;
import java.util.List;

public interface OptionService {
    Option saveOption(Integer questionId, Option option);
    List<Option> getOptionsByQuestion(Integer questionId);
    Option updateOption(Integer optionId, Option optionDetails);
    void deleteOption(Integer optionId);
}

