package org.devjefster.springapp.controller;

import org.devjefster.springapp.model.entities.User;
import org.devjefster.springapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");

        User user = userService.registerUser(username, password);
        return Map.of("message", "User registered successfully", "username", user.getUsername());
    }
}
