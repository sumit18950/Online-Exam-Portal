package com.springboot.online_exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    private String examTitle;
    private LocalDate examDate;
    private LocalTime examTime;
    private int durationMinutes;
    private int subjectId;
    private Long createdBy;
    private String examType; // e.g., "MULTIPLE_CHOICE", "DESCRIPTIVE", "MIXED"
    private String status; // "SCHEDULED", "ONGOING", "COMPLETED"
}

