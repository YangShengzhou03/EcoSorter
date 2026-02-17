package com.ecosorter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@TableName("waste_categories")
public class WasteCategory {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotBlank(message = "Category name is required")
    private String name;
    
    private String nameEn;
    
    private String description;
    
    private String color;
    
    private String icon;
    
    private String disposalInstructions;
    
    private String disposalMethod;
    
    private String environmentalImpact;
    
    private Double recyclingRate;
    
    private Integer points;
    
    private String commonItems;
    
    private Boolean specialHandling = false;
    
    private Boolean hazardous = false;
    
    private Boolean isActive = true;
    
    private Integer sortOrder = 0;
        
    private LocalDateTime createdAt;
        
    private LocalDateTime updatedAt;

    public WasteCategory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDisposalInstructions() {
        return disposalInstructions;
    }

    public void setDisposalInstructions(String disposalInstructions) {
        this.disposalInstructions = disposalInstructions;
    }

    public String getDisposalMethod() {
        return disposalMethod;
    }

    public void setDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public String getEnvironmentalImpact() {
        return environmentalImpact;
    }

    public void setEnvironmentalImpact(String environmentalImpact) {
        this.environmentalImpact = environmentalImpact;
    }

    public Double getRecyclingRate() {
        return recyclingRate;
    }

    public void setRecyclingRate(Double recyclingRate) {
        this.recyclingRate = recyclingRate;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getCommonItems() {
        return commonItems;
    }

    public void setCommonItems(String commonItems) {
        this.commonItems = commonItems;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
