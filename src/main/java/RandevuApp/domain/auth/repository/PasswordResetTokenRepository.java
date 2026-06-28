package RandevuApp.domain.auth.repository;

import RandevuApp.domain.auth.model.PasswordResetToken;
import RandevuApp.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUserId(Long userId);
}
