package com.springboot.online_exam_portal.resultAnalysis.controller;

import java.util.List;

import com.springboot.online_exam_portal.resultAnalysis.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.online_exam_portal.resultAnalysis.service.ResultService;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private ResultService resultService;


    // GET /results
    @GetMapping
    public List<ResultResponseDTO> getAllResults() {

        return resultService.getAllResults();
    }


    // GET /results/{id}
    @GetMapping("/{id}")
    public ResultResponseDTO getResultById(@PathVariable Long id) {

        return resultService.getResultById(id);
    }


    // GET /results/user/{userId}
    @GetMapping("/user/{userId}")
    public List<ResultResponseDTO> getResultsByUserId(
            @PathVariable Long userId) {

        return resultService.getResultsByUserId(userId);
    }


    // GET /results/exam/{examId}
    @GetMapping("/exam/{examId}")
    public List<ResultResponseDTO> getResultsByExamId(
            @PathVariable Long examId) {

        return resultService.getResultsByExamId(examId);
    }


    // GET /results/leaderboard/{examId}
    @GetMapping("/leaderboard/{examId}")
    public List<LeaderboardDTO> getLeaderboard(
            @PathVariable Long examId) {

        return resultService.getLeaderboardByExamId(examId);
    }

    @PutMapping("/review")
    public ResultResponseDTO reviewResult(

            @RequestBody ResultReviewRequestDTO request) {

        return resultService.reviewResult(request);

    }

    @GetMapping("/review-history/{resultId}")
    public List<ScoreReviewHistoryDTO> getReviewHistory(
            @PathVariable Long resultId) {

        return resultService.getReviewHistory(resultId);
    }

    @GetMapping("/certificate/{resultId}")
    public ResultCertificateDTO getCertificate(
            @PathVariable Long resultId) {

        return resultService.getCertificate(resultId);
    }

    @GetMapping("/export/exam/{examId}")
    public ResponseEntity<byte[]> exportResults(
            @PathVariable Long examId) {

        byte[] data =
                resultService.exportResultsByExam(examId);

        return ResponseEntity
                .ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=results.csv"
                )
                .header(
                        "Content-Type",
                        "text/csv"
                )
                .body(data);
    }
}