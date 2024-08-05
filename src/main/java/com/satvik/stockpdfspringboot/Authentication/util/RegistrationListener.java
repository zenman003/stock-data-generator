package com.satvik.stockpdfspringboot.Authentication.util;

import com.satvik.stockpdfspringboot.Authentication.model.VerificationToken;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationComplete> {

    private final UserService userService;
    private final MessageSource messageSource;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationComplete onRegistrationComplete) {
        this.confirmRegistration(onRegistrationComplete);
    }

    private void confirmRegistration(OnRegistrationComplete onRegistrationComplete) {
        User user = onRegistrationComplete.getUser();
        if (user == null) {
            return;
        }

        String token;
        if (user.getVerificationToken() == null) {
            token = UUID.randomUUID().toString();
            userService.createVerificationToken(user, token);
        } else {
            userService.generateNewVerificationToken(user.getUsername());
            token = user.getVerificationToken().getToken();
        }

        String recipientAddress = user.getEmail();
        String confirmationUrl = onRegistrationComplete.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Registration Successful. To confirm your registration, please click on the link below: \r\n";

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipientAddress);
            email.setSubject("Registration Confirmation");
            email.setText(message + " \r\n" + confirmationUrl);
            mailSender.send(email);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}
