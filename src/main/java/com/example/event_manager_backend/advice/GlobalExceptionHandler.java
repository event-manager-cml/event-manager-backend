package com.example.event_manager_backend.advice;

import com.example.event_manager_backend.exception.EventNotFoundException;
import com.example.event_manager_backend.exception.BatchProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Event not found custom exceptions
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFoundException(EventNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // Handle Batch Processing custom exceptions
    @ExceptionHandler(BatchProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleBatchProcessingException(BatchProcessingException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle other exceptions globally
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        errors.put("message", "An unexpected error occurred");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle MethodArgumentNotValidException for @Valid annotation violations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST);
        errors.put("message", "Validation failed");

        List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        errors.put("errors", validationErrors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle illegal argument exception
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // A generic API error response body
    @Setter
    @Getter
    public static class ApiError {
        private HttpStatus status;
        private String message;
        private List<String> errors;

        public ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public ApiError(HttpStatus status, String message, List<String> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

    }
}
