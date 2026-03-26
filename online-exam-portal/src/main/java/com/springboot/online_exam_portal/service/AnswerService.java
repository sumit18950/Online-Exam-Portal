package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Result;
import com.springboot.online_exam_portal.entity.StudentAnswer;

public interface AnswerService {
    public String submitAnswer(StudentAnswer answer);
    public Result evaluateExam(Long examId, Long userId);
}
