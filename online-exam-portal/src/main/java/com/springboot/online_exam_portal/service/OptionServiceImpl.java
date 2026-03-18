package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.repository.OptionRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionRepository optionRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Override
    public Option saveOption(Integer questionId, Option option) {
        Questions question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        option.setQuestion(question);
        return optionRepo.save(option);
    }

    @Override
    public List<Option> getOptionsByQuestion(Integer questionId) {
        return optionRepo.findByQuestionId(questionId);
    }

    @Override
    public Option updateOption(Integer optionId, Option optionDetails) {
        Option existing = optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        existing.setOptionText(optionDetails.getOptionText());
        existing.setIsCorrect(optionDetails.getIsCorrect());

        return optionRepo.save(existing);
    }

    @Override
    public void deleteOption(Integer optionId) {
        optionRepo.deleteById(optionId);
    }
}