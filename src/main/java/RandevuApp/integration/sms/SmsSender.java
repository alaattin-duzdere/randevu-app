package RandevuApp.integration.sms;

public interface SmsSender {
    /**
     * Send SMS message
     *
     * @param toPhoneNumber The recipient's phone number
     * @param messageBody   The message content
     * @return A unique message identifier (SID or ID)
     *
     */
    String sendSms(String toPhoneNumber, String messageBody);
}
