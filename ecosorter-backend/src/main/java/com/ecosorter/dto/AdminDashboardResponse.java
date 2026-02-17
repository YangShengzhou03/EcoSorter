package com.ecosorter.dto;

public class AdminDashboardResponse {
    
    private Long totalUsers;
    private Long totalDevices;
    private Long totalWeight;
    private Double accuracyRate;
    private Long recentClassifications;

    public AdminDashboardResponse() {
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Long totalDevices) {
        this.totalDevices = totalDevices;
    }

    public Long getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Long totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(Double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public Long getRecentClassifications() {
        return recentClassifications;
    }

    public void setRecentClassifications(Long recentClassifications) {
        this.recentClassifications = recentClassifications;
    }
}
