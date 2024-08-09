package com.satvik.stockpdfspringboot.User.controller;

import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/delete")
    @Transactional
    public void deleteByUsername(@RequestParam String username) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        userService.deleteByUsername(username);
    }


    @GetMapping("admin/get_all_users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }



}
