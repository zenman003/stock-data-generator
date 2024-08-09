package com.satvik.stockpdfspringboot.Authentication.service;

import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.Authentication.repositories.PasswordTokenRepository;
import com.satvik.stockpdfspringboot.User.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordTokenRepository passwordTokenRepository;

    @Transactional
    public void createPasswordResetTokenForUser(User user) {
        log.info("Creating password reset token for user: " + user.getEmail());
        if(user.getPasswordResetToken() != null) {
            user.getPasswordResetToken().updateToken(UUID.randomUUID().toString());
        } else {
            user.setPasswordResetToken(new PasswordResetToken(UUID.randomUUID().toString(), user));
        }
        passwordTokenRepository.save(user.getPasswordResetToken());

        log.info("Password reset token created for user: " + user.getEmail());
    }

    @Transactional(readOnly = true)
    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }
}
