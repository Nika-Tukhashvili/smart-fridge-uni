package org.example.smartfridgeuni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${notification.email.from}")
    private String fromEmail;

    private final JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String message) {
        try {

            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setFrom(fromEmail);
            emailMessage.setTo(to);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            emailSender.send(emailMessage);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new CustomException("Email sending failed: " + e);
        }
    }
}
