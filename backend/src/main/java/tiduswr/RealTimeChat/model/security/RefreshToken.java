package tiduswr.RealTimeChat.model.security;

import jakarta.persistence.*;
import lombok.*;
import tiduswr.RealTimeChat.model.User;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
