package com.tiduswr.rcuser.exceptions;

public record GenericFeignException (
    String timestamp, String status, String error, String path
){}
