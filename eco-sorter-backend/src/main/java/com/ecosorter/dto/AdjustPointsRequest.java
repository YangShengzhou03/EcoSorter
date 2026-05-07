package com.ecosorter.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdjustPointsRequest {
    
    @NotNull
    private Long points;
    
    @NotNull
    @Size(min = 5, max = 200)
    private String reason;

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
