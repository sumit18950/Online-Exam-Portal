package com.springboot.online_exam_portal.repository;
import com.springboot.online_exam_portal.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository
        extends JpaRepository<Result, Long> {
}
