package RandevuApp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.auth.password")
public class AuthPasswordProperties {
    private int resetTokenValidityMinutes = 15;
    private int codeLength = 6;
}
