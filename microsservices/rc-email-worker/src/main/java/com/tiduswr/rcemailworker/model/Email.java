package com.tiduswr.rcemailworker.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;    
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter 
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity @Table(name = "TB_EMAIL")
public class Email implements Serializable{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String emailFrom;

    @Column(nullable = false)
    private String emailTo;

    @Column(nullable = false)
    private String emailSubject;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String emailText;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime emailSendAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus emailStatus;

}
