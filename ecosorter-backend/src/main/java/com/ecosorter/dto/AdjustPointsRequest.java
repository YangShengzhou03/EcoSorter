package com.ecosorter.dto;

public class AdjustPointsRequest {
    
    private Integer points;
    
    private String reason;

    public AdjustPointsRequest() {
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
