package com.springboot.online_exam_portal.repository;

import com.springboot.online_exam_portal.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}