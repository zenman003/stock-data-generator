package com.satvik.stockpdfspringboot.Authentication.service;

import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.Authentication.repositories.PasswordTokenRepository;
import com.satvik.stockpdfspringboot.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordTokenRepository passwordTokenRepository;

    public void createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }
}
