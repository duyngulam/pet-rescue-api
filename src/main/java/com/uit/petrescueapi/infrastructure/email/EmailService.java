package com.uit.petrescueapi.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Infrastructure service that sends emails via SMTP (Gmail).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from:noreply@petrescue.com}")
    private String fromAddress;

    @Value("${app.email.verification-url:http://localhost:8080/api/v1/auth/verify-email}")
    private String verificationBaseUrl;

    /**
     * Send verification email asynchronously.
     */
    @Async
    public void sendVerificationEmail(String to, String username, String token) {
        String link = verificationBaseUrl + "?token=" + token;
        String subject = "Verify your Pet Rescue account";
        String body = """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                  <h2>Welcome to Pet Rescue, %s!</h2>
                  <p>Thank you for registering. Please verify your email address by clicking the link below:</p>
                  <p><a href="%s" style="display:inline-block;padding:12px 24px;background:#4CAF50;color:#fff;text-decoration:none;border-radius:4px;">Verify Email</a></p>
                  <p>Or copy this link into your browser:</p>
                  <p><code>%s</code></p>
                  <p>This link expires in 24 hours.</p>
                  <br/>
                  <p style="color:#888;">— Pet Rescue Team</p>
                </body>
                </html>
                """.formatted(username, link, link);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Verification email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", to, e.getMessage());
        }
    }
}
