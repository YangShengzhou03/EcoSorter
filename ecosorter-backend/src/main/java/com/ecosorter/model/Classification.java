package com.ecosorter.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
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
 * Classification record entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "classifications")
public class Classification {
    
    private String id;
    
    @NotBlank(message = "User ID is required")
    @Indexed
    private String userId;
    
    @Field("image_url")
    private String imageUrl;
    
    @Field("image_path")
    private String imagePath;
    
    @Field("thumbnail_url")
    private String thumbnailUrl;
    
    @Field("thumbnail_path")
    private String thumbnailPath;
    
    @NotBlank(message = "Item name is required")
    @Field("item_name")
    private String itemName;
    
    @Field("description")
    private String description;
    
    @NotNull(message = "Predicted category is required")
    @Field("predicted_category")
    private String predictedCategory;
    
    @NotNull(message = "Confidence score is required")
    @Min(0)
    @Max(100)
    @Field("confidence_score")
    private Double confidenceScore;
    
    @Field("ai_model")
    private String aiModel;
    
    @Field("ai_model_version")
    private String aiModelVersion;
    
    @Field("processing_time")
    private Long processingTime;
    
    @Field("user_verified")
    private Boolean userVerified = false;
    
    @Field("user_category")
    private String userCategory;
    
    @Field("verification_status")
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    
    @Field("feedback")
    private ClassificationFeedback feedback;
    
    @Field("location")
    private ClassificationLocation location;
    
    @Field("device_info")
    private DeviceInfo deviceInfo;
    
    @Field("tags")
    private List<String> tags;
    
    @Field("notes")
    private String notes;
    
    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Verification status
     */
    public enum VerificationStatus {
        PENDING, VERIFIED, REJECTED, FLAGGED
    }
    
    /**
     * Classification feedback
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClassificationFeedback {
        private Boolean correct;
        private String comment;
        private String suggestedCategory;
        private LocalDateTime timestamp;
    }
    
    /**
     * Classification location
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClassificationLocation {
        private Double latitude;
        private Double longitude;
        private String address;
        private String city;
        private String country;
    }
    
    /**
     * Device information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeviceInfo {
        private String deviceId;
        private String deviceType;
        private String os;
        private String appVersion;
        private String cameraInfo;
    }
}