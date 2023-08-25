package com.tiduswr.rcauth.models.dto;

import com.tiduswr.rcauth.models.EmailTemplateType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailDTO (
    @Min(0)
    Long id,

    @NotNull 
    @NotBlank
    String formalName,

    @Min(0)
    Long ownerId,

    @NotNull 
    @NotBlank
    String emailFrom,

    @NotNull 
    @NotBlank
    String emailTo,

    @NotNull 
    @NotBlank
    String emailSubject,

    @NotNull 
    @NotBlank
    EmailTemplateType emailTemplateType,
    
    String emailText,
    String browser_name,
    String operating_system,
    String action_url
){}
