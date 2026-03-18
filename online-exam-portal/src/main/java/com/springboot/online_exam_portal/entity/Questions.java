package com.springboot.online_exam_portal.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name="Questions")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String questionText;
    private String questionType;
    private String difficulty;
    private Integer marks;
    private Integer createdBy;


    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;

    @OneToMany(mappedBy="question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options = new ArrayList<Option>();
}