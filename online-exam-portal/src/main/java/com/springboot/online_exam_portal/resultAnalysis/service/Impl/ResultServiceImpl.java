package com.springboot.online_exam_portal.resultAnalysis.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.springboot.online_exam_portal.resultAnalysis.repository.ScoreReviewHistoryRepository;
import com.springboot.online_exam_portal.resultAnalysis.dto.*;
import com.springboot.online_exam_portal.resultAnalysis.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.online_exam_portal.resultAnalysis.entity.ScoreReviewHistory;

import com.springboot.online_exam_portal.entity.Result;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.entity.Exams;

import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import com.springboot.online_exam_portal.repository.ExamsRepository;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamsRepository examsRepository;

    @Autowired
    private ScoreReviewHistoryRepository scoreReviewHistoryRepository;


    // convert to ResultResponseDTO
    private ResultResponseDTO convertToDTO(Result result) {

        ResultResponseDTO dto = new ResultResponseDTO();

        dto.setId(result.getId());
        dto.setExamId(result.getExamId());
        dto.setUserId(result.getUserId());
        dto.setScore(result.getScore());
        dto.setGrade(result.getGrade());
        dto.setEvaluatedAt(result.getEvaluatedAt());

        User user =
                userRepository.findById(result.getUserId()).orElseThrow(()-> new RuntimeException("User not found"));


        dto.setUsername(user.getUsername());

//
//        Exams exam =
//                examsRepository.findById (result.getExamId()).orElse(null);

        Integer examId = result.getExamId() == null ? null : Math.toIntExact(result.getExamId());
        Exams exam = examId == null ? null : examsRepository.findById(examId).orElseThrow(()->new RuntimeException("Exam not found"));


        dto.setExamTitle(exam.getExamTitle());


        return dto;
    }


    // convert to LeaderboardDTO
    private LeaderboardDTO convertToLeaderboardDTO(Result result) {

        LeaderboardDTO dto = new LeaderboardDTO();

        dto.setScore(result.getScore());
        dto.setGrade(result.getGrade());

        User user =
                userRepository.findById(result.getUserId()).orElseThrow(()->new RuntimeException("User not found"));


        dto.setUsername(user.getUsername());


        Integer examId = result.getExamId() == null ? null : Math.toIntExact(result.getExamId());
        Exams exam = examId == null ? null : examsRepository.findById(examId).orElseThrow(()->new RuntimeException("Exam not found"));


            dto.setExamTitle(exam.getExamTitle());


        return dto;
    }


    @Override
    public List<ResultResponseDTO> getAllResults() {

        return resultRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ResultResponseDTO getResultById(Long id) {

        Result result =
                resultRepository.findById(id).orElseThrow(()->new RuntimeException("Result of this ID doesn't exist"));

        //if (result == null) return null;

        return convertToDTO(result);
    }


    @Override
    public List<ResultResponseDTO> getResultsByUserId(Long userId) {

        return resultRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<ResultResponseDTO> getResultsByExamId(Long examId) {

        return resultRepository.findByExamId(examId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<LeaderboardDTO> getLeaderboardByExamId(Long examId) {

        return resultRepository
                .findByExamIdOrderByScoreDesc(examId)
                .stream()
                .map(this::convertToLeaderboardDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ResultResponseDTO reviewResult(ResultReviewRequestDTO request) {

        // 1. find result
        Result result =
                resultRepository.findById(request.getResultId())
                        .orElseThrow(()->new RuntimeException("Result of this ID doesn't exist"));


        // 2. store old values
        Integer oldScore = result.getScore();
        String oldGrade = result.getGrade();

        // 3. update result
        result.setScore(request.getNewScore().intValue());
        result.setGrade(request.getNewGrade());
        result.setEvaluatedAt(java.time.LocalDateTime.now());
        resultRepository.save(result);

        // 4. save history
        ScoreReviewHistory history = new ScoreReviewHistory();
        history.setResultId(result.getId());
        history.setReviewedBy(request.getReviewedBy());
        history.setOldScore(oldScore.doubleValue());
        history.setNewScore(request.getNewScore());
        history.setOldGrade(oldGrade);
        history.setNewGrade(request.getNewGrade());
        history.setReviewReason(request.getReviewReason());
        history.setReviewedAt(java.time.LocalDateTime.now());
        scoreReviewHistoryRepository.save(history);

        // 5. return updated dto
        return convertToDTO(result);

    }

    @Override
    public List<ScoreReviewHistoryDTO> getReviewHistory(Long resultId) {

        List<ScoreReviewHistory> historyList =
                scoreReviewHistoryRepository.findByResultId(resultId);

        List<ScoreReviewHistoryDTO> dtoList = new ArrayList<>();

        for (ScoreReviewHistory history : historyList) {

            ScoreReviewHistoryDTO dto = new ScoreReviewHistoryDTO();

            dto.setResultId(history.getResultId());
            dto.setReviewedBy(history.getReviewedBy());

            dto.setOldScore(history.getOldScore());
            dto.setNewScore(history.getNewScore());

            dto.setOldGrade(history.getOldGrade());
            dto.setNewGrade(history.getNewGrade());

            dto.setReviewReason(history.getReviewReason());

            dto.setReviewedAt(history.getReviewedAt());

            dtoList.add(dto);
        }

        return dtoList;
    }


    @Override
    public ResultCertificateDTO getCertificate(Long resultId) {

        Result result =
                resultRepository.findById(resultId).orElseThrow(()->new RuntimeException("Result of this ID doesn't exist"));

        User user =
                userRepository.findById(result.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

//        Exams exam =
//                examsRepository.findById(result.getExamId()).orElse(null);

        Integer examId = result.getExamId() == null ? null : Math.toIntExact(result.getExamId());
        Exams exam = examId == null ? null : examsRepository.findById(examId).orElseThrow(()->new RuntimeException("Exam not found"));


        ResultCertificateDTO dto = new ResultCertificateDTO();


        dto.setStudentName(user.getUsername());



        dto.setExamTitle(exam.getExamTitle());
        dto.setExamDate(exam.getExamDate());


        dto.setScore(result.getScore());
        dto.setGrade(result.getGrade());

        // PASS / FAIL logic
        if (result.getScore() >= 40) {
            dto.setResultStatus("PASS");
        } else {
            dto.setResultStatus("FAIL");
        }

        dto.setEvaluatedAt(result.getEvaluatedAt());

        // certificate message
        String text =
                "This is to certify that "
                        + dto.getStudentName()
                        + " has "
                        + dto.getResultStatus()
                        + " the exam "
                        + dto.getExamTitle()
                        + " with grade "
                        + dto.getGrade();

        dto.setCertificateText(text);

        return dto;
    }

    @Override
    public byte[] exportResultsByExam(Long examId) {

        List<Result> results =
                resultRepository.findByExamId(examId);

        StringBuilder sb = new StringBuilder();

        // header
        sb.append("ResultId,UserId,ExamId,Score,Grade\n");

        for (Result r : results) {

            sb.append(r.getId()).append(",");
            sb.append(r.getUserId()).append(",");
            sb.append(r.getExamId()).append(",");
            sb.append(r.getScore()).append(",");
            sb.append(r.getGrade()).append("\n");
        }

        return sb.toString().getBytes();
    }
}