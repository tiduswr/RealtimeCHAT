package com.tiduswr.rcmessage.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiduswr.rcmessage.exceptions.GenericFeignException;
import com.tiduswr.rcmessage.exceptions.UserNotFoundException;
import com.tiduswr.rcmessage.model.ErrorResponse;

import feign.FeignException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        if (ex.responseBody().isPresent() && ex.responseBody().get() != null) {
            try {
                String content = ex.contentUTF8();
                ErrorResponse errorResponse = null;

                if(content.contains("timestamp")){
                    var generic = new ObjectMapper().readValue(content, GenericFeignException.class);
                    errorResponse = new ErrorResponse("error", generic.error());
                }else{
                    errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                }

                return ResponseEntity.status(ex.status()).body(errorResponse);
            } catch (IOException e) {
                logger.error("Function handleFeignException", e);
            }
        }
        ErrorResponse errorResponse = new ErrorResponse("error", ex.getMessage());
        return ResponseEntity.status(ex.status()).body(errorResponse);
    }

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("user", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "USER_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
