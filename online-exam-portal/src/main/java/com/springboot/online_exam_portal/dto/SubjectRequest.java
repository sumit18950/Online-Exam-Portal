package com.springboot.online_exam_portal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SubjectRequest DTO - Accepts flexible JSON field names for subject creation
 *
 * COMMENT: Accepts BOTH "subject" and "subjectName" as field name in JSON body
 * Example 1: { "subject": "Maths", "description": "This is maths subject" }
 * Example 2: { "subjectName": "Maths", "description": "This is maths subject" }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {

    /**
     * COMMENT: @JsonAlias allows "subject" OR "subjectName" to map to this field.
     * So both JSON formats work in Postman.
     */
    @JsonAlias({"subject", "subjectName"})
    private String subjectName;

    // COMMENT: Description of the subject
    private String description;
}

