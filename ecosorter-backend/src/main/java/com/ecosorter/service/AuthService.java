package com.ecosorter.service;

import com.ecosorter.dto.AuthResponse;
import com.ecosorter.dto.LoginRequest;
import com.ecosorter.dto.RegisterRequest;
import com.ecosorter.dto.UserResponse;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.User;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.security.JwtUtils;
import com.ecosorter.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Authentication service implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    
    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already in use!");
        }
        
        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(User.UserRole.USER)
                .status(User.UserStatus.ACTIVE)
                .loginAttempts(0)
                .emailVerified(false)
                .twoFactorEnabled(false)
                .build();
        
        // Set profile information
        if (registerRequest.getFirstName() != null || registerRequest.getLastName() != null) {
            user.setProfile(com.ecosorter.model.UserProfile.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .phone(registerRequest.getPhone())
                    .build());
        }
        
        // Set default preferences
        user.setPreferences(com.ecosorter.model.UserPreferences.builder()
                .theme(com.ecosorter.model.UserPreferences.Theme.LIGHT)
                .language("zh-CN")
                .notifications(com.ecosorter.model.UserNotifications.builder()
                        .email(true)
                        .push(true)
                        .classification(true)
                        .build())
                .build());
        
        // Set default statistics
        user.setStatistics(com.ecosorter.model.UserStatistics.builder()
                .totalClassifications(0)
                .correctClassifications(0)
                .accuracy(0.0)
                .totalWasteWeight(0.0)
                .carbonSaved(0.0)
                .build());
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        
        // Generate tokens
        String accessToken = jwtUtils.generateTokenFromUsername(savedUser.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(savedUser.getUsername());
        
        // Convert to response
        UserResponse userResponse = userService.convertToUserResponse(savedUser);
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(86400000L) // 24 hours
                .user(userResponse)
                .build();
    }
    
    /**
     * User login
     */
    public AuthResponse login(Authentication authentication, LoginRequest loginRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate tokens
        String accessToken = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());
        
        // Convert to response
        UserResponse userResponse = userService.convertToUserResponse(user);
        
        log.info("User logged in successfully: {}", user.getUsername());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(86400000L) // 24 hours
                .user(userResponse)
                .build();
    }
    
    /**
     * Refresh access token
     */
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate new access token
        String newAccessToken = jwtUtils.generateTokenFromUsername(username);
        
        // Convert to response
        UserResponse userResponse = userService.convertToUserResponse(user);
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Keep the same refresh token
                .expiresIn(86400000L) // 24 hours
                .user(userResponse)
                .build();
    }
    
    /**
     * User logout
     */
    public void logout(Authentication authentication) {
        // In a real implementation, you might want to:
        // 1. Add the token to a blacklist
        // 2. Remove refresh tokens from database
        // 3. Clear user sessions
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        log.info("User logged out: {}", userPrincipal.getUsername());
    }
    
    /**
     * Get current user information
     */
    public UserResponse getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return userService.convertToUserResponse(user);
    }
}