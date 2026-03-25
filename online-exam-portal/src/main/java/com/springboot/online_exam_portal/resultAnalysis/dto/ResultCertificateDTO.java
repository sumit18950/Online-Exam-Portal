package com.springboot.online_exam_portal.resultAnalysis.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResultCertificateDTO {

    private String studentName;

    private String examTitle;

    private LocalDate examDate;

    private Integer score;

    private String grade;

    private String resultStatus;

    private LocalDateTime evaluatedAt;

    private String certificateText;

    public ResultCertificateDTO() {
    }

}