package com.ecosorter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Waste category response")
public class WasteCategoryResponse {
    
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
    
    @Schema(description = "Environmental impact score (1-10)", example = "8")
    private Integer environmentalImpact;
    
    @Schema(description = "Recycling rate (0-100)", example = "85.0")
    private Double recyclingRate;
    
    @Schema(description = "Common items in this category", example = "[\"bottle\", \"bag\", \"container\"]")
    private String[] commonItems;
    
    @Schema(description = "Disposal instructions", example = "Rinse and place in recycling bin")
    private String disposalInstructions;
    
    @Schema(description = "Special handling required", example = "false")
    private Boolean specialHandling;
    
    @Schema(description = "Hazardous material", example = "false")
    private Boolean hazardous;
    
    @Schema(description = "Category is active", example = "true")
    private Boolean active;
}