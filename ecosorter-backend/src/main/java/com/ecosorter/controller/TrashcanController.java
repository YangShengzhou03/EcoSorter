package com.ecosorter.controller;

import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.model.Classification;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/trashcan")
@PreAuthorize("hasRole('TRASHCAN')")
public class TrashcanController {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final UserRepository userRepository;
    private final ClassificationRepository classificationRepository;
    
    public TrashcanController(TrashcanDataRepository trashcanDataRepository, 
                            UserRepository userRepository,
                            ClassificationRepository classificationRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.userRepository = userRepository;
        this.classificationRepository = classificationRepository;
    }
    
    @GetMapping("/me")
    public ResponseEntity<DeviceListResponse> getTrashcanInfo(
            @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        TrashcanData trashcan = trashcanDataRepository.findByDeviceId(user.getUsername());
        if (trashcan == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(convertToDeviceListResponse(trashcan));
    }
    
    @PutMapping("/status")
    public ResponseEntity<DeviceListResponse> updateStatus(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DeviceListResponse request) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        TrashcanData trashcan = trashcanDataRepository.findByDeviceId(user.getUsername());
        if (trashcan == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (request.getCapacityLevel() != null) {
            trashcan.setCapacityLevel(request.getCapacityLevel());
        }
        
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.save(trashcan);
        
        return ResponseEntity.ok(convertToDeviceListResponse(trashcan));
    }
    
    @PostMapping("/classification")
    public ResponseEntity<Void> submitClassification(
            @AuthenticationPrincipal User user,
            @RequestBody ClassificationRequest request,
            HttpServletRequest httpRequest) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        TrashcanData trashcan = trashcanDataRepository.findByDeviceId(user.getUsername());
        if (trashcan == null) {
            return ResponseEntity.notFound().build();
        }
        
        Classification classification = new Classification();
        classification.setUserId(user.getId());
        classification.setTrashcanId(trashcan.getId());
        classification.setWasteCategoryId(request.getCategoryId());
        classification.setImageUrl(request.getImageUrl());
        classification.setConfidenceScore(request.getConfidence());
        classification.setAiSuggestion("AI识别结果");
        classification.setIpAddress(httpRequest.getRemoteAddr());
        classification.setUserAgent(httpRequest.getHeader("User-Agent"));
        classification.setCreatedAt(LocalDateTime.now());
        classification.setUpdatedAt(LocalDateTime.now());
        
        classificationRepository.save(classification);
        
        return ResponseEntity.ok().build();
    }
    
    private DeviceListResponse convertToDeviceListResponse(TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setLocation(trashcan.getLocation());
        device.setCapacityLevel(trashcan.getCapacityLevel());
        device.setMaxCapacity(trashcan.getMaxCapacity());
        device.setThreshold(trashcan.getThreshold());
        device.setStatus(trashcan.getStatus());
        device.setStatusText(getStatusText(trashcan.getStatus()));
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? trashcan.getUpdatedAt().toString() : "");
        return device;
    }
    
    private String getStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "online": return "在线";
            case "offline": return "离线";
            case "maintenance": return "维护中";
            case "error": return "故障";
            default: return "未知";
        }
    }
    
    public static class ClassificationRequest {
        private String imageUrl;
        private Long categoryId;
        private Double confidence;
        
        public String getImageUrl() {
            return imageUrl;
        }
        
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        
        public Long getCategoryId() {
            return categoryId;
        }
        
        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
        
        public Double getConfidence() {
            return confidence;
        }
        
        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
    }
}
