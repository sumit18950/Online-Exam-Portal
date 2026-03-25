package com.springboot.online_exam_portal.util;

import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.exception.UnauthorizedException;

public class RoleVerificationUtil {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TEACHER = "TEACHER";
    public static final String ROLE_USER = "USER";

    /**
     * Verify if user has admin role
     */
    public static boolean isAdmin(User user) {
        return user != null && user.getRole() != null 
               && ROLE_ADMIN.equals(user.getRole().getRoleName());
    }

    /**
     * Verify if user has teacher role
     */
    public static boolean isTeacher(User user) {
        return user != null && user.getRole() != null 
               && ROLE_TEACHER.equals(user.getRole().getRoleName());
    }

    /**
     * Verify if user has student/user role
     */
    public static boolean isStudent(User user) {
        return user != null && user.getRole() != null 
               && ROLE_USER.equals(user.getRole().getRoleName());
    }

    /**
     * Verify if user has any of the specified roles
     */
    public static boolean hasRole(User user, String... roles) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        String userRole = user.getRole().getRoleName();
        for (String role : roles) {
            if (role.equals(userRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enforce admin role - throws exception if user is not admin
     */
    public static void enforceAdmin(User user) {
        if (!isAdmin(user)) {
            throw new UnauthorizedException("Admin access required");
        }
    }

    /**
     * Enforce teacher role - throws exception if user is not teacher
     */
    public static void enforceTeacher(User user) {
        if (!isTeacher(user)) {
            throw new UnauthorizedException("Teacher access required");
        }
    }

    /**
     * Enforce admin or teacher role
     */
    public static void enforceAdminOrTeacher(User user) {
        if (!hasRole(user, ROLE_ADMIN, ROLE_TEACHER)) {
            throw new UnauthorizedException("Admin or Teacher access required");
        }
    }

    /**
     * Enforce student role
     */
    public static void enforceStudent(User user) {
        if (!isStudent(user)) {
            throw new UnauthorizedException("Student access required");
        }
    }
}

