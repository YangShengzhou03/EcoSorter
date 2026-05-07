package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskExceptionRequest {
    
    @NotBlank
    private String taskId;
    
    @NotBlank
    @Size(max = 50)
    private String exceptionType;
    
    @NotBlank
    @Size(min = 5, max = 500)
    private String description;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
