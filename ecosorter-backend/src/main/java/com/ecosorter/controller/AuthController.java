package com.ecosorter.controller;

import com.ecosorter.dto.AuthResponse;
import com.ecosorter.dto.DeviceActivateRequest;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.LoginRequest;
import com.ecosorter.dto.RegisterRequest;
import com.ecosorter.dto.UserResponse;
import com.ecosorter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    @PostMapping("/device/activate")
    public ResponseEntity<DeviceListResponse> activateDevice(
            @Valid @RequestBody DeviceActivateRequest request) {
        DeviceListResponse response = authService.activateDevice(request);
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
    
    @PostMapping("/face-login-with-file")
    public ResponseEntity<AuthResponse> faceLoginWithFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        AuthResponse response = authService.faceLoginWithFile(file);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        UserResponse userResponse = convertToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }
    
    private UserResponse convertToUserResponse(com.ecosorter.model.User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setId(user.getId() != null ? user.getId().toString() : null);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole() != null ? user.getRole().name() : null);
        response.setIsActive(user.getIsActive());
        response.setLastLogin(user.getLastLogin() != null ? user.getLastLogin().toString() : null);
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
        
        UserResponse.UserProfileDto profileDto = new UserResponse.UserProfileDto();
        profileDto.setAvatar(user.getAvatarUrl());
        profileDto.setPhone(user.getPhone());
        profileDto.setFullName(user.getUsername());
        response.setProfile(profileDto);
        
        return response;
    }
}