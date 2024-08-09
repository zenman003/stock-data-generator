package com.satvik.stockpdfspringboot.Authentication.dto;

import lombok.Data;

@Data
public class PasswordResetDto {

    String token;
    String password;
    String confirmPassword;
}
