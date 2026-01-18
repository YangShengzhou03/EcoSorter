package com.ecosorter.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "collection_tasks")
public class CollectionTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "task_id", unique = true, nullable = false, length = 50)
    private String taskId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trashcan_id", nullable = false)
    private TrashcanData trashcan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id")
    private User collector;
    
    @Column(nullable = false, length = 20)
    private String status = "pending";
    
    @Column(nullable = false, length = 20)
    private String priority = "medium";
    
    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedWeight;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal actualWeight;
    
    @Column(length = 50)
    private String garbageType = "混合垃圾";
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CollectionTask() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TrashcanData getTrashcan() {
        return trashcan;
    }

    public void setTrashcan(TrashcanData trashcan) {
        this.trashcan = trashcan;
    }

    public User getCollector() {
        return collector;
    }

    public void setCollector(User collector) {
        this.collector = collector;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public BigDecimal getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(BigDecimal estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getGarbageType() {
        return garbageType;
    }

    public void setGarbageType(String garbageType) {
        this.garbageType = garbageType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
