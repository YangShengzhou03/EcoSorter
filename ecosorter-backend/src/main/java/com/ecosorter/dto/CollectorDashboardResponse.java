package com.ecosorter.dto;

public class CollectorDashboardResponse {
    
    private Integer todayTasks;
    private Integer completedTasks;
    private Integer inProgressTasks;
    private Integer abnormalDevices;

    public CollectorDashboardResponse() {
    }

    public Integer getTodayTasks() {
        return todayTasks;
    }

    public void setTodayTasks(Integer todayTasks) {
        this.todayTasks = todayTasks;
    }

    public Integer getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Integer completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Integer getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(Integer inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public Integer getAbnormalDevices() {
        return abnormalDevices;
    }

    public void setAbnormalDevices(Integer abnormalDevices) {
        this.abnormalDevices = abnormalDevices;
    }
}
