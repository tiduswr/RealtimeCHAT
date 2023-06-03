package tiduswr.RealTimeChat.exceptions.handlers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(ImageNotSupportedException.class)
    public ErrorResponse handleImageNotSupportedException(ImageNotSupportedException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("image", ex.getMessage());

        return new ErrorResponse(errors, "UNSUPPORTED_MEDIA_TYPE");

    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(IOException.class)
    public ErrorResponse handleImageNotSupportedException(IOException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("image", ex.getMessage());

        return new ErrorResponse(errors, "UNSUPPORTED_MEDIA_TYPE");

    }

    @ExceptionHandler({ExpiredJwtException.class, UnsupportedJwtException.class, MalformedJwtException.class,
            SignatureException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwtExceptions(Exception ex) {
        return new ErrorResponse("Erro ao processar token JWT", "UNAUTHORIZED");
    }

}