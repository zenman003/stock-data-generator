package com.satvik.stockpdfspringboot.Authentication.util;

import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.User.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSenderHelper {

    private final JavaMailSender javaMailSender;

    public void sendPasswordResetMail(User user, String appUrl) throws MessagingException {
        log.info("Sending password reset mail to user: " + user.getEmail());
        String email = user.getEmail();
        PasswordResetToken passwordResetToken = user.getPasswordResetToken();
        String token = passwordResetToken.getToken();
        String resetUrl = appUrl + "/confirm-password-token?token=" + token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String plainTextContent = "To reset your password, click the link below:\n\n"
                + resetUrl;

        helper.setTo(email);
        helper.setSubject("Reset Password");
        helper.setText(plainTextContent); // Using plain text

        javaMailSender.send(mimeMessage);
        log.info("Password reset mail sent to user: " + user.getEmail());
    }
}
