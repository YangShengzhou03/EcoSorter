package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComplaintSubmitRequest {
    
    @NotBlank(message = "分类ID不能为空")
    private String classificationId;
    
    @NotBlank(message = "投诉类型不能为空")
    @Size(max = 50, message = "投诉类型不能超过50个字符")
    private String type;
    
    @NotBlank(message = "投诉描述不能为空")
    @Size(min = 5, max = 500, message = "投诉描述长度必须在5-500个字符之间")
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
