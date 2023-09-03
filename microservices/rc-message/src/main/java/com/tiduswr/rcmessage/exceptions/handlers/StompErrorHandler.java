package com.tiduswr.rcmessage.exceptions.handlers;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiduswr.rcmessage.exceptions.StompAccessorNotFound;
import com.tiduswr.rcmessage.exceptions.StompConnectionRefused;
import com.tiduswr.rcmessage.model.ErrorResponse;

public class StompErrorHandler extends StompSubProtocolErrorHandler{

    public StompErrorHandler(){}

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex){
        Throwable exception = ex;
        if (exception instanceof MessageDeliveryException)
        {
            exception = exception.getCause();
        }

        if (exception instanceof StompConnectionRefused || exception instanceof StompAccessorNotFound)
        {
            return handleResponse(clientMessage, exception);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> handleResponse(Message<byte[]> clientMessage, Throwable ex){
        ErrorResponse errorResponse = new ErrorResponse("UNAUTHORIZED", "Token de Acesso ou Payload inválidos!");

        return prepareErrorMessage(clientMessage, errorResponse);
    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ErrorResponse errorResponse){
        String message = transformErrorResponseToJson(errorResponse);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage("UNAUTHORIZED");
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message != null ? message.getBytes() : new byte[]{}, accessor.getMessageHeaders());
    }

    private String transformErrorResponseToJson(ErrorResponse errorResponse){
        final String DEFAULT = "{\"error\":\"Erro Interno\",\"message\":Não foi possível fazer parse do erro}";;

        try{
            return new ObjectMapper().writeValueAsString(errorResponse);
        }catch(Exception ex){
            return DEFAULT;
        }
    }

}
