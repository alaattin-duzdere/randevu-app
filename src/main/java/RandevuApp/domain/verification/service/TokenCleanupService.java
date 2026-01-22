package RandevuApp.domain.verification.service;

import RandevuApp.domain.verification.repository.VerificationTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenCleanupService {

    private final VerificationTokenRepository repository;

    public TokenCleanupService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 3 * * *") // 3 AM everyday
    public void removeExpiredTokens() {
        LocalDateTime cutOffTime = LocalDateTime.now().minusDays(1);
        repository.deleteByExpiresAtBefore(cutOffTime);
    }
}
