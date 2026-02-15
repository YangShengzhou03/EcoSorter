package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BookingStatus {
    PENDING("pending", "待确认"),
    CONFIRMED("confirmed", "已确认"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    @EnumValue
    private final String value;
    private final String description;

    BookingStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static BookingStatus fromValue(String value) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown BookingStatus: " + value);
    }
}
