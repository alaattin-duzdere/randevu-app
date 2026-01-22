package RandevuApp.domain.notification.service;

import RandevuApp.domain.notification.model.NotificationRequest;
import RandevuApp.domain.notification.model.VerificationNotificationRequest;

public interface INotificationService {
    void send(NotificationRequest request);
    void sendVerificationNotification(VerificationNotificationRequest request);
}
