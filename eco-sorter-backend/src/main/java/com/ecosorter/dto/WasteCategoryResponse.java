package com.ecosorter.dto;

import java.util.List;

public class WasteCategoryResponse {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private String disposalMethod;
    
    private String color;
    
    private String icon;
    
    private Integer environmentalImpact;
    
    private Double recyclingRate;
    
    private List<String> commonItems;
    
    private String disposalInstructions;
    
    private Boolean specialHandling;
    
    private Boolean hazardous;
    
    private Boolean active;
    
    private Integer points;

    public WasteCategoryResponse() {
    }

    public WasteCategoryResponse(Long id, String name, String description, String disposalMethod, String color, String icon, Integer environmentalImpact, Double recyclingRate, List<String> commonItems, String disposalInstructions, Boolean specialHandling, Boolean hazardous, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.disposalMethod = disposalMethod;
        this.color = color;
        this.icon = icon;
        this.environmentalImpact = environmentalImpact;
        this.recyclingRate = recyclingRate;
        this.commonItems = commonItems;
        this.disposalInstructions = disposalInstructions;
        this.specialHandling = specialHandling;
        this.hazardous = hazardous;
        this.active = active;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
