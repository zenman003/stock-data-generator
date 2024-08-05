package com.satvik.stockpdfspringboot.service;

import com.satvik.stockpdfspringboot.Authentication.dto.RegisterDto;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import com.satvik.stockpdfspringboot.Authentication.repositories.VerificationTokenRepository;
import com.satvik.stockpdfspringboot.User.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void when_SaveRegisteredUser_thenReturnUser() {
        // Arrange
        RegisterDto registerDto = RegisterDto.builder()
                .username("satvik")
                .password("password")
                .email("satvik@gmail.com")
                .build();

        User user = User.builder()
                .username("satvik")
                .password("encodedPassword")
                .email("satvik@gmail.com")
                .enabled(false)
                .build();

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.saveRegisteredUser(registerDto);

        // Assert
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("satvik", savedUser.getUsername());
        Assertions.assertEquals("encodedPassword", savedUser.getPassword());
        Assertions.assertEquals("satvik@gmail.com", savedUser.getEmail());
        Assertions.assertFalse(savedUser.isEnabled());
    }

    @Test
    public void when_FindByUsername_thenReturnUser() {
        // Arrange
        String username = "satvik";

        when(userRepository.findByUsername(any(String.class))).thenReturn(User.builder()
                .username("satvik")
                .password("encodedPassword")
                .email("satvik@gmail.com")
                .enabled(false)
                .build());

        // Act
        User user = userService.findByUsername(username);

        // Assert
        Assertions.assertNotNull(user);
        Assertions.assertEquals("satvik", user.getUsername());
        Assertions.assertEquals("encodedPassword", user.getPassword());
        Assertions.assertEquals("satvik@gmail.com", user.getEmail());
        Assertions.assertFalse(user.isEnabled());


    }

}
