package com.springboot.online_exam_portal.resultAnalysis.repository;
import com.springboot.online_exam_portal.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository
        extends JpaRepository<Result, Long> {
    List<Result> findByUserId(Long userId);

    List<Result> findByExamId(Long examId);

    List<Result> findByExamIdOrderByScoreDesc(Long examId);
}
