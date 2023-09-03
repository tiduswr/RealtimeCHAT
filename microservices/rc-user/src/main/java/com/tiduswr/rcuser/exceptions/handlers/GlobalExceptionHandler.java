package com.tiduswr.rcuser.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tiduswr.rcuser.exceptions.EmailAlreadyExists;
import com.tiduswr.rcuser.exceptions.ImageNotSupportedException;
import com.tiduswr.rcuser.exceptions.UserNotFoundException;
import com.tiduswr.rcuser.exceptions.UsernameAlreadyExists;
import com.tiduswr.rcuser.model.ErrorResponse;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (errors.containsKey(error.getField())) {
                errors.put(error.getField(), String.format("%s, %s", errors.get(error.getField()), error.getDefaultMessage()));
            } else {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        });

        ErrorResponse errorResponse = new ErrorResponse(errors, "VALIDATION_FAILED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExists ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("userName", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "VALIDATION_FAILED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExists ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "VALIDATION_FAILED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("user", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "USER_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ImageNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleImageNotSupportedException(ImageNotSupportedException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "UNSUPPORTED_MEDIA_TYPE");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleImageNotSupportedException(IOException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

}
