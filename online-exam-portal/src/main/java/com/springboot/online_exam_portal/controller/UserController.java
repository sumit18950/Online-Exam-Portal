package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.*;
import com.springboot.online_exam_portal.entity.Role;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.RoleRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 1. GET PROFILE
    @GetMapping("/profile")
    public User getProfile(Authentication authentication){
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ 2. UPDATE PROFILE
    @PutMapping("/update-profile")
    public User updateProfile(@RequestBody UpdateProfileRequest request,
                              Authentication authentication){

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());

        return userRepository.save(user);
    }

    // ✅ 3. CHANGE PASSWORD
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

    // ✅ 4. GET ALL USERS (ADMIN)
    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // ✅ 5. GET USER BY ID (ADMIN, or TEACHER for STUDENT users only)
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

    // ✅ 6. UPDATE USER (ADMIN)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody AdminUpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null && !request.getRole().isBlank()) {
            Role role = roleRepository.findByRoleName(request.getRole())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role"));
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    // ✅ 7. DELETE USER (ADMIN)
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){

        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}