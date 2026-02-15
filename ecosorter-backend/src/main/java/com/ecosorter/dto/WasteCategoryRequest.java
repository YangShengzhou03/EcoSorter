package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WasteCategoryRequest {
    
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 2, max = 50, message = "分类名称长度必须在2-50个字符之间")
    private String name;
    
    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;
    
    @Size(max = 200, message = "处理方法不能超过200个字符")
    private String disposalMethod;
    
    @Size(max = 20, message = "颜色不能超过20个字符")
    private String color;
    
    @Size(max = 100, message = "图标不能超过100个字符")
    private String icon;
    
    private Integer environmentalImpact;
    
    private Double recyclingRate;
    
    private String[] commonItems;
    
    @Size(max = 500, message = "处理说明不能超过500个字符")
    private String disposalInstructions;
    
    private Boolean specialHandling;
    
    private Boolean hazardous;
    
    @NotNull(message = "状态不能为空")
    private Boolean active;

    public WasteCategoryRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisposalMethod() {
        return disposalMethod;
    }

    public void setDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getEnvironmentalImpact() {
        return environmentalImpact;
    }

    public void setEnvironmentalImpact(Integer environmentalImpact) {
        this.environmentalImpact = environmentalImpact;
    }

    public Double getRecyclingRate() {
        return recyclingRate;
    }

    public void setRecyclingRate(Double recyclingRate) {
        this.recyclingRate = recyclingRate;
    }

    public String[] getCommonItems() {
        return commonItems;
    }

    public void setCommonItems(String[] commonItems) {
        this.commonItems = commonItems;
    }

    public String getDisposalInstructions() {
        return disposalInstructions;
    }

    public void setDisposalInstructions(String disposalInstructions) {
        this.disposalInstructions = disposalInstructions;
    }

    public Boolean getSpecialHandling() {
        return specialHandling;
    }

    public void setSpecialHandling(Boolean specialHandling) {
        this.specialHandling = specialHandling;
    }

    public Boolean getHazardous() {
        return hazardous;
    }

    public void setHazardous(Boolean hazardous) {
        this.hazardous = hazardous;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
