package tiduswr.RealTimeChat.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class WeakSecretJWT extends RuntimeException {
    public WeakSecretJWT(String s) {
        super(s);
    }
}
