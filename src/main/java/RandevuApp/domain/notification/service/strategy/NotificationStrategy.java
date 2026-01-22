package RandevuApp.domain.notification.service.strategy;

import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;

public interface NotificationStrategy {
    void send(NotificationRequest request);
    NotificationChannel supports();
}
