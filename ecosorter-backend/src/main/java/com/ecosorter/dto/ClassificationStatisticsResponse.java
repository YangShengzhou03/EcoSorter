package com.ecosorter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Classification statistics response")
public class ClassificationStatisticsResponse {
    
    @Schema(description = "Total number of classifications", example = "150")
    private Long totalClassifications;
    
    @Schema(description = "Number of classifications in current period", example = "25")
    private Long currentPeriodClassifications;
    
    @Schema(description = "Average confidence score", example = "87.5")
    private Double averageConfidence;
    
    @Schema(description = "Most common waste category")
    private WasteCategoryResponse mostCommonCategory;
    
    @Schema(description = "Classification accuracy rate", example = "92.3")
    private Double accuracyRate;
    
    @Schema(description = "Daily statistics")
    private Map<String, DailyStats> dailyStatistics;
    
    @Schema(description = "Category breakdown")
    private Map<String, CategoryStats> categoryBreakdown;
    
    @Schema(description = "Environmental impact score", example = "8.5")
    private Double environmentalImpact;
    
    @Schema(description = "Recycling rate", example = "75.2")
    private Double recyclingRate;
    
    @Schema(description = "Period start date")
    private LocalDateTime periodStart;
    
    @Schema(description = "Period end date")
    private LocalDateTime periodEnd;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStats {
        @Schema(description = "Number of classifications", example = "5")
        private Long count;
        
        @Schema(description = "Average confidence", example = "89.2")
        private Double averageConfidence;
        
        @Schema(description = "Most common category", example = "plastic")
        private String mostCommonCategory;
        
        @Schema(description = "Recycling rate for the day", example = "80.0")
        private Double recyclingRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStats {
        @Schema(description = "Number of items in this category", example = "45")
        private Long count;
        
        @Schema(description = "Percentage of total", example = "30.0")
        private Double percentage;
        
        @Schema(description = "Average confidence for this category", example = "91.5")
        private Double averageConfidence;
        
        @Schema(description = "Disposal method", example = "recycling")
        private String disposalMethod;
        
        @Schema(description = "Environmental impact score", example = "9.0")
        private Double environmentalImpact;
    }
}