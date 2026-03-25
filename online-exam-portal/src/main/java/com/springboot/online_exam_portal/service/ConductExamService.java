package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.QuestionResponse;

import java.util.List;

public interface ConductExamService {
    public String enrollExam(Long examId, Long userId);
    public List<QuestionResponse> getQuestions(Long examId, Long userId);
    public Long getRemainingTime(Long examId);
}

