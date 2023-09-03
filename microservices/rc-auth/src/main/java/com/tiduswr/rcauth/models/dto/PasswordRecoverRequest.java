package com.tiduswr.rcauth.models.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PasswordRecoverRequest{
    @NotNull(message = "{email.notnull}")
    @Email(message = "{email.valid}")
    private String email;

    @NotNull(message = "{password.recover.url.notnull}")
    @URL(message = "{password.recover.url.valid}")
    private String redirectPrefix;

    @NotNull(message = "{password.recover.browser.notnull}")
    private String browserName;

    @NotNull(message = "{password.recover.device.notnull}")
    private String operatingSystem;
}
