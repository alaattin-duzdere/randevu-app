package RandevuApp.domain.user.model;

import RandevuApp.commons.model.BaseEntity;
import RandevuApp.domain.notification.model.UserNotificationPreference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = true)
    @Email
    @Nullable
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Nullable
    @Builder.Default
    private Gender gender = Gender.NOT_SPECIFIED;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @NotEmpty
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserNotificationPreference> notificationPreferences = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private Instant phoneVerifiedAt;

    private Instant emailVerifiedAt;


    // Helper methods for verification status.
    public VerificationStatus getPhoneVerificationStatus(long validityDurationInDays) {
        if (this.phoneVerifiedAt == null) {
            return VerificationStatus.NOT_VERIFIED;
        }

        Instant expirationTime = this.phoneVerifiedAt.plus(validityDurationInDays, ChronoUnit.DAYS);

        if (expirationTime.isBefore(Instant.now())) {
            return VerificationStatus.EXPIRED;
        }

        return VerificationStatus.VERIFIED;
    }

    public VerificationStatus getEmailVerificationStatus(long validityDurationInDays) {
        if (this.emailVerifiedAt == null) {
            return VerificationStatus.NOT_VERIFIED;
        }

        Instant expirationTime = this.emailVerifiedAt.plus(validityDurationInDays, ChronoUnit.DAYS);

        if (expirationTime.isBefore(Instant.now())) {
            return VerificationStatus.EXPIRED;
        }

        return VerificationStatus.VERIFIED;
    }
}
