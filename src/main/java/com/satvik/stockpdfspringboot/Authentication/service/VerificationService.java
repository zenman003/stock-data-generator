package com.satvik.stockpdfspringboot.Authentication.service;

import com.satvik.stockpdfspringboot.Authentication.model.VerificationToken;
import com.satvik.stockpdfspringboot.Authentication.repositories.VerificationTokenRepository;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public void generateNewVerificationToken(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        VerificationToken token = user.getVerificationToken();
        String newToken = UUID.randomUUID().toString();
        token.updateToken(newToken);
        verificationTokenRepository.save(token);
    }


    @Transactional
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
