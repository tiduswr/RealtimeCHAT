package com.tiduswr.rcmessage.exceptions.handlers;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

public class StompErrorHandler extends StompSubProtocolErrorHandler{

    public StompErrorHandler() {
    }

    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor, byte[] errorPayload, Throwable cause, StompHeaderAccessor clientHeaderAccessor) {
        errorHeaderAccessor.setMessage(null);

        String message;
        if(cause instanceof MessageDeliveryException ex){
            message = ex.getMessage();
        }else{
            message = cause.getMessage();
        }

        return MessageBuilder.createMessage(message.getBytes(), errorHeaderAccessor.getMessageHeaders());
    }
}
