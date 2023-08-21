package com.tiduswr.rcmessage.exceptions;

import org.springframework.messaging.MessageDeliveryException;

public class StompAccessorNotFound extends MessageDeliveryException {
    public StompAccessorNotFound(String message) {
        super(message);
    }
}
