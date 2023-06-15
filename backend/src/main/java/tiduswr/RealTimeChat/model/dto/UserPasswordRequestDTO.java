package tiduswr.RealTimeChat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tiduswr.RealTimeChat.model.validation.anotation.ValidPassword;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserPasswordRequestDTO{
        @NotNull(message = "{password.notnull}")
        @ValidPassword
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password;
}
