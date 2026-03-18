package com.springboot.online_exam_portal.controller;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin("*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/upload")
    public ResponseEntity<Questions> upload(@RequestBody Questions question) {
        return new ResponseEntity<>(questionService.saveQuestionAndOptions(question), HttpStatus.CREATED);
    }

    // Get all questions by subject ID
    @GetMapping("/subject/{subjectId}")
    public List<Questions> getBySubjectId(@PathVariable Integer subjectId) {
        return questionService.getQuestionsBySubjectId(subjectId);
    }

    // Get a particular question by subject ID and question ID
    @GetMapping("/subject/{subjectId}/question/{questionId}")
    public ResponseEntity<Questions> getBySubjectIdAndQuestionId(
            @PathVariable Integer subjectId, @PathVariable Integer questionId) {
        Questions question = questionService.getQuestionById(questionId);
        if (question.getSubject() == null || question.getSubject().getId() != subjectId) {
            throw new RuntimeException("Question " + questionId + " does not belong to subject " + subjectId);
        }
        return ResponseEntity.ok(question);
    }

    // Update question by subject ID and question ID
    @PutMapping("/subject/{subjectId}/question/{questionId}")
    public ResponseEntity<Questions> update(
            @PathVariable Integer subjectId, @PathVariable Integer questionId,
            @RequestBody Questions question) {
        Questions existing = questionService.getQuestionById(questionId);
        if (existing.getSubject() == null || existing.getSubject().getId() != subjectId) {
            throw new RuntimeException("Question " + questionId + " does not belong to subject " + subjectId);
        }
        return ResponseEntity.ok(questionService.updateQuestion(questionId, question));
    }

    // Delete question by subject ID and question ID
    @DeleteMapping("/subject/{subjectId}/question/{questionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer subjectId, @PathVariable Integer questionId) {
        Questions existing = questionService.getQuestionById(questionId);
        if (existing.getSubject() == null || existing.getSubject().getId() != subjectId) {
            throw new RuntimeException("Question " + questionId + " does not belong to subject " + subjectId);
        }
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
