package com.satvik.stockpdfspringboot.User.service;

import com.satvik.stockpdfspringboot.Authentication.dto.RegisterDto;
import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.Authentication.model.VerificationToken;
import com.satvik.stockpdfspringboot.Authentication.repositories.PasswordTokenRepository;
import com.satvik.stockpdfspringboot.Authentication.repositories.VerificationTokenRepository;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final LinkedHashMap<Long, Date> unVerifiedUsers;
    private final PasswordTokenRepository passwordTokenRepository;

    public User saveRegisteredUser(RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        if(userRepository.findByEmailIgnoreCase(registerDto.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setRole("USER");
        userRepository.save(user);
        unVerifiedUsers.put(user.getId(), new Date());
        return user;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
