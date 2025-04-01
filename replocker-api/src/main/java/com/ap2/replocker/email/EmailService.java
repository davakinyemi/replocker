package com.ap2.replocker.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String systemEmail;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    @Async
    public void sendRequestPending(String toEmail, UUID requestId, String collectionName) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("collectionName", collectionName);
        properties.put("requestId", requestId);

        this.sendEmail(
                toEmail,
                "Access Request Received: " + collectionName,
                "email/access-pending",
                properties,
                "request-pending"
        );
    }

    @Async
    public void sendRequestAccepted(String toEmail, String accessToken, String collectionName, LocalDateTime expiresAt) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("collectionName", collectionName);
        properties.put("accessToken", accessToken);
        properties.put("expiresAt", expiresAt.format(DATE_TIME_FORMATTER));

        this.sendEmail(
                toEmail,
                "Access Granted: " + collectionName,
                "email/access-accepted",
                properties,
                "request-accepted"
        );
    }

    @Async
    public void sendRequestRejected(String toEmail, String adminComment, String collectionName) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("collectionName", collectionName);
        properties.put("adminComment", adminComment);

        this.sendEmail(
                toEmail,
                "Access Denied: " + collectionName,
                "email/access-denied",
                properties,
                "request-rejected"
        );
    }

    @Async
    public void sendTokenExpiryWarning(String toEmail, LocalDateTime expiry, String collectionName, String accessToken) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("collectionName", collectionName);
        properties.put("expiry", expiry);
        properties.put("accessToken", accessToken);

        this.sendEmail(
                toEmail,
                "Access Token Expiring Soon",
                "email/token-expiring",
                properties,
                "token-expiring"
        );
    }

    public void sendTokenExpiredNotification(String toEmail, String collectionName, String accessToken) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("collectionName", collectionName);
        properties.put("accessToken", accessToken);

        this.sendEmail(
                toEmail,
                "Access Token Expired",
                "email/token-expired",
                properties,
                "token-expired"
        );
    }

    private void sendEmail(String toEmail, String subject, String templateName, Map<String, Object> properties, String emailType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    UTF_8.name()
            );

            Context context = new Context();
            context.setVariables(properties);

            helper.setFrom(this.systemEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(templateEngine.process(templateName, context), true);

            this.mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send {} email: {}", emailType, e.getMessage());
        }
    }
}
