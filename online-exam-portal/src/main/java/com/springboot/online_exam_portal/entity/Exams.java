package com.springboot.online_exam_portal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    
    private String examType; // MULTIPLE_CHOICE, DESCRIPTIVE, MIXED

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    private String status; // SCHEDULED, ONGOING, COMPLETED

    // COMMENT: @JsonBackReference = "child" side of the relationship.
    // When Exams is serialized, the "subject" field is SKIPPED to prevent
    // the circular loop: Subject→Exams→Subject→Exams→...
    // Subject details are included via ExamResponse DTO (subjectId, subjectName).
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}