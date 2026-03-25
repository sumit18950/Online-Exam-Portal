package com.springboot.online_exam_portal.repository;
import com.springboot.online_exam_portal.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findBySubjectId(Integer subjectId);
    List<Questions> findByExamId(Long examId);
    long countBySubject_Id(Integer subjectId);
    void deleteBySubject_Id(Integer subjectId);
}
