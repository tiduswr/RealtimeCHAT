package com.tiduswr.rcuser.model;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
