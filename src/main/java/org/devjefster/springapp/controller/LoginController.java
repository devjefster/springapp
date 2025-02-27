package org.devjefster.springapp.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public LoginController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Instant now = Instant.now();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                        .subject(username)
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(3600)) // 1-hour expiration
                        .claim("scope", "read write")
                        .build()
        )).getTokenValue();

        return Map.of("access_token", token);
    }
}
