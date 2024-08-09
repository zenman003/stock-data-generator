package com.satvik.stockpdfspringboot.Authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.analysis.function.Abs;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDto {

    String token;
    String password;
    String confirmPassword;
}
