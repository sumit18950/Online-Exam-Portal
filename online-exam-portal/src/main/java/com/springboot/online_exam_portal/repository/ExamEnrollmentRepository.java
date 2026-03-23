package com.springboot.online_exam_portal.repository;

import com.springboot.online_exam_portal.entity.ExamEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamEnrollmentRepository extends JpaRepository<ExamEnrollment, Long> {

    boolean existsByExamIdAndUserId(Long examId, Long userId);
}
