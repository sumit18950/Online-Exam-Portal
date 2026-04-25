package com.springboot.online_exam_portal.service;


import com.springboot.online_exam_portal.dto.ExamFeedbackDTO;
import com.springboot.online_exam_portal.entity.*;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.OptionRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import com.springboot.online_exam_portal.repository.StudentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ExamsRepository examsRepo;

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

    public ExamFeedbackDTO getExamFeedback(Long examId, Long userId) {
        // Get the exam
        Exams exam = examsRepo.findById(Math.toIntExact(examId)).orElse(null);
        if (exam == null) {
            throw new RuntimeException("Exam not found");
        }

        // Get all questions for this exam
        List<Questions> questions = questionRepo.findByExamId(examId);

        // Get all student answers for this exam
        List<StudentAnswer> studentAnswers = answerRepo.findByExamIdAndUserId(examId, userId);
        Map<Long, Long> answerMap = studentAnswers.stream()
                .collect(Collectors.toMap(StudentAnswer::getQuestionId, StudentAnswer::getSelectedOptionId));

        // Get the result
        List<Result> results = resultRepo.findByUserId(userId);
        Result result = results.stream()
                .filter(r -> r.getExamId().equals(examId))
                .findFirst().orElse(null);

        int totalScore = 0;
        int obtainedScore = 0;

        List<ExamFeedbackDTO.QuestionFeedback> questionFeedbacks = new ArrayList<>();

        for (Questions q : questions) {
            ExamFeedbackDTO.QuestionFeedback qf = new ExamFeedbackDTO.QuestionFeedback();
            qf.setQuestionId(q.getId().longValue());
            qf.setQuestionText(q.getQuestionText());
            qf.setMarks(q.getMarks() != null ? q.getMarks() : 0);
            totalScore += qf.getMarks();

            Long selectedOptionId = answerMap.get(q.getId().longValue());
            qf.setSelectedOptionId(selectedOptionId);

            // Build option feedbacks and find correct option
            Long correctOptionId = null;
            List<ExamFeedbackDTO.OptionFeedback> optionFeedbacks = new ArrayList<>();
            for (Option opt : q.getOptions()) {
                ExamFeedbackDTO.OptionFeedback of = new ExamFeedbackDTO.OptionFeedback();
                of.setOptionId(opt.getId().longValue());
                of.setOptionText(opt.getOptionText());
                of.setIsCorrect(opt.getIsCorrect() != null && opt.getIsCorrect());
                if (of.getIsCorrect()) {
                    correctOptionId = opt.getId().longValue();
                }
                optionFeedbacks.add(of);
            }
            qf.setOptions(optionFeedbacks);
            qf.setCorrectOptionId(correctOptionId);

            boolean isCorrect = selectedOptionId != null && selectedOptionId.equals(correctOptionId);
            qf.setIsCorrect(isCorrect);
            if (isCorrect) {
                obtainedScore += qf.getMarks();
            }

            questionFeedbacks.add(qf);
        }

        ExamFeedbackDTO dto = new ExamFeedbackDTO();
        dto.setExamId(examId);
        dto.setExamTitle(exam.getExamTitle());
        dto.setTotalScore(totalScore);
        dto.setObtainedScore(obtainedScore);
        dto.setGrade(result != null ? result.getGrade() : "N/A");
        dto.setQuestions(questionFeedbacks);

        return dto;
    }
}

