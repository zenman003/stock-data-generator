package com.satvik.stockpdfspringboot.Authentication.controller;


import com.satvik.stockpdfspringboot.Authentication.dto.ForgotPassWordDto;
import com.satvik.stockpdfspringboot.Authentication.dto.PasswordResetDto;
import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.Authentication.repositories.PasswordTokenRepository;
import com.satvik.stockpdfspringboot.Authentication.service.PasswordResetService;
import com.satvik.stockpdfspringboot.Authentication.util.MailSenderHelper;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class PasswordChangeController {

    private final MailSenderHelper mailSenderHelper;
    private final UserService userService;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordResetService passwordResetService;

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
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        if(passwordResetToken == null) {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
        if(passwordResetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Token has expired");
        }
        passwordResetToken.setEnabled(true);
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
        if(!passwordResetToken.isEnabled()) {
            return ResponseEntity.badRequest().body("Token is not enabled");
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordResetDto.getPassword());
        userService.save(user);
        passwordResetToken.setEnabled(false);
        return ResponseEntity.ok("Password changed successfully");
    }
}
