package com.springboot.online_exam_portal.controller;
import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.service.OptionService;
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

    // Create a single option for an existing question
    @PostMapping("/question/{questionId}")
    public ResponseEntity<Option> addOption(@PathVariable Integer questionId, @RequestBody Option option) {
        return ResponseEntity.ok(optionService.saveOption(questionId, option));
    }

    // Get all options for a specific question
    @GetMapping("/question/{questionId}")
    public List<Option> getByQuestion(@PathVariable Integer questionId) {
        return optionService.getOptionsByQuestion(questionId);
    }

    // Update an option by its own ID
    @PutMapping("/{id}")
    public ResponseEntity<Option> update(@PathVariable Integer id, @RequestBody Option option) {
        return ResponseEntity.ok(optionService.updateOption(id, option));
    }

    // Delete a specific option
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        optionService.deleteOption(id);
        return ResponseEntity.ok("Option deleted successfully");
    }
}
