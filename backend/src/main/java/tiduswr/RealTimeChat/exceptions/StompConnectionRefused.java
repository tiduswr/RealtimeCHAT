package tiduswr.RealTimeChat.exceptions;

import org.springframework.messaging.MessageDeliveryException;

public class StompConnectionRefused extends MessageDeliveryException {
    public StompConnectionRefused(String message) {
        super(message);
    }
}
