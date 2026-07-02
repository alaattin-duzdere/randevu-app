package RandevuApp.domain.auth.model;

import RandevuApp.commons.model.BaseEntity;
import RandevuApp.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class RefreshToken extends BaseEntity {

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expired_date")
    private Instant expiredDate;

    @ManyToOne
    private User user;
}
