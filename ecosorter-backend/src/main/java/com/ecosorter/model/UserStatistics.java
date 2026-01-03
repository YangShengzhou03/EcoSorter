package com.ecosorter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * User statistics information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatistics {
    
    private Integer totalClassifications = 0;
    private Integer correctClassifications = 0;
    private Double accuracy = 0.0;
    private Double totalWasteWeight = 0.0;
    private Double carbonSaved = 0.0;
}