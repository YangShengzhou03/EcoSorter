package com.ecosorter.service;

import com.ecosorter.dto.AuthResponse;
import com.ecosorter.dto.LoginRequest;
import com.ecosorter.dto.RegisterRequest;
import com.ecosorter.dto.UserResponse;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.User;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    private UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setId(user.getId() != null ? user.getId().toString() : null);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setIsActive(user.getIsActive());
        response.setLastLogin(user.getLastLogin() != null ? user.getLastLogin().toString() : null);
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
        
        UserResponse.UserProfileDto profileDto = new UserResponse.UserProfileDto();
        profileDto.setAvatar(user.getAvatarUrl());
        profileDto.setPhone(user.getPhone());
        profileDto.setFullName(user.getFullName());
        response.setProfile(profileDto);
        
        return response;
    }
    
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already in use!");
        }
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(User.UserRole.RESIDENT);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        
        UserResponse userResponse = convertToUserResponse(savedUser);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("simple-token-" + savedUser.getId());
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getIdentifier())
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        UserResponse userResponse = convertToUserResponse(user);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("simple-token-" + user.getId());
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        String userIdStr = refreshToken.replace("simple-token-", "");
        Long userId = Long.parseLong(userIdStr);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        UserResponse userResponse = convertToUserResponse(user);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(refreshToken);
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public void logout(String token) {
    }
    
    public UserResponse getCurrentUser(String token) {
        String userIdStr = token.replace("simple-token-", "");
        Long userId = Long.parseLong(userIdStr);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return convertToUserResponse(user);
    }
}