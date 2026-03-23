package com.springboot.online_exam_portal.repository;

import com.springboot.online_exam_portal.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    List<StudentAnswer> findByExamIdAndUserId(Long examId, Long userId);
    StudentAnswer findByExamIdAndQuestionIdAndUserId(Long examId, Long questionId, Long userId);
}
