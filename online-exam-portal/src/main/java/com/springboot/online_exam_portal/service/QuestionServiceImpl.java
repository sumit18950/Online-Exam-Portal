package com.springboot.online_exam_portal.service;
import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Override
    @Transactional
    public Questions saveQuestionAndOptions(Questions question) {
        // Essential: Link every option to the question object
        if (question.getOptions() != null) {
            question.getOptions().forEach(option -> option.setQuestion(question));
        }
        return questionRepo.save(question);
    }

    @Override
    public List<Questions> getAllQuestions() {
        return questionRepo.findAll();
    }

    @Override
    public Questions getQuestionById(Integer id) {
        return questionRepo.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @Override
    @Transactional
    public Questions updateQuestion(Integer id, Questions questionDetails) {
        Questions existing = getQuestionById(id);

        existing.setQuestionText(questionDetails.getQuestionText());
        existing.setQuestionType(questionDetails.getQuestionType());
        existing.setDifficulty(questionDetails.getDifficulty());
        existing.setMarks(questionDetails.getMarks());

        // Update Options: Clear existing and add new list to maintain sync
        existing.getOptions().clear();
        if (questionDetails.getOptions() != null) {
            questionDetails.getOptions().forEach(opt -> {
                opt.setQuestion(existing);
                existing.getOptions().add(opt);
            });
        }
        return questionRepo.save(existing);
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionRepo.deleteById(id);
    }
}

