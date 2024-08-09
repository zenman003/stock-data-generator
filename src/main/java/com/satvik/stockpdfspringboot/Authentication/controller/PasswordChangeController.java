package com.satvik.stockpdfspringboot.Authentication.controller;


import com.satvik.stockpdfspringboot.Authentication.dto.PasswordResetDto;
import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.Authentication.repositories.PasswordTokenRepository;
import com.satvik.stockpdfspringboot.Authentication.service.PasswordResetService;
import com.satvik.stockpdfspringboot.Authentication.util.MailSenderHelper;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordChangeController {

    private final MailSenderHelper mailSenderHelper;
    private final UserService userService;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> passwordReset(@RequestParam String email, HttpServletRequest request) {
        User user = userService.findByEmail(email);
        passwordResetService.createPasswordResetTokenForUser(user);
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api";
        try {
            mailSenderHelper.sendPasswordResetMail(user, appUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Password reset link sent to email");
    }

    @PostMapping("/confirm-password-token")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        log.info("Confirming password reset token");
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        if(passwordResetToken == null) {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
        if(passwordResetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Token has expired");
        }
        passwordResetToken.setEnabled(true);
        log.info("Password reset token confirmed");
        return ResponseEntity.ok("Token is valid");
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordResetDto passwordResetDto) {
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(passwordResetDto.getToken());
        if(passwordResetToken == null) {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
        if(passwordResetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Token has expired");
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
        userService.save(user);
        passwordTokenRepository.delete(passwordResetToken);
        return ResponseEntity.ok("Password changed successfully");
    }
}
