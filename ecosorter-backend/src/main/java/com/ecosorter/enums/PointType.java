package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PointType {
    CLASSIFICATION("classification", "分类获得"),
    ORDER("order", "订单消耗"),
    ADMIN("admin", "管理员调整");

    @EnumValue
    private final String value;
    private final String description;

    PointType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static PointType fromValue(String value) {
        for (PointType type : PointType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PointType: " + value);
    }
}
