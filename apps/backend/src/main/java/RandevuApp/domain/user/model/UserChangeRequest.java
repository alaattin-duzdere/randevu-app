package RandevuApp.domain.user.model;

import RandevuApp.commons.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_change_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangeRequest extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private RequestType type;

    private String newValue;

    //private String verificationCode;

    private Instant expiresAt;

    public enum RequestType {
        EMAIL_UPDATE, PHONE_UPDATE
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}