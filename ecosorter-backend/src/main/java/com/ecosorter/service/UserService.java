package com.ecosorter.service;

import com.ecosorter.dto.UserResponse;
import com.ecosorter.model.User;
import org.springframework.stereotype.Service;

/**
 * User service for user-related operations
 */
@Service
public class UserService {
    
    /**
     * Convert User entity to UserResponse DTO
     */
    public UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setLastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : null);
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
        
        // Convert profile
        if (user.getProfile() != null) {
            UserResponse.UserProfileDto profileDto = UserResponse.UserProfileDto.builder()
                    .firstName(user.getProfile().getFirstName())
                    .lastName(user.getProfile().getLastName())
                    .avatar(user.getProfile().getAvatar())
                    .phone(user.getProfile().getPhone())
                    .department(user.getProfile().getDepartment())
                    .position(user.getProfile().getPosition())
                    .fullName(user.getFullName())
                    .build();
            response.setProfile(profileDto);
        }
        
        // Convert preferences
        if (user.getPreferences() != null) {
            UserResponse.UserNotificationsDto notificationsDto = UserResponse.UserNotificationsDto.builder()
                    .email(user.getPreferences().getNotifications() != null ? 
                           user.getPreferences().getNotifications().getEmail() : true)
                    .push(user.getPreferences().getNotifications() != null ? 
                          user.getPreferences().getNotifications().getPush() : true)
                    .classification(user.getPreferences().getNotifications() != null ? 
                                    user.getPreferences().getNotifications().getClassification() : true)
                    .build();
            
            UserResponse.UserPreferencesDto preferencesDto = UserResponse.UserPreferencesDto.builder()
                    .theme(user.getPreferences().getTheme() != null ? 
                           user.getPreferences().getTheme().name().toLowerCase() : "light")
                    .language(user.getPreferences().getLanguage())
                    .notifications(notificationsDto)
                    .build();
            response.setPreferences(preferencesDto);
        }
        
        // Convert statistics
        if (user.getStatistics() != null) {
            UserResponse.UserStatisticsDto statisticsDto = UserResponse.UserStatisticsDto.builder()
                    .totalClassifications(user.getStatistics().getTotalClassifications())
                    .correctClassifications(user.getStatistics().getCorrectClassifications())
                    .accuracy(user.getStatistics().getAccuracy())
                    .totalWasteWeight(user.getStatistics().getTotalWasteWeight())
                    .carbonSaved(user.getStatistics().getCarbonSaved())
                    .build();
            response.setStatistics(statisticsDto);
        }
        
        return response;
    }
}