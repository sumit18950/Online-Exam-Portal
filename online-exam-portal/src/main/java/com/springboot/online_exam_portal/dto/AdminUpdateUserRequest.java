package com.springboot.online_exam_portal.dto;

import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    private String username;
    private String email;
    private String role;
}

