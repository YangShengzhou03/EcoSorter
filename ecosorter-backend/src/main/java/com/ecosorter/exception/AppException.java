package com.ecosorter.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for application-specific exceptions
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}