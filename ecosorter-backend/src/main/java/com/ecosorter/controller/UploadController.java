package com.ecosorter.controller;

import com.ecosorter.dto.UploadResponse;
import com.ecosorter.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    @Autowired
    private UploadService uploadService;
    
    @PostMapping("/avatar")
    public ResponseEntity<UploadResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        String fileUrl = uploadService.uploadAvatar(file);
        UploadResponse response = new UploadResponse();
        response.setUrl(fileUrl);
        response.setFilename(file.getOriginalFilename());
        response.setSize(file.getSize());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/image")
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        String fileUrl = uploadService.uploadImage(file);
        UploadResponse response = new UploadResponse();
        response.setUrl(fileUrl);
        response.setFilename(file.getOriginalFilename());
        response.setSize(file.getSize());
        
        return ResponseEntity.ok(response);
    }
}
