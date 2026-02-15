package com.ecosorter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComplaintProcessRequest {
    
    @NotBlank(message = "状态不能为空")
    private String status;
    
    @Size(max = 500, message = "回复内容不能超过500个字符")
    private String adminResponse;

    public ComplaintProcessRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }
}
