package com.tiduswr.rcimageworker.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @Table(name = "profile_image")
@Getter @Setter @NoArgsConstructor
@Builder @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfileImage {

    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 30, nullable = false)
    private String username;

}
