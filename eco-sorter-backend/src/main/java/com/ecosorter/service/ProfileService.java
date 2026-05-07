package com.ecosorter.service;

import com.ecosorter.dto.UpdateUserRequest;
import com.ecosorter.dto.UserResponse;
import com.ecosorter.model.User;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    
    private final UserRepository userRepository;
    
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserResponse getProfileByUserId(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return convertToUserResponse(user);
    }
    
    public UserResponse updateProfile(Long userId, UpdateUserRequest request) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            user.setUsername(request.getUsername().trim());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
    }
    
    public UserResponse updateAvatar(Long userId, String avatarUrl) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        user.setAvatarUrl(avatarUrl);
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
    }
    
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole() != null ? user.getRole().name() : null);
        response.setIsActive(user.getIsActive());
        response.setLastLogin(user.getLastLogin());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        
        UserResponse.UserProfileDto profileDto = new UserResponse.UserProfileDto();
        profileDto.setAvatar(user.getAvatarUrl());
        profileDto.setPhone(user.getPhone());
        profileDto.setFullName(user.getUsername());
        response.setProfile(profileDto);
        
        return response;
    }
}
