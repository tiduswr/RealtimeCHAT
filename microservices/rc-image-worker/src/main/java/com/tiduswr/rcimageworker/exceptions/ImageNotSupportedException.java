package com.tiduswr.rcimageworker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class ImageNotSupportedException extends RuntimeException {
    public ImageNotSupportedException(String s) {
        super(s);
    }
}
