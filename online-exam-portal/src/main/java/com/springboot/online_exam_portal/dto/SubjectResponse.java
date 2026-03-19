package com.springboot.online_exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SubjectResponse DTO
 *
 * COMMENT: Exposes only flat subject fields to avoid lazy-loading errors
 * from Subject.exams when converting to JSON.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private int id;
    private String subjectName;
    private String description;
}

