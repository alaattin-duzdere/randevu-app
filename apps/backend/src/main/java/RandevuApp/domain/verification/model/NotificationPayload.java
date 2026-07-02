package RandevuApp.domain.verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class NotificationPayload{
        private String subject;
        private String message;
        private Map<String, Object> variables;
}
