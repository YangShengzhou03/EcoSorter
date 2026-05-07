package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeviceActivateRequest {
    
    @NotBlank
    @Size(min = 2, max = 20)
    private String deviceName;
    
    @NotBlank
    @Size(max = 200)
    private String location;
    
    @NotBlank
    private String binType;
    
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
