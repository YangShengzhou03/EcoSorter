package com.ecosorter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Classification request for waste identification")
public class ClassificationRequest {
    
    @Schema(description = "Image file name or path", example = "waste_image_123.jpg")
    @NotBlank(message = "Image file is required")
    private String imageFile;
    
    @Schema(description = "Image data in base64 format", example = "data:image/jpeg;base64,/9j/4AAQSkZJRg...")
    @NotBlank(message = "Image data is required")
    @Size(max = 10485760, message = "Image data must not exceed 10MB")
    private String imageData;
    
    @Schema(description = "Image format", example = "jpeg")
    @NotBlank(message = "Image format is required")
    private String imageFormat;
    
    @Schema(description = "Additional context or description", example = "Plastic bottle found in kitchen")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @Schema(description = "Location information (optional)", example = "Beijing, China")
    private String location;
    
    @Schema(description = "Device information (optional)", example = "Desktop App v1.0.0")
    private String deviceInfo;
    
    @Schema(description = "User confidence level (1-10)", example = "8")
    private Integer userConfidence;
    
    @Schema(description = "User's guess for waste category (optional)", example = "plastic")
    private String userGuess;
    
    @Schema(description = "Additional tags or keywords", example = "[\"bottle\", \"plastic\", \"recyclable\"]")
    private List<String> tags;
}