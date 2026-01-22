package RandevuApp.domain.notification.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
public class VerificationNotificationRequest {
    private final NotificationRequest notificationRequest;

    @Getter
    private final NotificationChannel channel;

    private VerificationNotificationRequest(Builder builder) {
        this.channel = builder.channel;

        this.notificationRequest = NotificationRequest.builder()
                .recipient(builder.recipient)
                .userId(builder.userId)
                .subject(builder.subject)
                .message(builder.message)
                .category(builder.category) // Dinamik kategori
                .variables(builder.variables)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    // --- Wrapper Builder ---
    public static class Builder {
        private String recipient;
        private Long userId;
        private String subject;
        private String message;
        private NotificationCategory category; // Yeni alan
        private Map<String, Object> variables = new HashMap<>();

        private NotificationChannel channel;

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

        public Builder channel(NotificationChannel channel) {
            this.channel = channel;
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

        public VerificationNotificationRequest build() {
            return new VerificationNotificationRequest(this);
        }
    }

    public String getRecipient() {
        return notificationRequest.getRecipient();
    }

    public Long getUserId() {
        return notificationRequest.getUserId();
    }

    public String getSubject() {
        return notificationRequest.getSubject();
    }

    public String getMessage() {
        return notificationRequest.getMessage();
    }

    public NotificationCategory getCategory() {
        return notificationRequest.getCategory();
    }

    public Map<String, Object> getVariables() {
        return notificationRequest.getVariables();
    }

    public NotificationRequest getNotificationRequest(){
        return  notificationRequest;
    }

}
