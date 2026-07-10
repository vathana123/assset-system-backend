package com.bnkc.assetsystembackend.exception;

import com.bnkc.assetsystembackend.data.respone.ResourceNotFoundResponse;
import com.bnkc.assetsystembackend.data.respone.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResourceNotFoundResponse> handleApiException(ApiException e) {
        ResourceNotFoundResponse response = ResourceNotFoundResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );

        ValidationResponse response = ValidationResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .messages(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResourceNotFoundResponse> handleAuthenticationException(AuthenticationException e) {
        ResourceNotFoundResponse response = ResourceNotFoundResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("Authentication failed.")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResourceNotFoundResponse> handleAccessDeniedException(AccessDeniedException e) {
        ResourceNotFoundResponse response = ResourceNotFoundResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("Access is denied.")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
