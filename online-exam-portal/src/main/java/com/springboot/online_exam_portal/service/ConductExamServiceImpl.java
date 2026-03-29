package com.springboot.online_exam_portal.service;
import com.springboot.online_exam_portal.dto.OptionResponse;
import com.springboot.online_exam_portal.dto.QuestionResponse;
import com.springboot.online_exam_portal.entity.ExamEnrollment;
import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Option;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.repository.ExamEnrollmentRepository;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.OptionRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConductExamServiceImpl implements ConductExamService {

    @Autowired
    private ExamEnrollmentRepository enrollmentRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private OptionRepository optionRepo;

    @Autowired
    private ExamsRepository examRepo;

    public String enrollExam(Long examId, Long userId){

        if(enrollmentRepo.existsByExamIdAndUserId(examId,userId)){
            return "Already enrolled";
        }

        ExamEnrollment enrollment = new ExamEnrollment();
        enrollment.setExamId(examId);
        enrollment.setUserId(userId);
        enrollment.setEnrolledAt(LocalDateTime.now());

        enrollmentRepo.save(enrollment);

        return "Enrollment successful";
    }

    public List<QuestionResponse> getQuestions(Long examId, Long userId){

        if(!enrollmentRepo.existsByExamIdAndUserId(examId,userId)){
            throw new RuntimeException("Not enrolled in this exam");
        }
        Exams exam =examRepo.findById(examId.intValue()).orElseThrow(()-> new RuntimeException("Exam not found"));
        LocalDateTime startTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
        if(startTime.isAfter(LocalDateTime.now())){
            throw new RuntimeException("Exam has not started yet");
        }
        LocalDateTime endTime = startTime.plusMinutes(exam.getDurationMinutes());
        if(endTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("Exam has already ended");
        }

        List<Questions> questions = questionRepo.findByExamId(examId);

        List<QuestionResponse> responseList = new ArrayList<>();

        for(Questions q : questions){

            QuestionResponse qr = new QuestionResponse();
            qr.setQuestionId(Long.valueOf(q.getId()));
            qr.setQuestionText(q.getQuestionText());
            qr.setQuestionType(q.getQuestionType());
            qr.setMarks(q.getMarks());

            List<Option> options = optionRepo.findByQuestionId(q.getId());

            List<OptionResponse> optionResponses = new ArrayList<>();

            for(Option op : options){
                OptionResponse or = new OptionResponse();
                or.setOptionId(Long.valueOf(op.getId()));
                or.setOptionText(op.getOptionText());
                optionResponses.add(or);
            }

            qr.setOptions(optionResponses);
            responseList.add(qr);
        }

        return responseList;
    }

    public Long getRemainingTime(Long examId) {
        Exams exam =examRepo.findById(Math.toIntExact(examId)).orElseThrow(()-> new RuntimeException("Exam not found"));
        LocalDateTime startTime = LocalDateTime.of(exam.getExamDate(), exam.getExamTime());
        LocalDateTime endTime = startTime.plusMinutes(exam.getDurationMinutes());
        if(endTime.isBefore(LocalDateTime.now())){
            return 0L;
        }
        return Duration.between(LocalDateTime.now(),endTime).toMinutes();
    }
}
