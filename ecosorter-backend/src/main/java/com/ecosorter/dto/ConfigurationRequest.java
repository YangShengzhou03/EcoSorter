package com.ecosorter.dto;

public class ConfigurationRequest {
    
    private String systemName;
    private Boolean maintenanceMode;
    private Double pointsRatio;
    private Integer dailyLimit;
    private Integer heartbeatInterval;
    private Integer offlineTimeout;

    public ConfigurationRequest() {
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Boolean getMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public Double getPointsRatio() {
        return pointsRatio;
    }

    public void setPointsRatio(Double pointsRatio) {
        this.pointsRatio = pointsRatio;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Integer heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public Integer getOfflineTimeout() {
        return offlineTimeout;
    }

    public void setOfflineTimeout(Integer offlineTimeout) {
        this.offlineTimeout = offlineTimeout;
    }
}
