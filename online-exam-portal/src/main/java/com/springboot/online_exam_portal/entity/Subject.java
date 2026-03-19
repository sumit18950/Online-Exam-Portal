package com.springboot.online_exam_portal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String subjectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    // COMMENT: @JsonManagedReference = "parent" side. Exams list IS included in JSON output.
    // The "child" side (@JsonBackReference on Exams.subject) is automatically excluded
    // to prevent infinite loop: Subject→Exams→Subject→Exams→...
    @JsonManagedReference
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exams> exams;
}