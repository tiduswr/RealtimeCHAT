package com.tiduswr.rcauth.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name = "refresh_tokens")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class RefreshToken implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String token;
    
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
