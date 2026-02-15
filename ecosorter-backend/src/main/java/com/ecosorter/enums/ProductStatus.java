package com.ecosorter.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ProductStatus {
    AVAILABLE("available", "上架"),
    UNAVAILABLE("unavailable", "下架");

    @EnumValue
    private final String value;
    private final String description;

    ProductStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ProductStatus fromValue(String value) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ProductStatus: " + value);
    }
}
