package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BookingType {
    RECYCLABLE("recyclable", "可回收物"),
    ELECTRONICS("electronics", "电子废弃物"),
    LARGE("large", "大件物品"),
    OTHER("other", "其他");

    @EnumValue
    private final String value;
    private final String description;

    BookingType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static BookingType fromValue(String value) {
        for (BookingType type : BookingType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown BookingType: " + value);
    }
}
