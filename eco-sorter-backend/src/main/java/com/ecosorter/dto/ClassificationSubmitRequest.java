package com.ecosorter.dto;

import jakarta.validation.constraints.NotNull;

public class ClassificationSubmitRequest {
    
    @NotNull(message = "垃圾类别ID不能为空")
    private Long categoryId;
    
    private Long trashcanId;
    
    private Double confidence;
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getTrashcanId() {
        return trashcanId;
    }
    
    public void setTrashcanId(Long trashcanId) {
        this.trashcanId = trashcanId;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
