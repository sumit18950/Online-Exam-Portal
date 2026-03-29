package com.springboot.online_exam_portal.controller;


import com.springboot.online_exam_portal.entity.Result;
import com.springboot.online_exam_portal.entity.StudentAnswer;
import com.springboot.online_exam_portal.service.AnswerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerServiceImpl answerService;

    @PostMapping("/selectanswer")
    public String submitAnswer(@RequestBody StudentAnswer answer){

        return answerService.submitAnswer(answer);
    }

    @PostMapping("/finish")
    public Result finishExam(@RequestParam Long examId,
                             @RequestParam Long userId){

        return answerService.evaluateExam(examId,userId);
    }
}

