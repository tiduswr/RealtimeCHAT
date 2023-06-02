package tiduswr.RealTimeChat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PublicMessageDTO(

        @NotNull(message = "{message.NotNull}")
        @NotBlank(message = "{message.NotBlank}")
        String message,

        @NotNull(message = "{read.NotNull}")
        Boolean read,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime createdAt

){}
