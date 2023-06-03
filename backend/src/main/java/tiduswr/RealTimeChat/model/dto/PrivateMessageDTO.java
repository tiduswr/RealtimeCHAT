package tiduswr.RealTimeChat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tiduswr.RealTimeChat.model.Message;

import java.time.LocalDateTime;

@Data @Builder
public class PrivateMessageDTO{

        @NotNull(message = "{receiver.NotNull}")
        private String receiver;

        @NotNull(message = "{message.NotNull}")
        @NotBlank(message = "{message.NotBlank}")
        private String message;

        @NotNull(message = "{read.NotNull}")
        private Boolean read;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private LocalDateTime createdAt;

        public static PrivateMessageDTO from(Message message){
                return PrivateMessageDTO.builder()
                        .message(message.getMessage())
                        .receiver(message.getReceiver().getUserName())
                        .read(message.getRead())
                        .createdAt(message.getCreatedAt())
                        .build();
        }

}
