package RandevuApp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {
    private long phoneVerificationValidityDays;
    private long emailVerificationValidityDays;
}