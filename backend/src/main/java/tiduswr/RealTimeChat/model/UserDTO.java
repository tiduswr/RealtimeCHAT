package tiduswr.RealTimeChat.model;

import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tiduswr.RealTimeChat.model.validation.anotation.ValidPassword;


public record UserDTO(
        @JsonProperty(access = Access.READ_ONLY)
        Long id,

        @NotNull(message = "{user_name.notnull}")
        @Size(min = 3, max = 30, message = "{username.size}")
        String userName,

        @NotNull(message = "{password.notnull}")
        @ValidPassword
        @JsonProperty(access = Access.WRITE_ONLY)
        String password,

        @NotNull(message = "{formal_name.notnull}")
        @Size(min = 3, max = 100, message = "{formal_name.size}")
        String formalName
) {}
