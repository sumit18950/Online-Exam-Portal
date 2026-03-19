package com.springboot.online_exam_portal.repository;

import com.springboot.online_exam_portal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
