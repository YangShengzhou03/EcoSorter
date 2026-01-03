package com.ecosorter.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a resource is not found
 */
public class ResourceNotFoundException extends AppException {
    
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue), 
              HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
}