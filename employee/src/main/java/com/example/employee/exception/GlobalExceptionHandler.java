package com.example.employee.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleNotFound(
            ResourceNotFoundException ex) {

        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        404,
                        ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>
    handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String,String> errors =
                new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
                  errors.put(
                          error.getField(),
                          error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}