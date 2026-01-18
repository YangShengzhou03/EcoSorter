package com.ecosorter.dto;

public class UpdateUserRequest {
    
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
