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
import java.util.Map;

/**
 * Waste category entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "waste_categories")
public class WasteCategory {
    
    private String id;
    
    @NotBlank(message = "Category name is required")
    @Indexed(unique = true)
    @Field("name")
    private String name;
    
    @NotBlank(message = "Category code is required")
    @Indexed(unique = true)
    @Field("code")
    private String code;
    
    @NotBlank(message = "Category description is required")
    @Field("description")
    private String description;
    
    @NotNull(message = "Category type is required")
    @Field("type")
    private WasteType type;
    
    @Field("color")
    private String color;
    
    @Field("icon")
    private String icon;
    
    @Field("image")
    private String image;
    
    @Field("examples")
    private List<String> examples;
    
    @Field("disposal_methods")
    private List<String> disposalMethods;
    
    @Field("environmental_impact")
    private String environmentalImpact;
    
    @Field("recycling_info")
    private RecyclingInfo recyclingInfo;
    
    @Field("regulations")
    private List<Regulation> regulations;
    
    @Field("statistics")
    private CategoryStatistics statistics;
    
    @Field("metadata")
    private Map<String, Object> metadata;
    
    @Field("parent_category")
    private String parentCategory;
    
    @Field("sub_categories")
    private List<String> subCategories;
    
    @Field("is_active")
    private Boolean isActive = true;
    
    @Field("priority")
    private Integer priority = 0;
    
    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Waste type enum
     */
    public enum WasteType {
        RECYCLABLE, HAZARDOUS, WET, DRY, ELECTRONIC, CONSTRUCTION, MEDICAL
    }
    
    /**
     * Recycling information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecyclingInfo {
        private Boolean recyclable;
        private String recyclingProcess;
        private List<String> recyclingCenters;
        private Double carbonFootprintReduction;
        private String recycledProducts;
    }
    
    /**
     * Regulation information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Regulation {
        private String title;
        private String description;
        private String jurisdiction;
        private LocalDateTime effectiveDate;
        private List<String> penalties;
    }
    
    /**
     * Category statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryStatistics {
        private Integer totalClassifications = 0;
        private Double accuracy = 0.0;
        private Double averageConfidence = 0.0;
        private Double totalWeight = 0.0;
        private Double carbonSaved = 0.0;
        private Integer userReports = 0;
    }
}