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
@CrossOrigin("*") // Useful for frontend connection
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/upload")
    public ResponseEntity<Questions> upload(@RequestBody Questions question) {
        return new ResponseEntity<>(questionService.saveQuestionAndOptions(question), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Questions> getAll() {
        return questionService.getAllQuestions();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Questions> update(@PathVariable Integer id, @RequestBody Questions question) {
        return ResponseEntity.ok(questionService.updateQuestion(id, question));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}

