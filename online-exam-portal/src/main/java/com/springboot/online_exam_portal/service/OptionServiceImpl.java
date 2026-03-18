package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.repository.OptionRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionRepository optionRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Override
    public Option saveOption(Integer questionId, Option option) {
        Questions question = questionRepo.findById(Objects.requireNonNull(questionId))
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
        Option existing = optionRepo.findById(Objects.requireNonNull(optionId))
                .orElseThrow(() -> new RuntimeException("Option not found"));

        existing.setOption_text(optionDetails.getOption_text());
        existing.setIs_correct(optionDetails.getIs_correct());

        return optionRepo.save(existing);
    }

    @Override
    public void deleteOption(Integer optionId) {
        optionRepo.deleteById(Objects.requireNonNull(optionId));
    }
}