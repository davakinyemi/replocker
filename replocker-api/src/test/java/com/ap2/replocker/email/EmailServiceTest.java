package com.ap2.replocker.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Dave AKN
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    JavaMailSender mailSender;

    @Mock
    SpringTemplateEngine templateEngine;

    @InjectMocks
    EmailService emailService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(emailService, "systemEmail", "noreply@company.com");
    }

    @Test
    void sendTokenExpiryWarningEmail() throws Exception {
        MimeMessage mockMessage = Mockito.mock(MimeMessage.class);
        when(this.mailSender.createMimeMessage()).thenReturn(mockMessage);
        when(templateEngine.process(eq("email/token-expiring"), any(Context.class)))
                .thenReturn("<html>Mock template content</html>");

        this.emailService.sendTokenExpiryWarning(
                "user@company.com",
                LocalDateTime.of(2025, 4, 5, 14, 30),
                "Financial Reports",
                "654321"
        );

        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email/token-expiring"), contextCaptor.capture());


        Context templateContext = contextCaptor.getValue();
        assertThat(templateContext.getVariable("collectionName")).isEqualTo("Financial Reports");

        assertThat(templateContext.getVariable("accessToken")).isEqualTo("654321");
    }

    @Test
    void testEmailErrorHandling() {
        when(this.mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP error"));

        assertDoesNotThrow(() -> this.emailService.sendRequestPending("user@company.com", UUID.randomUUID(), "Test Collection"));
    }

    @Test
    void verifyEmailConfiguration() {
        assertThat(ReflectionTestUtils.getField(emailService, "systemEmail"))
                .isEqualTo("noreply@company.com");
    }
}
