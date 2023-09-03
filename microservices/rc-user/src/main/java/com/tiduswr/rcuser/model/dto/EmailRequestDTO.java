package com.tiduswr.rcuser.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequestDTO {
    @NotNull(message = "{email.notnull}")
    @Email(message = "{email.valid}")
    String email;
}
