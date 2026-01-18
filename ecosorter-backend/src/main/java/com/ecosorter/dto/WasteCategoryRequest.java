package com.ecosorter.dto;

public class WasteCategoryRequest {
    
    private String name;
    
    private String description;
    
    private String disposalMethod;
    
    private String color;
    
    private String icon;
    
    private Integer environmentalImpact;
    
    private Double recyclingRate;
    
    private String[] commonItems;
    
    private String disposalInstructions;
    
    private Boolean specialHandling;
    
    private Boolean hazardous;
    
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
