package com.example.java_final_assignment.exceptions;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //For global exception, this will be my standard response
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<AppResponse<Void>> handleGlobalException(
            GlobalException ex) {

        return ResponseEntity
                .status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .body(new AppResponse<>(
                        ex.getStatusCode(),
                        ex.getMessage()
                ));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AppResponse<Void>> handleInvalidRequest(HttpMessageNotReadableException ex) {

        return ResponseEntity
                .badRequest()
                .body(new AppResponse<>(
                        400,
                        "Bad Request (check Enums or JSON format)"
                ));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity
                .badRequest()
                .body(new AppResponse<>(
                        400,
                        message
                ));
    }
}