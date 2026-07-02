package RandevuApp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.business")
public class BusinessProperties {
    
    private String defaultTimezone = "Europe/Istanbul";
    private Defaults defaults = new Defaults();

    @Getter
    @Setter
    public static class Defaults {
        private int slotDurationTime = 30;
        private LocalTime openingTime = LocalTime.parse("09:00");
        private LocalTime closingTime = LocalTime.parse("18:00");
    }
}
