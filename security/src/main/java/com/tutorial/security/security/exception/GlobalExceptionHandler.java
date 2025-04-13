package com.tutorial.security.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException validEx) {
        Map<String, String> validationErrors = new HashMap<>();
        validEx.getBindingResult().getAllErrors()
                .forEach(
                        err -> {
                            String field = ((FieldError) err).getField();
                            String msg = err.getDefaultMessage();
                            validationErrors.put(field, msg);
                        }
                );
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

}
