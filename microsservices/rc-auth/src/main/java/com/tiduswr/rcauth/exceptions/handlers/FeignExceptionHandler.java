package com.tiduswr.rcauth.exceptions.handlers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiduswr.rcauth.exceptions.GenericFeignException;
import com.tiduswr.rcauth.models.ErrorResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class FeignExceptionHandler{

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        ErrorResponse errorResponse = null;
        System.out.println(ex);
        if (ex.responseBody().isPresent() && ex.responseBody().get() != null) {
            try {
                String content = ex.contentUTF8();

                if(content.contains("timestamp")){
                    log.info("Is a GenericFeignException");
                    var generic = new ObjectMapper().readValue(content, GenericFeignException.class);
                    errorResponse = new ErrorResponse("error", parseFeignErrorMessage(generic.error()));
                }else{
                    log.info("Is probably a ErrorResponseEntity");
                    errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                }

                return ResponseEntity.status(ex.status()).body(errorResponse);
            } catch (IOException e) {
                log.error("It is an Unexpected Exception");
            }
        }
        errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.name(), "Serviço indisponível!");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    private String parseFeignErrorMessage(String message){
        try{
            if(!message.contains(":")) return message;
            String[] splited = message.split(":");
            String result = splited[2];
            return result.replace(" [", "").replace("]", "");
        }catch(Exception ex){
            return message;
        }
    }
}