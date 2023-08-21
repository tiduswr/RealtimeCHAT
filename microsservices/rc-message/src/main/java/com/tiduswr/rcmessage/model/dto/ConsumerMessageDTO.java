package com.tiduswr.rcmessage.model.dto;

import java.time.LocalDateTime;

import com.tiduswr.rcmessage.model.ConsumerMessageType;
import com.tiduswr.rcmessage.model.Status;
import com.tiduswr.rcmessage.model.User;

public record ConsumerMessageDTO(
    Long id,
    User sender,
    User receiver,
    String message,
    Boolean read,
    Status status,
    ConsumerMessageType type,
    LocalDateTime createdAt
){}
