package RandevuApp.integration.sms;

import RandevuApp.commons.util.DataNormalizationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "application.sms.provider", havingValue = "mock")
public class MockSmsSender implements SmsSender {

    @Override
    public String sendSms(String toPhoneNumber, String messageBody) {
        String safePhoneNumber = DataNormalizationUtil.normalizePhone(toPhoneNumber);
        String messageSid = "MOCK-" + UUID.randomUUID().toString().replace("-", "");

        log.info("\n================= MOCK SMS SENDER =================" +
                        "\nTo: {}" +
                        "\nMessage: {}" +
                        "\nGenerated SID: {}" +
                        "\n===================================================",
                safePhoneNumber, messageBody, messageSid);

        return messageSid;
    }
}
