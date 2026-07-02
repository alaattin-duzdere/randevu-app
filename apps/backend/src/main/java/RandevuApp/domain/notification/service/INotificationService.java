package RandevuApp.domain.notification.service;

import RandevuApp.domain.notification.model.NotificationRequest;

public interface INotificationService {
    void send(NotificationRequest request);
}
