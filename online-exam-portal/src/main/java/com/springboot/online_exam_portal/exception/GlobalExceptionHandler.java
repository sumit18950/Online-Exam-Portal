package com.springboot.online_exam_portal.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildError(status, ex.getReason());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Fallback for DB-level unique constraints (e.g., duplicated email).
        return buildError(HttpStatus.CONFLICT, "Email already exists");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        String finalMessage = (message == null || message.isBlank()) ? "Request failed" : message;
        Map<String, Object> body = Map.of(
//                "timestamp", LocalDateTime.now().toString(),
//                "status", status.value(),
//                "error", status.getReasonPhrase(),
                "message", finalMessage
        );
        return ResponseEntity.status(status).body(body);
    }
}
