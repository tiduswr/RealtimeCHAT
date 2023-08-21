package com.tiduswr.rcmessage.exceptions;

public record GenericFeignException (
    String timestamp, String status, String error, String path
){}
