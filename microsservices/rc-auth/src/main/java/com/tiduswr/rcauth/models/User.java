package com.tiduswr.rcauth.models;

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

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false, length = 70)
    private String password;

    @Column(nullable = false, length = 100)
    private String formalName;

}
