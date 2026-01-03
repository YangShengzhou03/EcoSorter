package com.ecosorter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * User preferences configuration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {
    
    private Theme theme = Theme.LIGHT;
    private String language = "zh-CN";
    private UserNotifications notifications;
    
    public enum Theme {
        LIGHT, DARK
    }
}