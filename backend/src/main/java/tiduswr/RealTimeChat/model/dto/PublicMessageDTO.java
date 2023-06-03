package tiduswr.RealTimeChat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tiduswr.RealTimeChat.model.Message;

import java.time.LocalDateTime;

@Data @Builder
public class PublicMessageDTO{
        @NotNull(message = "{message.NotNull}")
        @NotBlank(message = "{message.NotBlank}")
        private String message;

        private Boolean read;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private LocalDateTime createdAt;

        public static PublicMessageDTO from(Message message){
                return PublicMessageDTO.builder()
                        .message(message.getMessage())
                        .read(message.getRead())
                        .createdAt(message.getCreatedAt())
                        .build();
        }

}
