package com.satvik.stockpdfspringboot.Authentication.dto;

import com.satvik.stockpdfspringboot.Authentication.util.ExtendedEmailValidator;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {


    private String username;

    @ExtendedEmailValidator
    private String email;

    @Size(min = 6, message = "Password must be at least 8 characters long")
    private String password;
}