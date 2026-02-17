package com.ecosorter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    
    @Size(min = 3, max = 20, message = "用户名长度在3到20个字符")
    private String username;
    
    @Email(message = "请输入正确的邮箱格式")
    private String email;
    
    @Pattern(regexp = "^(RESIDENT|COLLECTOR|ADMIN)$", message = "角色必须是RESIDENT、COLLECTOR或ADMIN")
    private String role;
    
    private Boolean isActive;

    public UpdateUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
