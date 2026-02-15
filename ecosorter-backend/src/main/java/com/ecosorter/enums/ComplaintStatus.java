package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ComplaintStatus {
    PENDING("pending", "待处理"),
    PROCESSING("processing", "处理中"),
    RESOLVED("resolved", "已解决"),
    REJECTED("rejected", "已驳回");

    @EnumValue
    private final String value;
    private final String description;

    ComplaintStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ComplaintStatus fromValue(String value) {
        for (ComplaintStatus status : ComplaintStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ComplaintStatus: " + value);
    }
}
