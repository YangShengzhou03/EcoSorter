package com.ecosorter.model;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * User profile information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;
    
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;
    
    private String avatar;
    
    private String phone;
    
    @Size(max = 100, message = "Department must be less than 100 characters")
    private String department;
    
    @Size(max = 100, message = "Position must be less than 100 characters")
    private String position;
}