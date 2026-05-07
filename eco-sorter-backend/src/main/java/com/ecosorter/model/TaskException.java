package com.ecosorter.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ecosorter.enums.TaskStatus;

import java.time.LocalDateTime;

@TableName("task_exceptions")
public class TaskException {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long taskId;
    
    private Long reporterId;
    
    private ExceptionType exceptionType;
    
    private String description;
    
    private TaskStatus status = TaskStatus.PENDING;
    
    private Long reviewerId;
    
    private String reviewNotes;
    
    private LocalDateTime reviewedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    public enum ExceptionType {
        DEVICE_FAILURE("device_failure", "设备故障"),
        ROAD_BLOCKED("road_blocked", "道路受阻"),
        ACCESS_DENIED("access_denied", "无法进入"),
        OTHER("other", "其他");

        @EnumValue
        private final String value;
        private final String description;

        ExceptionType(String value, String description) {
            this.value = value;
            this.description = description;
        }

        public String getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        public static ExceptionType fromValue(String value) {
            for (ExceptionType type : ExceptionType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown ExceptionType: " + value);
        }
    }

    public TaskException() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
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
