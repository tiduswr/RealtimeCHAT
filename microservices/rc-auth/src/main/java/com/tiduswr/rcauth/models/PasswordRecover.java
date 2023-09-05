package com.tiduswr.rcauth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "TB_RECOVER_PASSWORD_CODE")
public class PasswordRecover {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 36)
    private String code;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    public PasswordRecover(String username){
        this.username = username;
    }

}
