package com.ecosorter.controller;

import com.ecosorter.dto.AuthResponse;
import com.ecosorter.dto.LoginRequest;
import com.ecosorter.dto.RegisterRequest;
import com.ecosorter.model.User;
import com.ecosorter.security.JwtUtils;
import com.ecosorter.security.UserDetailsServiceImpl;
import com.ecosorter.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and authorization APIs")
public class AuthController {
    
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    
    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registering new user: {}", registerRequest.getUsername());
        
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * User login endpoint
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return access token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("User login attempt: {}", loginRequest.getIdentifier());
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getIdentifier(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        AuthResponse response = authService.login(authentication, loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Refresh access token
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String refreshToken) {
        log.info("Refreshing access token");
        
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Logout user
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate tokens")
    public ResponseEntity<String> logout() {
        log.info("User logout");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            authService.logout(authentication);
        }
        
        return ResponseEntity.ok("Logged out successfully");
    }
    
    /**
     * Get current user info
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user information")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        UserResponse userResponse = authService.getCurrentUser(authentication);
        return ResponseEntity.ok(userResponse);
    }
}