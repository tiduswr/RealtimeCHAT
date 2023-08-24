package com.tiduswr.rcmessage.exceptions;

public class StompConnectionRefused extends RuntimeException {
    public StompConnectionRefused(String message) {
        super(message);
    }
}
