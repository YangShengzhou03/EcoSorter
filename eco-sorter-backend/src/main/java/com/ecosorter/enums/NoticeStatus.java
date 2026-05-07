package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum NoticeStatus {
    DRAFT("draft", "草稿"),
    PUBLISHED("published", "已发布");

    @EnumValue
    private final String value;
    private final String description;

    NoticeStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static NoticeStatus fromValue(String value) {
        for (NoticeStatus status : NoticeStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown NoticeStatus: " + value);
    }
}
