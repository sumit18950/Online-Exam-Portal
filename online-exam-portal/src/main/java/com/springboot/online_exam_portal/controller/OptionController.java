package com.springboot.online_exam_portal.controller;
import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.service.OptionService;
import com.springboot.online_exam_portal.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@CrossOrigin("*")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @Autowired
    private QuestionService questionService;

    // Validate that question belongs to the subject
    private Questions validateSubjectQuestion(Integer subjectId, Integer questionId) {
        Questions question = questionService.getQuestionById(questionId);
        if (question.getSubject() == null || question.getSubject().getId() != subjectId) {
            throw new RuntimeException("Question " + questionId + " does not belong to subject " + subjectId);
        }
        return question;
    }

    // Add a single option to a question
    @PostMapping("/subject/{subjectId}/question/{questionId}")
    public ResponseEntity<Option> addOption(
            @PathVariable Integer subjectId, @PathVariable Integer questionId,
            @RequestBody Option option) {
        validateSubjectQuestion(subjectId, questionId);
        return ResponseEntity.ok(optionService.saveOption(questionId, option));
    }

    // Get all options for a question
    @GetMapping("/subject/{subjectId}/question/{questionId}")
    public List<Option> getByQuestion(
            @PathVariable Integer subjectId, @PathVariable Integer questionId) {
        validateSubjectQuestion(subjectId, questionId);
        return optionService.getOptionsByQuestion(questionId);
    }

    // Update a single option by its ID
    @PutMapping("/subject/{subjectId}/question/{questionId}/option/{optionId}")
    public ResponseEntity<Option> update(
            @PathVariable Integer subjectId, @PathVariable Integer questionId,
            @PathVariable Integer optionId, @RequestBody Option option) {
        validateSubjectQuestion(subjectId, questionId);
        return ResponseEntity.ok(optionService.updateOption(optionId, option));
    }

    // Update all options of a question at once
    @PutMapping("/subject/{subjectId}/question/{questionId}")
    public ResponseEntity<List<Option>> updateAll(
            @PathVariable Integer subjectId, @PathVariable Integer questionId,
            @RequestBody List<Option> options) {
        validateSubjectQuestion(subjectId, questionId);
        return ResponseEntity.ok(optionService.updateAllOptions(questionId, options));
    }

    // Delete a specific option
    @DeleteMapping("/subject/{subjectId}/question/{questionId}/option/{optionId}")
    public ResponseEntity<String> delete(
            @PathVariable Integer subjectId, @PathVariable Integer questionId,
            @PathVariable Integer optionId) {
        validateSubjectQuestion(subjectId, questionId);
        optionService.deleteOption(optionId);
        return ResponseEntity.ok("Option deleted successfully");
    }
}
