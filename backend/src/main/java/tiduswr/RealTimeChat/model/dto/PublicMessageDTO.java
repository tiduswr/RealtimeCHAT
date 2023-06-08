package tiduswr.RealTimeChat.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tiduswr.RealTimeChat.model.Message;
import tiduswr.RealTimeChat.model.Status;

import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor
public class PublicMessageDTO{

        @NotNull(message = "{message.NotNull}")
        @NotBlank(message = "{message.NotBlank}")
        private String message;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String sender;

        private Boolean read;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Status status;

        public static PublicMessageDTO from(Message message){
                return PublicMessageDTO.builder()
                        .message(message.getMessage())
                        .read(message.getRead())
                        .sender(message.getSender().getUserName())
                        .status(message.getStatus())
                        .createdAt(message.getCreatedAt())
                        .build();
        }

}
