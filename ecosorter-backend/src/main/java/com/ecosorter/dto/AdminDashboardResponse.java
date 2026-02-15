package com.ecosorter.dto;

public class AdminDashboardResponse {
    
    private Long residentCount;
    private Long totalDevices;
    private Long collectorCount;
    private Long pendingOrders;

    public AdminDashboardResponse() {
    }

    public Long getResidentCount() {
        return residentCount;
    }

    public void setResidentCount(Long residentCount) {
        this.residentCount = residentCount;
    }

    public Long getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Long totalDevices) {
        this.totalDevices = totalDevices;
    }

    public Long getCollectorCount() {
        return collectorCount;
    }

    public void setCollectorCount(Long collectorCount) {
        this.collectorCount = collectorCount;
    }

    public Long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }
}
