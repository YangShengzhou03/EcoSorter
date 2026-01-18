package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class LoginRequest {
    
    @NotBlank(message = "Username or email is required")
    private String identifier; // Can be username or email
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String twoFactorCode;
    private Boolean rememberMe = false;

    public LoginRequest() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTwoFactorCode() {
        return twoFactorCode;
    }

    public void setTwoFactorCode(String twoFactorCode) {
        this.twoFactorCode = twoFactorCode;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
