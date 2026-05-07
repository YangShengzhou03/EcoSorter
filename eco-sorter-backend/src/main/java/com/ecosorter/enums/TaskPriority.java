package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum TaskPriority {
    LOW("low", "低"),
    MEDIUM("medium", "中"),
    HIGH("high", "高");

    @EnumValue
    private final String value;
    private final String description;

    TaskPriority(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static TaskPriority fromValue(String value) {
        for (TaskPriority priority : TaskPriority.values()) {
            if (priority.value.equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown TaskPriority: " + value);
    }
}
