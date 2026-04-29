package com.springboot.online_exam_portal.repository;

import com.springboot.online_exam_portal.entity.Exams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamsRepository extends JpaRepository<Exams, Integer> {
	long countBySubject_Id(int subjectId);
	long countBySubject_IdAndCreatedBy(int subjectId, int createdBy);
	java.util.List<Exams> findBySubject_Id(int subjectId);
}
