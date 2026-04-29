package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.*;
import com.springboot.online_exam_portal.entity.Role;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.entity.Result;
import com.springboot.online_exam_portal.repository.ExamEnrollmentRepository;
import com.springboot.online_exam_portal.repository.RoleRepository;
import com.springboot.online_exam_portal.repository.StudentAnswerRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import com.springboot.online_exam_portal.resultAnalysis.repository.ResultRepository;
import com.springboot.online_exam_portal.resultAnalysis.repository.ScoreReviewHistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_TEACHER = "ROLE_TEACHER";
    private static final String ROLE_STUDENT = "STUDENT";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamEnrollmentRepository examEnrollmentRepository;
    private final ResultRepository resultRepository;
    private final ScoreReviewHistoryRepository scoreReviewHistoryRepository;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          StudentAnswerRepository studentAnswerRepository,
                          ExamEnrollmentRepository examEnrollmentRepository,
                          ResultRepository resultRepository,
                          ScoreReviewHistoryRepository scoreReviewHistoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentAnswerRepository = studentAnswerRepository;
        this.examEnrollmentRepository = examEnrollmentRepository;
        this.resultRepository = resultRepository;
        this.scoreReviewHistoryRepository = scoreReviewHistoryRepository;
    }

    //  1. GET PROFILE
    @GetMapping("/profile")
    public User getProfile(Authentication authentication){
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //  2. UPDATE PROFILE
    @PutMapping("/update-profile")
    public User updateProfile(@RequestBody UpdateProfileRequest request,
                              Authentication authentication){

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());

        return userRepository.save(user);
    }

    //  3. CHANGE PASSWORD
    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request,
                                 Authentication authentication){

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password updated successfully";
    }

    // 4. GET ALL USERS (ADMIN)
    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // 4.1 GET ALL STUDENT PROFILES (TEACHER)
    @GetMapping("/students")
    public List<User> getAllStudentProfiles() {
        return userRepository.findByRole_RoleNameIgnoreCase(ROLE_STUDENT);
    }
    // 5. GET USER BY ID (ADMIN, or TEACHER for STUDENT users only)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()));
        if (isAdmin) {
            return user;
        }

        boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(a -> ROLE_TEACHER.equals(a.getAuthority()));
        if (!isTeacher) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        String targetRole = user.getRole() == null || user.getRole().getRoleName() == null
                ? ""
                : user.getRole().getRoleName().trim().toUpperCase(Locale.ROOT);

        if (!ROLE_STUDENT.equals(targetRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Teachers can access only STUDENT users");
        }

        return user;
    }

    // Backward-compatible route.
    @GetMapping("/by-id/{id}")
    public User getUserByIdLegacy(@PathVariable Long id, Authentication authentication) {
        return getUserById(id, authentication);
    }

    //  6. UPDATE USER (ADMIN or SELF)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody AdminUpdateUserRequest request,
                           Authentication authentication) {

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()));

        boolean isSelf = currentUser.getId() != null && currentUser.getId().equals(targetUser.getId());
        if (!isAdmin && !isSelf) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can update only your own profile");
        }

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            targetUser.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            targetUser.setEmail(request.getEmail());
        }

        if (request.getRole() != null && !request.getRole().isBlank()) {
            if (!isAdmin) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can change roles");
            }
            Role role = roleRepository.findByRoleName(request.getRole())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role"));
            targetUser.setRole(role);
        }

        return userRepository.save(targetUser);
    }

    //  7. DELETE USER (ADMIN)
    @Transactional
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){

        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Clean up all related data before deleting the user
        studentAnswerRepository.deleteByUserId(id);
        // Delete score_review_history for each result before deleting results
        List<Result> userResults = resultRepository.findByUserId(id);
        for (Result r : userResults) {
            scoreReviewHistoryRepository.deleteByResultId(r.getId());
        }
        resultRepository.deleteByUserId(id);
        examEnrollmentRepository.deleteByUserId(id);

        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
