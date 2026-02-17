package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeviceActivateRequest {
    
    @NotBlank(message = "设备名称不能为空")
    @Size(min = 2, max = 20, message = "设备名称长度在2到20个字符")
    private String deviceName;
    
    @NotBlank(message = "设备位置不能为空")
    private String location;
    
    @NotBlank(message = "垃圾桶类型不能为空")
    private String binType;
    
    @NotBlank(message = "管理员密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6到20个字符")
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
