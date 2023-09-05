package com.tiduswr.rcmessage.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity @Table(name = "messages")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString
@Builder @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message implements Serializable {

    @EqualsAndHashCode.Include
    @Id() @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    @Column
    private String receiver;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "readed")
    private Boolean read;

    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
