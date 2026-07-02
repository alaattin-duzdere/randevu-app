package RandevuApp.domain.notification.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Data
public class NotificationRequest {
    private String recipient; // User ID or Email/Phone
    private Long userId;
    private String subject;
    private String message; // Used for SMS or as fallback
    private NotificationCategory category; // Used for template selection
    private Set<NotificationChannel> explicitChannels; // Bypass user preferences if provided
    private Map<String, Object> variables; // Dynamic variables for Thymeleaf templates

    // sadece builder erişecek
    private NotificationRequest(Builder builder) {
        this.recipient = builder.recipient;
        this.userId = builder.userId;
        this.subject = builder.subject;
        this.message = builder.message;
        this.category = builder.category;
        this.explicitChannels = builder.explicitChannels;
        this.variables = builder.variables;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String recipient;
        private Long userId;
        private String subject;
        private String message;
        private NotificationCategory category;
        private Set<NotificationChannel> explicitChannels = new HashSet<>();
        private Map<String, Object> variables = new HashMap<>();

        public Builder recipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder category(NotificationCategory category) {
            this.category = category;
            return this;
        }
        
        public Builder explicitChannels(Set<NotificationChannel> explicitChannels) {
            this.explicitChannels = explicitChannels;
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        public Builder variable(String key, Object value) {
            this.variables.put(key, value);
            return this;
        }

        public NotificationRequest build() {
            return new NotificationRequest(this);
        }
    }
}
