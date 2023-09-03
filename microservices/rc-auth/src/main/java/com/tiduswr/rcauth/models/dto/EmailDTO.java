package com.tiduswr.rcauth.models.dto;

import java.io.Serializable;

import com.tiduswr.rcauth.models.EmailTemplateType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class EmailDTO implements Serializable{
    @Min(0)
    private Long id;

    @NotNull 
    @NotBlank
    private String formalName;

    @Min(0)
    private Long ownerId;

    @NotNull 
    @NotBlank
    private String emailFrom;

    @NotNull 
    @NotBlank
    private String emailTo;

    @NotNull 
    @NotBlank
    private String emailSubject;

    @NotNull 
    @NotBlank
    private EmailTemplateType emailTemplateType;
    
    private String emailText;
    private String browser_name;
    private String operating_system;
    private String action_url;
}
