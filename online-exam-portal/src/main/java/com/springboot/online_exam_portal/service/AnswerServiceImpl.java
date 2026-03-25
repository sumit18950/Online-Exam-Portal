package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.entity.Result;
import com.springboot.online_exam_portal.entity.StudentAnswer;
import com.springboot.online_exam_portal.repository.OptionRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import com.springboot.online_exam_portal.repository.StudentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private StudentAnswerRepository answerRepo;

    @Autowired
    private OptionRepository optionRepo;

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private QuestionRepository questionRepo;

    public String submitAnswer(StudentAnswer answer){

        StudentAnswer existingAnswer =
                answerRepo.findByExamIdAndQuestionIdAndUserId(
                        answer.getExamId(),
                        answer.getQuestionId(),
                        answer.getUserId()
                );
        if(existingAnswer!=null){
            existingAnswer.setSelectedOptionId(answer.getSelectedOptionId());
            existingAnswer.setSubmittedAt(LocalDateTime.now());
            answerRepo.save(existingAnswer);
            return "Answer updated";
        }

        answer.setSubmittedAt(LocalDateTime.now());
        answerRepo.save(answer);

        return "Answer saved";
    }

    public Result evaluateExam(Long examId, Long userId){

        List<StudentAnswer> answers =
                answerRepo.findByExamIdAndUserId(examId,userId);

        Integer score = 0;
        Integer totalMarks = 0;

        for(StudentAnswer ans : answers){

            Option option =
                    optionRepo.findById(Math.toIntExact(ans.getSelectedOptionId())).orElse(null);
            Long questionId = ans.getQuestionId();
            Questions q = questionRepo.findById(Math.toIntExact(questionId)).orElse(null);
            Integer marks = q != null ? q.getMarks() : 0;
            totalMarks += marks;
            if(option != null && option.getIsCorrect()){
                score += marks;
            }
        }
        String grade="";
        if(score >=(0.90*totalMarks))
            grade="O";
        else if(score >=(0.80*totalMarks))
            grade="A+";
        else if(score >=(0.70*totalMarks))
            grade="A";
        else if(score >=(0.60*totalMarks))
            grade="B+";
        else if(score >=(0.50*totalMarks))
            grade="B";
        else if(score >=(0.40*totalMarks))
            grade="C";
        else
            grade="FAIL";

        Result result = new Result();
        result.setExamId(examId);
        result.setUserId(userId);
        result.setScore(score);
        result.setGrade(grade);
        result.setEvaluatedAt(LocalDateTime.now());

        return resultRepo.save(result);
    }
}
