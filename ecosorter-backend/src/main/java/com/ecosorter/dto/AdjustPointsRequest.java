package com.ecosorter.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdjustPointsRequest {
    
    @NotNull(message = "积分不能为空")
    private Long points;
    
    @NotNull(message = "调整原因不能为空")
    @Size(min = 5, max = 200, message = "调整原因长度在5-200个字符之间")
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
