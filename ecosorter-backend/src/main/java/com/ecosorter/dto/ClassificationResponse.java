package com.ecosorter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Classification response with waste identification results")
public class ClassificationResponse {
    
    @Schema(description = "Classification ID", example = "cls_1234567890")
    private String id;
    
    @Schema(description = "User ID who performed the classification", example = "user_1234567890")
    private String userId;
    
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
    
    @Schema(description = "Waste category information")
    private WasteCategoryResponse category;
    
    @Schema(description = "Classification confidence score (0-100)", example = "95.5")
    private Double confidence;
    
    @Schema(description = "Classification result", example = "plastic_bottle")
    private String result;
    
    @Schema(description = "Classification status", example = "completed")
    private String status;
    
    @Schema(description = "Image file path", example = "uploads/classifications/cls_1234567890.jpg")
    private String imageUrl;
    
    @Schema(description = "Processing time in milliseconds", example = "1250")
    private Long processingTime;
    
    @Schema(description = "AI model used for classification", example = "eco-sorter-v1.0")
    private String model;
    
    @Schema(description = "Classification timestamp", example = "2023-12-29T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Additional notes or warnings", example = "Clean before recycling")
    private String notes;
    
    @Schema(description = "Disposal instructions", example = "Rinse and place in recycling bin")
    private String disposalInstructions;
    
    @Schema(description = "Environmental impact score (1-10)", example = "8")
    private Integer environmentalImpact;
    
    @Schema(description = "Alternative suggestions", example = "[\"glass_bottle\", \"aluminum_can\"]")
    private List<String> alternatives;
    
    @Schema(description = "User feedback on classification", example = "accurate")
    private String userFeedback;
    
    @Schema(description = "Classification accuracy metrics")
    private ClassificationMetrics metrics;
    
    @Schema(description = "Processing details")
    private ProcessingDetails processingDetails;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WasteCategoryResponse {
        @Schema(description = "Category ID", example = "cat_plastic")
        private String id;
        
        @Schema(description = "Category name", example = "Plastic")
        private String name;
        
        @Schema(description = "Category description", example = "Plastic waste materials")
        private String description;
        
        @Schema(description = "Disposal method", example = "recycling")
        private String disposalMethod;
        
        @Schema(description = "Color code for category", example = "#2196F3")
        private String color;
        
        @Schema(description = "Icon for category", example = "🗑️")
        private String icon;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassificationMetrics {
        @Schema(description = "Accuracy score (0-100)", example = "95.5")
        private Double accuracy;
        
        @Schema(description = "Precision score (0-100)", example = "92.3")
        private Double precision;
        
        @Schema(description = "Recall score (0-100)", example = "89.7")
        private Double recall;
        
        @Schema(description = "F1 score (0-100)", example = "90.9")
        private Double f1Score;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingDetails {
        @Schema(description = "Image preprocessing time (ms)", example = "250")
        private Long preprocessingTime;
        
        @Schema(description = "Model inference time (ms)", example = "800")
        private Long inferenceTime;
        
        @Schema(description = "Post-processing time (ms)", example = "200")
        private Long postprocessingTime;
        
        @Schema(description = "Total processing stages", example = "3")
        private Integer stages;
    }
}