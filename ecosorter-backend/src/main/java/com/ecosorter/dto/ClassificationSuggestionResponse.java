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
@Schema(description = "Classification suggestion response")
public class ClassificationSuggestionResponse {
    
    @Schema(description = "Suggested waste category")
    private WasteCategoryResponse category;
    
    @Schema(description = "Confidence score for suggestion", example = "85.5")
    private Double confidence;
    
    @Schema(description = "Suggestion reason", example = "Based on your query 'plastic bottle'")
    private String reason;
    
    @Schema(description = "Matching keywords", example = "[\"plastic\", \"bottle\"]")
    private String[] keywords;
    
    @Schema(description = "Disposal instructions", example = "Rinse and place in recycling bin")
    private String disposalInstructions;
    
    @Schema(description = "Alternative suggestions")
    private AlternativeSuggestion[] alternatives;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlternativeSuggestion {
        @Schema(description = "Alternative category")
        private WasteCategoryResponse category;
        
        @Schema(description = "Confidence score", example = "72.3")
        private Double confidence;
        
        @Schema(description = "Reason for alternative", example = "Similar material properties")
        private String reason;
    }
}