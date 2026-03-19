package com.springboot.online_exam_portal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Exams exam;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Subject subject;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_type")
    private String questionType;

    private Integer marks;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options = new ArrayList<>();
}