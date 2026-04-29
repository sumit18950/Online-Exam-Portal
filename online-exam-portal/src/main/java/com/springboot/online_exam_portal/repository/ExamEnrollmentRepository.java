package com.springboot.online_exam_portal.repository;

import com.springboot.online_exam_portal.entity.ExamEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamEnrollmentRepository extends JpaRepository<ExamEnrollment, Long> {
    List<ExamEnrollment> findByUserId(Long userId);
    List<ExamEnrollment> findByExamId(Long examId);
    boolean existsByExamIdAndUserId(long examId, Long userId);
    void deleteByExamId(Long examId);
    void deleteByUserId(Long userId);
}

