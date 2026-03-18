package com.springboot.online_exam_portal.service;
import com.springboot.online_exam_portal.entity.Questions;
import java.util.List;

public interface QuestionService {
    Questions saveQuestionAndOptions(Questions question);
    List<Questions> getAllQuestions();
    Questions getQuestionById(Integer id);
    Questions updateQuestion(Integer id, Questions question);
    void deleteQuestion(Integer id);
}

