package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ComplaintType {
    MISCLASSIFICATION("misclassification", "分类错误"),
    WEIGHT("weight", "重量争议"),
    POINTS("points", "积分争议"),
    OTHER("other", "其他");

    @EnumValue
    private final String value;
    private final String description;

    ComplaintType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ComplaintType fromValue(String value) {
        for (ComplaintType type : ComplaintType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ComplaintType: " + value);
    }
}
