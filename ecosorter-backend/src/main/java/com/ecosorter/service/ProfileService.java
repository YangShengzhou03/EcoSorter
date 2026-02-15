package com.ecosorter.service;

import com.ecosorter.dto.ProfileResponse;
import com.ecosorter.model.User;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    
    private final UserRepository userRepository;
    
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public ProfileResponse getProfileByUserId(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return convertToResponse(user);
    }
    
    public ProfileResponse updateProfile(Long userId, User profileData) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        
        if (profileData.getPhone() != null) {
            user.setPhone(profileData.getPhone());
        }
        if (profileData.getAddress() != null) {
            user.setAddress(profileData.getAddress());
        }
        
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }
    
    public ProfileResponse updateAvatar(Long userId, String avatarUrl) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        user.setAvatarUrl(avatarUrl);
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }
    
    private ProfileResponse convertToResponse(User user) {
        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatarUrl());
        response.setFullName(user.getUsername());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
