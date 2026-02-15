package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum TrashcanStatus {
    ONLINE("online", "在线"),
    OFFLINE("offline", "离线"),
    MAINTENANCE("maintenance", "维护中"),
    ERROR("error", "故障");

    @EnumValue
    private final String code;
    private final String description;

    TrashcanStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TrashcanStatus fromCode(String code) {
        for (TrashcanStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
