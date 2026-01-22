package RandevuApp.domain.notification.service.strategy;

import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationStrategy implements NotificationStrategy {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void send(NotificationRequest request) {
        Context context = new Context();
        
        // Add standard variables import org.thymeleaf.context.Context;
        context.setVariable("subject", request.getSubject());
        context.setVariable("message", request.getMessage()); // Fallback message
        
        // Add custom variables from request
        if (request.getVariables() != null) {
            request.getVariables().forEach(context::setVariable);
        }

        // Determine template based on category
        String templateName = "email/" + request.getCategory().name().toLowerCase(Locale.ENGLISH).replace("_", "-");

        try {
            String htmlContent = templateEngine.process(templateName, context);

            log.info("Sending HTML EMAIL to User ID: {} (Address: {})", request.getUserId(), request.getRecipient());
            
            sendHtmlEmail(request.getRecipient(), request.getSubject(), htmlContent);
            
            log.info("Email sent successfully to {}", request.getRecipient());
            
        } catch (Exception e) {
            log.error("Failed to send email to {}. Template: {}", request.getRecipient(), templateName, e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    @Override
    public NotificationChannel supports() {
        return NotificationChannel.EMAIL;
    }
}
