package com.example.expensetracker.exception;

import com.example.expensetracker.dto.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ApiResponse<>(message));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Invalid request payload. Please verify all the fields provided and their formats";
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException || cause instanceof DateTimeParseException) {
            message = "Invalid date format Use YYYY-MM-DD with valid month / day values.";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Invalid data. One or more fields violate database constraints.";
//        Throwable cause = ex.getCause();

//        if (ex.getMostSpecificCause().getMessage().contains("numeric")) {
//            message = "Invalid amount, value exceeds allowed numeric precision.";
//        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(message));
    }
}
