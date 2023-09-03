package tiduswr.RealTimeChat.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String s) {
        super(s);
    }
}
