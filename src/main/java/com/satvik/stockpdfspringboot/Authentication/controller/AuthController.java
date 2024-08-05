package com.satvik.stockpdfspringboot.Authentication.controller;

import com.satvik.stockpdfspringboot.Authentication.dto.LoginDto;
import com.satvik.stockpdfspringboot.Authentication.dto.RegisterDto;
import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.Authentication.model.VerificationToken;
import com.satvik.stockpdfspringboot.User.service.UserService;
import com.satvik.stockpdfspringboot.Authentication.util.OnRegistrationComplete;
import com.satvik.stockpdfspringboot.security.JwtGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final LinkedHashSet<Long> unVerifiedUsers;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody  RegisterDto registerDto, HttpServletRequest request) {
        try{
            User registered = userService.saveRegisteredUser(registerDto);
            String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api";
            applicationEventPublisher.publishEvent(new OnRegistrationComplete(registered, request.getLocale(), appUrl));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        String token = jwtGenerator.generate(authenticationResponse);
        return ResponseEntity.ok("Bearer " + token);
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token){
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
            return ResponseEntity.badRequest().body("Token expired");
        }
        user.setEnabled(true);
        unVerifiedUsers.remove(user.getId());
        userService.saveRegisteredUser(user);
        return ResponseEntity.ok("User verified successfully");
    }

    @RequestMapping(value = "/resendRegistrationToken", method = RequestMethod.POST)
    public ResponseEntity<String> resendRegistrationToken(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api";
        User user =userService.findByUsername(loginDto.getUsername());
        if(user == null){
            return ResponseEntity.badRequest().body("User not found");
        }
        if(user.isEnabled()){
            return ResponseEntity.badRequest().body("User is already verified");
        }
        applicationEventPublisher.publishEvent(new OnRegistrationComplete(user, request.getLocale(), appUrl));
        return ResponseEntity.ok("Token resent successfully");
    }
}
