package com.ecosorter.dto;

public class AdminDashboardResponse {
    
    private Long totalUsers;
    private Long totalDevices;
    private Long totalCollections;
    private Long totalPoints;

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

    public Long getTotalCollections() {
        return totalCollections;
    }

    public void setTotalCollections(Long totalCollections) {
        this.totalCollections = totalCollections;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }
}
