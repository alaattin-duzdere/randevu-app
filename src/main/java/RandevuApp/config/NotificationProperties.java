package RandevuApp.config;


import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.notification")
public class NotificationProperties {

    /**
     * Maps notification categories to their default channels.
     * Example in properties:
     * app.notification.defaults.LOAN_OVERDUE=EMAIL,SMS
     */
    private Map<NotificationCategory, Set<NotificationChannel>> defaults = new EnumMap<>(NotificationCategory.class);
}
