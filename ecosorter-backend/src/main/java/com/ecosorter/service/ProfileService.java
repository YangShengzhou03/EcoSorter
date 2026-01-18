package com.ecosorter.service;

import com.ecosorter.dto.ProfileResponse;
import com.ecosorter.model.User;
import com.ecosorter.model.UserProfile;
import com.ecosorter.repository.UserProfileRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    
    public ProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }
    
    public ProfileResponse getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfile(userId));
        return convertToResponse(user, profile);
    }
    
    public ProfileResponse updateProfile(Long userId, UserProfile profileData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfile(userId));
        
        profile.setFullName(profileData.getFullName());
        profile.setBio(profileData.getBio());
        profile.setBirthDate(profileData.getBirthDate());
        profile.setGender(profileData.getGender());
        profile.setOccupation(profileData.getOccupation());
        profile.setCompany(profileData.getCompany());
        profile.setWebsite(profileData.getWebsite());
        profile.setLocation(profileData.getLocation());
        profile.setTimezone(profileData.getTimezone());
        profile.setLanguage(profileData.getLanguage());
        
        UserProfile updatedProfile = userProfileRepository.save(profile);
        return convertToResponse(user, updatedProfile);
    }
    
    public ProfileResponse updateAvatar(Long userId, String avatarUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfile(userId));
        return convertToResponse(user, profile);
    }
    
    private UserProfile createDefaultProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setTimezone("Asia/Shanghai");
        profile.setLanguage("zh-CN");
        return userProfileRepository.save(profile);
    }
    
    private ProfileResponse convertToResponse(User user, UserProfile profile) {
        ProfileResponse response = new ProfileResponse();
        response.setId(profile.getId());
        response.setAvatar(user.getAvatarUrl());
        response.setFullName(profile.getFullName());
        response.setBio(profile.getBio());
        response.setBirthDate(profile.getBirthDate());
        response.setGender(profile.getGender());
        response.setOccupation(profile.getOccupation());
        response.setCompany(profile.getCompany());
        response.setWebsite(profile.getWebsite());
        response.setLocation(profile.getLocation());
        response.setTimezone(profile.getTimezone());
        response.setLanguage(profile.getLanguage());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        return response;
    }
}
