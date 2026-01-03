package com.ecosorter.dto;

import com.ecosorter.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * User response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    
    private String id;
    private String username;
    private String email;
    private User.UserRole role;
    private UserProfileDto profile;
    private UserPreferencesDto preferences;
    private UserStatisticsDto statistics;
    private User.UserStatus status;
    private String lastLoginAt;
    private String createdAt;
    private String updatedAt;
    
    /**
     * User profile DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProfileDto {
        private String firstName;
        private String lastName;
        private String avatar;
        private String phone;
        private String department;
        private String position;
        private String fullName;
    }
    
    /**
     * User preferences DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserPreferencesDto {
        private String theme;
        private String language;
        private UserNotificationsDto notifications;
    }
    
    /**
     * User notifications DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserNotificationsDto {
        private Boolean email;
        private Boolean push;
        private Boolean classification;
    }
    
    /**
     * User statistics DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserStatisticsDto {
        private Integer totalClassifications;
        private Integer correctClassifications;
        private Double accuracy;
        private Double totalWasteWeight;
        private Double carbonSaved;
    }
}