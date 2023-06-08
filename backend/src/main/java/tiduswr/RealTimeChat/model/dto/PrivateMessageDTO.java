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
public class PrivateMessageDTO{

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String sender;

        @NotNull(message = "{receiver.NotNull}")
        private String receiver;

        @NotNull(message = "{message.NotNull}")
        @NotBlank(message = "{message.NotBlank}")
        private String message;

        @NotNull(message = "{read.NotNull}")
        private Boolean read;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Status status;

        public static PrivateMessageDTO from(Message message){
                return PrivateMessageDTO.builder()
                        .message(message.getMessage())
                        .receiver(message.getReceiver().getUserName())
                        .sender(message.getSender().getUserName())
                        .read(message.getRead())
                        .status(message.getStatus())
                        .createdAt(message.getCreatedAt())
                        .build();
        }

}
