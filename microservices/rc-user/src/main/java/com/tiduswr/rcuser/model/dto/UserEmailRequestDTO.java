package com.tiduswr.rcuser.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserEmailRequestDTO(
        @NotNull(message = "{email.notnull}")
        @Email(message = "{email.valid}")
        String email
) {
}
