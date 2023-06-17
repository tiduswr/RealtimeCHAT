package tiduswr.RealTimeChat.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import tiduswr.RealTimeChat.model.ErrorResponse;
import tiduswr.RealTimeChat.model.Status;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.services.MessageService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@SuppressWarnings("unused")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public PublicMessageDTO receivePublicMessage(@Payload @Valid PublicMessageDTO message,
                                                 Principal principal){
        if(principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return messageService.createPublicMessage(message, principal.getName());
    }

    @MessageMapping("/private-message")
    public PrivateMessageDTO receivePrivateMessage(@Payload @Valid PrivateMessageDTO message,
                                                   Principal principal){

        if(principal == null){
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private",
                    new ResponseStatusException(HttpStatus.UNAUTHORIZED));
            return message;
        }

        if(message.getStatus().equals(Status.READ)){
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private",  message);
            return message;
        }

        PrivateMessageDTO persistedMessage = messageService.createPrivateMessage(message, principal.getName());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", persistedMessage);
        simpMessagingTemplate.convertAndSendToUser(persistedMessage.getSender(), "/private", persistedMessage);
        return persistedMessage;
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        if(ex.getBindingResult() != null){
            ex.getBindingResult().getFieldErrors().forEach(error -> {
                if (errors.containsKey(error.getField())) {
                    errors.put(error.getField(), String.format("%s, %s", errors.get(error.getField()), error.getDefaultMessage()));
                } else {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            });
        }else{
            errors.put("message", "Mensagem inválida!");
        }

        return new ErrorResponse(errors, "VALIDATION_FAILED");
    }

}
