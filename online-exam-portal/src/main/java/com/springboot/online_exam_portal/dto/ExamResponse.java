package com.springboot.online_exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private int id;
    private String examTitle;
    private String examDate;
    private String examTime;
    private int durationMinutes;
    private int subjectId;
    private String subjectName;
    private String examType;
    private Long createdBy;
    private String status; // "SCHEDULED", "ONGOING", "COMPLETED"
}

