package com.ecosorter.dto;

import java.time.LocalDateTime;

public class PointRecordResponse {
    private Long id;
    private Integer points;
    private String type;
    private Long referenceId;
    private String description;
    private LocalDateTime createdAt;
    
    public PointRecordResponse() {}
    
    public PointRecordResponse(Long id, Integer points, String type, Long referenceId, String description, LocalDateTime createdAt) {
        this.id = id;
        this.points = points;
        this.type = type;
        this.referenceId = referenceId;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
