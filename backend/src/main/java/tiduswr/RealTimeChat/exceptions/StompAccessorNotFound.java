package tiduswr.RealTimeChat.exceptions;

import org.springframework.messaging.MessageDeliveryException;

public class StompAccessorNotFound extends MessageDeliveryException {
    public StompAccessorNotFound(String message) {
        super(message);
    }
}
