package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.RegisterRequest;
import com.springboot.online_exam_portal.entity.Role;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.RoleRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Role studentRole;

    @BeforeEach
    void setUp() {
        studentRole = new Role();
        studentRole.setId(1L);
        studentRole.setRoleName("STUDENT");
    }

    @Test
    void registerUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@test.com");
        request.setPassword("pass123");

        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(roleRepository.findByRoleName("STUDENT")).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("pass123")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User result = userService.register(request);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("john@test.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_DuplicateEmail_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@test.com");
        request.setUsername("user");
        request.setPassword("pass");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setPasswordHash("encoded_pass");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encoded_pass")).thenReturn(true);

        User result = userService.login("john@test.com", "pass123");

        assertNotNull(result);
        assertEquals("john@test.com", result.getEmail());
    }

    @Test
    void loginUser_WrongPassword_ThrowsException() {
        User user = new User();
        user.setEmail("john@test.com");
        user.setPasswordHash("encoded_pass");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "encoded_pass")).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> userService.login("john@test.com", "wrongpass"));
    }

    @Test
    void loginUser_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> userService.login("unknown@test.com", "pass"));
    }

    @Test
    void registerUser_PasswordIsEncoded() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jane");
        request.setEmail("jane@test.com");
        request.setPassword("mypassword");

        when(userRepository.existsByEmail("jane@test.com")).thenReturn(false);
        when(roleRepository.findByRoleName("STUDENT")).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("mypassword")).thenReturn("hashed_value");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.register(request);

        assertEquals("hashed_value", result.getPasswordHash());
        verify(passwordEncoder).encode("mypassword");
    }
}
