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
}
