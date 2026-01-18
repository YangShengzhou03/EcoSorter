package com.ecosorter.dto;

public class ComplaintProcessRequest {
    
    private String status;
    
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
