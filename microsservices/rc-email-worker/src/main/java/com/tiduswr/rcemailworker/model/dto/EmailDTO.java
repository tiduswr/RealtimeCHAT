package com.tiduswr.rcemailworker.model.dto;

public record EmailDTO (
    Long id,
    String from,
    String to,
    String subject,
    String text
){}
