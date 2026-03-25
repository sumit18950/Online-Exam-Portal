package com.springboot.online_exam_portal.resultAnalysis.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ResultResponseDTO {

    private Long id;

    private Long examId;
    private String examTitle;

    private Long userId;
    private String username;

    private Integer score;
    private String grade;

    private LocalDateTime evaluatedAt;

    public ResultResponseDTO() {
    }
}
