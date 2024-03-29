package com.tiduswr.rcauth.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiduswr.rcauth.exceptions.GenericFeignException;
import com.tiduswr.rcauth.exceptions.UnauthorizedException;
import com.tiduswr.rcauth.exceptions.WeakSecretJWT;
import com.tiduswr.rcauth.models.ErrorResponse;

import feign.FeignException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
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

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(WeakSecretJWT.class)
    public ResponseEntity<ErrorResponse> handleImageNotSupportedException(WeakSecretJWT ex) {
        ErrorResponse errorResponse = new ErrorResponse(null, ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
    

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException unauthorizedException){
        ErrorResponse errorResponse = new ErrorResponse(null, unauthorizedException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
        ErrorResponse errorResponse = new ErrorResponse(null, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}
