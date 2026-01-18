package com.ecosorter.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    
    private HttpStatus status;
    private String code;
    
    public AppException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = "INTERNAL_ERROR";
    }
    
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = status.name();
    }
    
    public AppException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}