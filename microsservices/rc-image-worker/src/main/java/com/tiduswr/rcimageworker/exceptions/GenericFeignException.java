package com.tiduswr.rcimageworker.exceptions;

public record GenericFeignException (
    String timestamp, String status, String error, String path
){}
