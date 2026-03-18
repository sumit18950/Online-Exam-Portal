package com.springboot.online_exam_portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String examTitle;
    private LocalDate examDate;
    private LocalTime examTime;
    private int durationMinutes;
    private int createdBy;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}