package RandevuApp.domain.verification.validator.filter;

import RandevuApp.api.ErrorCode;
import RandevuApp.config.VerificationProperties;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.exceptions.verification.VerificationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Order(3)
@RequiredArgsConstructor
public class SpamProtectionFilter implements IVerificationFilter {

    private final StringRedisTemplate redisTemplate;
    private final VerificationProperties verificationProperties;

    @Override
    public void validate(VerificationRequest request, User user) {
        String spamActorIdentifier = (user != null)
                ? user.getId().toString()
                : request.getReferenceId();

        String key = "verification:spam:" + spamActorIdentifier + ":" + request.getPurpose();

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            // Set expiration for the first request
            redisTemplate.expire(key, Duration.ofMinutes(verificationProperties.getSpam().getBlockDurationMinutes()));
        }

        if (count != null && count > verificationProperties.getSpam().getMaxRequestsPerHour()) {
            throw new VerificationFailedException("Too many attempts. Please try again later.");
        }
    }
}
