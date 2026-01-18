package com.ecosorter.dto;

public class BinStatusResponse {
    
    private String binId;
    
    private String location;
    
    private Integer fillLevel;
    
    private String status;

    public BinStatusResponse() {
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getFillLevel() {
        return fillLevel;
    }

    public void setFillLevel(Integer fillLevel) {
        this.fillLevel = fillLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
