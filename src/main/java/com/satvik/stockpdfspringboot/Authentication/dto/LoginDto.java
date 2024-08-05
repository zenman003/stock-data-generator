package com.satvik.stockpdfspringboot.Authentication.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {
    private String username;
    private String password;
}
