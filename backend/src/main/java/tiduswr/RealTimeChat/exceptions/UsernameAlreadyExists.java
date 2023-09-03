package tiduswr.RealTimeChat.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String s) {
        super(s);
    }
}
