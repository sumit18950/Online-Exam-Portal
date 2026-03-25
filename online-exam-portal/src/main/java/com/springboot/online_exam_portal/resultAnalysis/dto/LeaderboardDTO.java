package com.springboot.online_exam_portal.resultAnalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardDTO {

    private String username;
    private String examTitle;

    private Integer score;
    private String grade;

    public LeaderboardDTO() {
    }


}