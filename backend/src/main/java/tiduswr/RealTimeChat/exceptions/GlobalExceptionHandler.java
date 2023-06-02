package tiduswr.RealTimeChat.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tiduswr.RealTimeChat.model.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
                if (errors.containsKey(error.getField())) {
                    errors.put(error.getField(), String.format("%s, %s", errors.get(error.getField()), error.getDefaultMessage()));
                } else {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            }
        );

        return new ErrorResponse(errors, "VALIDATION_FAILED");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExists.class)
    public ErrorResponse handleUsernameAlreadyExists(UsernameAlreadyExists ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("userName", ex.getMessage());

        return new ErrorResponse(errors, "VALIDATION_FAILED");
    }

}