package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class WasteCategoryRequest {
    
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @Size(max = 200)
    private String disposalMethod;
    
    @Size(max = 20)
    private String color;
    
    @Size(max = 100)
    private String icon;
    
    private Integer environmentalImpact;
    
    private Double recyclingRate;
    
    private List<String> commonItems;
    
    @Size(max = 500)
    private String disposalInstructions;
    
    private Boolean specialHandling;
    
    private Boolean hazardous;
    
    @NotNull
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

    public List<String> getCommonItems() {
        return commonItems;
    }

    public void setCommonItems(List<String> commonItems) {
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
