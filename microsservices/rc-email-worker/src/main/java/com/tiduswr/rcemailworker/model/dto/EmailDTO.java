package com.tiduswr.rcemailworker.model.dto;

import com.tiduswr.rcemailworker.model.EmailTemplateType;

public record EmailDTO (
    Long id,
    String formalName,
    Long ownerId,
    String emailFrom,
    String emailTo,
    String emailSubject,
    String emailText,
    EmailTemplateType emailTemplateType,
    String browser_name,
    String operating_system,
    String action_url
){}
