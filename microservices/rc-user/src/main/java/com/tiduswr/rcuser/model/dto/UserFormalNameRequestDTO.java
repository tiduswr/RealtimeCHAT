package com.tiduswr.rcuser.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserFormalNameRequestDTO(
        @NotNull(message = "{formal_name.notnull}")
        @Size(min = 3, max = 100, message = "{formal_name.size}")
        String formalName
) {
}
