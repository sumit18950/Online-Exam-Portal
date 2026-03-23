package com.springboot.online_exam_portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    private String examType;   // MULTIPLE_CHOICE, DESCRIPTIVE, MIXED

    private String status = "SCHEDULED"; // SCHEDULED, ONGOING, COMPLETED

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnore
    private Subject subject;

    /**
     * Automatically calculates and updates exam status based on current date/time
     * Called before persist and before update operations
     */
    @PrePersist
    @PreUpdate
    public void calculateStatus() {
        if (this.examDate == null || this.examTime == null || this.durationMinutes <= 0) {
            this.status = "SCHEDULED";
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime examStart = LocalDateTime.of(this.examDate, this.examTime);
        LocalDateTime examEnd = examStart.plusMinutes(this.durationMinutes);

        if (now.isBefore(examStart)) {
            this.status = "SCHEDULED";
        } else if (now.isAfter(examEnd)) {
            this.status = "COMPLETED";
        } else {
            this.status = "ONGOING";
        }
    }
}