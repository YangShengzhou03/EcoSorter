package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum TaskStatus {
    PENDING("pending", "待处理"),
    IN_PROGRESS("in_progress", "进行中"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消"),
    PROCESSING("processing", "处理中"),
    RESOLVED("resolved", "已解决"),
    REJECTED("rejected", "已驳回");

    @EnumValue
    private final String value;
    private final String description;

    TaskStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown TaskStatus: " + value);
    }
}
