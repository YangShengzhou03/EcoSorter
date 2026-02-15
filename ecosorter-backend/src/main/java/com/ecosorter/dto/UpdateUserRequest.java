package com.ecosorter.dto;

import jakarta.validation.constraints.Pattern;

public class UpdateUserRequest {
    
    @Pattern(regexp = "^(RESIDENT|COLLECTOR|ADMIN)$", message = "角色必须是RESIDENT、COLLECTOR或ADMIN")
    private String role;
    
    private Boolean isActive;

    public UpdateUserRequest() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
