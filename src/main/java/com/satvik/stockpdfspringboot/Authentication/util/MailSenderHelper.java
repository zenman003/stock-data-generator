package com.satvik.stockpdfspringboot.Authentication.util;


import com.satvik.stockpdfspringboot.Authentication.service.PasswordResetService;
import com.satvik.stockpdfspringboot.User.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.MimeMessageHelper;

@Component
@RequiredArgsConstructor
public class MailSenderHelper {

    private final JavaMailSender javaMailSender;

    public void sendPasswordResetMail(User user, String appUrl) throws MessagingException {
        String email = user.getEmail();
        String token = user.getPasswordResetToken().getToken();
        String resetUrl = appUrl + "/confirm-password-token?token=" + token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = "<p>To reset your password, click the link below:</p>"
                + "<p><a href=\"" + resetUrl + "\">Reset Password</a></p>";

        helper.setTo(email);
        helper.setSubject("Reset Password");
        helper.setText(htmlContent, true); // true indicates that the content is HTML

        javaMailSender.send(mimeMessage);
    }
}
