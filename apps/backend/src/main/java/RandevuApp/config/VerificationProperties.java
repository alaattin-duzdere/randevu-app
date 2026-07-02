package RandevuApp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.verification")
public class VerificationProperties {
    private int tokenValidityMinutes = 15;
    private int maxAttempts = 3;
    private int codeLength = 6;
    private int passwordResetTokenValidityMinutes = 15;
    
    private Spam spam = new Spam();

    @Getter
    @Setter
    public static class Spam {
        private int maxRequestsPerHour = 5;
        private int blockDurationMinutes = 60;
    }
}
