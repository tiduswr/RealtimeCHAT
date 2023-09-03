package com.tiduswr.rcuser.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor
@Builder @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String userName;

    @Column(nullable = false, length = 70)
    private String password;

    @Column(nullable = false, length = 100)
    private String formalName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

}
