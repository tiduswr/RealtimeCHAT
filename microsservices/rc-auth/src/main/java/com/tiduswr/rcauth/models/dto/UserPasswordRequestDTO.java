package com.tiduswr.rcauth.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiduswr.rcauth.models.validation.anotation.ValidPassword;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserPasswordRequestDTO{
        @NotNull(message = "{password.notnull}")
        @ValidPassword
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password;
}
