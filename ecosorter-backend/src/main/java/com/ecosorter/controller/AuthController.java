package com.ecosorter.controller;

import com.ecosorter.dto.AuthResponse;
import com.ecosorter.dto.LoginRequest;
import com.ecosorter.dto.RegisterRequest;
import com.ecosorter.dto.UserResponse;
import com.ecosorter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        authService.logout(user.getId().toString());
        return ResponseEntity.ok("Logged out successfully");
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        UserResponse userResponse = authService.getCurrentUser(user.getId().toString());
        return ResponseEntity.ok(userResponse);
    }
}