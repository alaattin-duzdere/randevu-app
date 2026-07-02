package RandevuApp.domain.notification.service.strategy;

import RandevuApp.commons.util.ContactFormatUtil;
import RandevuApp.commons.util.DataNormalizationUtil;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;
import RandevuApp.integration.sms.SmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsNotificationStrategy implements NotificationStrategy {

    private final SmsSender smsSender;

    @Override
    public void send(NotificationRequest request) {
        String phoneNumber = request.getRecipient();
        String message = request.getMessage();

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            log.error("SMS sending failed: Phone number is missing for User ID: {}", request.getUserId());
            return;
        }
        if (message == null || message.trim().isEmpty()) {
            log.error("SMS sending failed: Message content is empty for User ID: {}", request.getUserId());
            return;
        }

        phoneNumber = DataNormalizationUtil.normalizePhone(phoneNumber);
        if (!ContactFormatUtil.isPhone(phoneNumber)) {
            log.warn("SMS sending cancelled: Invalid phone number format '{}' for User ID: {}", phoneNumber, request.getUserId());
            return;
        }

        log.info("Sending SMS to User ID: {} (Address: {})", request.getUserId(), phoneNumber);

        try {
            String messageSid = smsSender.sendSms(phoneNumber, message);

            log.info("SMS sent successfully to {}. Message SID: {}", phoneNumber, messageSid);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}. Reason: {}", phoneNumber, e.getMessage(), e);
        }
    }

    @Override
    public NotificationChannel supports() {
        return NotificationChannel.SMS;
    }
}
