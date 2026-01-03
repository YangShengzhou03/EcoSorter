package com.ecosorter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * User notification preferences
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotifications {
    
    private Boolean email = true;
    private Boolean push = true;
    private Boolean classification = true;
}