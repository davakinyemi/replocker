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

        /* try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, UTF_8.name());

            Map<String, Object> properties = new HashMap<>();
            properties.put("collectionName", collectionName);
            properties.put("requestId", requestId);

            Context context = new Context();
            context.setVariables(properties);

            helper.setFrom(this.systemEmail);
            helper.setTo(toEmail);
            helper.setSubject("Access Request Received: " + collectionName);
            helper.setText(this.templateEngine.process("email/access-pending", context), true);

            this.mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send request-pending email: {}", e.getMessage());
        } */
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

        /* try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, UTF_8.name());

            Map<String, Object> properties = new HashMap<>();
            properties.put("collectionName", collectionName);
            properties.put("accessToken", accessToken);
            properties.put("expiresAt", expiresAt.format(DateTimeFormatter.ofLocalizedPattern("dd MMM YYYY HH:mm")));

            Context context = new Context();
            context.setVariables(properties);

            helper.setFrom(this.systemEmail);
            helper.setTo(toEmail);
            helper.setSubject("Access Granted: " + collectionName);
            helper.setText(this.templateEngine.process("email/access-accepted", context), true);

            this.mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send request-accepted email: {}", e.getMessage());
        } */
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

        /* try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, UTF_8.name());

            Map<String, Object> properties = new HashMap<>();
            properties.put("collectionName", collectionName);
            properties.put("adminComment", adminComment);

            Context context = new Context();
            context.setVariables(properties);

            helper.setFrom(this.systemEmail);
            helper.setTo(toEmail);
            helper.setSubject("Access Denied: " + collectionName);
            helper.setText(this.templateEngine.process("email/access-denied", context), true);

            this.mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send request-rejected email: {}", e.getMessage());
        } */
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
