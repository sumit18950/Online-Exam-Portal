package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.QuestionResponse;
import com.springboot.online_exam_portal.service.ConductExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/exam")
public class ConductExamController {

    @Autowired
    private ConductExamService examService;

    @PostMapping("/enroll")
    public String enroll(@RequestParam Long examId,
                         @RequestParam Long userId){

        return examService.enrollExam(examId,userId);
    }

    @GetMapping("/questions/{examId}/{userId}")
    public List<QuestionResponse> getQuestions(@PathVariable Long examId,
                                               @PathVariable Long userId){

        return examService.getQuestions(examId,userId);
    }
    @GetMapping("/timmer/{examId}")
    public Long getRemainingTime(@PathVariable Long examId){
        return examService.getRemainingTime(examId);
    }
}

