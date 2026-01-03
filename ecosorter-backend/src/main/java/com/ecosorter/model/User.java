package com.ecosorter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User entity representing application users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    
    private String id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Indexed(unique = true)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Indexed(unique = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonIgnore
    private String password;
    
    @Field("role")
    private UserRole role = UserRole.USER;
    
    @Field("profile")
    private UserProfile profile;
    
    @Field("preferences")
    private UserPreferences preferences;
    
    @Field("statistics")
    private UserStatistics statistics;
    
    @Field("status")
    private UserStatus status = UserStatus.ACTIVE;
    
    @Field("last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Field("login_attempts")
    private Integer loginAttempts = 0;
    
    @Field("lock_until")
    private LocalDateTime lockUntil;
    
    @Field("password_reset_token")
    @JsonIgnore
    private String passwordResetToken;
    
    @Field("password_reset_expires")
    @JsonIgnore
    private LocalDateTime passwordResetExpires;
    
    @Field("email_verification_token")
    @JsonIgnore
    private String emailVerificationToken;
    
    @Field("email_verified")
    private Boolean emailVerified = false;
    
    @Field("two_factor_secret")
    @JsonIgnore
    private String twoFactorSecret;
    
    @Field("two_factor_enabled")
    private Boolean twoFactorEnabled = false;
    
    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Get full name from profile
     */
    public String getFullName() {
        if (profile != null) {
            String firstName = profile.getFirstName() != null ? profile.getFirstName() : "";
            String lastName = profile.getLastName() != null ? profile.getLastName() : "";
            return (firstName + " " + lastName).trim();
        }
        return "";
    }
    
    /**
     * Check if account is locked
     */
    public boolean isLocked() {
        return lockUntil != null && lockUntil.isAfter(LocalDateTime.now());
    }
    
    /**
     * User roles
     */
    public enum UserRole {
        USER, ADMIN, OPERATOR
    }
    
    /**
     * User status
     */
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}