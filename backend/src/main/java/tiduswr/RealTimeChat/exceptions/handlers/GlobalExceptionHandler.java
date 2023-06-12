package tiduswr.RealTimeChat.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tiduswr.RealTimeChat.exceptions.ImageNotSupportedException;
import tiduswr.RealTimeChat.exceptions.UsernameAlreadyExists;
import tiduswr.RealTimeChat.model.ErrorResponse;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExists ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("userName", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "VALIDATION_FAILED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(ImageNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleImageNotSupportedException(ImageNotSupportedException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "UNSUPPORTED_MEDIA_TYPE");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleImageNotSupportedException(IOException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errors, "UNSUPPORTED_MEDIA_TYPE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}
