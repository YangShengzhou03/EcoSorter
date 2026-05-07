package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComplaintSubmitRequest {
    
    @NotBlank
    private String classificationId;
    
    @NotBlank
    @Size(max = 50)
    private String type;
    
    @NotBlank
    @Size(min = 5, max = 500)
    private String description;

    public ComplaintSubmitRequest() {
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
