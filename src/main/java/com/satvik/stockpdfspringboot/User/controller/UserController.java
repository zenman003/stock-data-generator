package com.satvik.stockpdfspringboot.User.controller;

import com.satvik.stockpdfspringboot.User.service.UserService;
import com.satvik.stockpdfspringboot.User.dto.DeleteDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/delete")
    @Transactional
    public void deleteByUsername(@RequestBody DeleteDto deleteDto) {
        String username = deleteDto.getUsername();
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        userService.deleteByUsername(username);
    }


}
