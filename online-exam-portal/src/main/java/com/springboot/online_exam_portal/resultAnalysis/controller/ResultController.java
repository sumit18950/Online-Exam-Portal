package com.springboot.online_exam_portal.resultAnalysis.controller;

import java.util.List;

import com.springboot.online_exam_portal.resultAnalysis.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ResultResponseDTO>> getAllResults() {

        return ResponseEntity.ok(resultService.getAllResults());
    }


    // GET /results/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDTO> getResultById(@PathVariable Long id) {

        return ResponseEntity.ok(resultService.getResultById(id));
    }


    // GET /results/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResultResponseDTO>> getResultsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(resultService.getResultsByUserId(userId));
    }


    // GET /results/exam/{examId}
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ResultResponseDTO>> getResultsByExamId(
            @PathVariable Long examId) {

        return ResponseEntity.ok(resultService.getResultsByExamId(examId));
    }


    // GET /results/leaderboard/{examId}
    @GetMapping("/leaderboard/{examId}")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard(
            @PathVariable Long examId) {

        return ResponseEntity.ok(resultService.getLeaderboardByExamId(examId));
    }

    @PutMapping("/review")
    public ResponseEntity<ResultResponseDTO> reviewResult(

            @RequestBody ResultReviewRequestDTO request) {

        return ResponseEntity.ok(resultService.reviewResult(request));

    }

    @GetMapping("/review-history/{resultId}")
    public ResponseEntity<List<ScoreReviewHistoryDTO>> getReviewHistory(
            @PathVariable Long resultId) {

        return ResponseEntity.ok(resultService.getReviewHistory(resultId));
    }

    @GetMapping("/certificate/{resultId}")
    public ResponseEntity<ResultCertificateDTO> getCertificate(
            @PathVariable Long resultId) {

        return ResponseEntity.ok(resultService.getCertificate(resultId));
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