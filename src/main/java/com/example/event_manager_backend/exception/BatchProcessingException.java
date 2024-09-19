package com.example.event_manager_backend.exception;

public class BatchProcessingException extends RuntimeException {
    public BatchProcessingException(String message) {
        super(message);
    }

    public BatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
