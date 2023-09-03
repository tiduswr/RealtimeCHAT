package com.tiduswr.rcauth.exceptions;

public record GenericFeignException (
    String timestamp, String status, String error, String path
){}
