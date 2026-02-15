package com.ecosorter.controller;

import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.model.Classification;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/trashcan")
public class TrashcanController {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final ClassificationRepository classificationRepository;
    
    public TrashcanController(TrashcanDataRepository trashcanDataRepository, 
                            ClassificationRepository classificationRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.classificationRepository = classificationRepository;
    }
    
    private TrashcanData authenticateTrashcan(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            return null;
        }
        authToken = authToken.substring(7);
        
        TrashcanData trashcan = trashcanDataRepository.findByAuthToken(authToken);
        if (trashcan == null) {
            return null;
        }
        
        trashcan.setLastActive(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return trashcan;
    }
    
    @GetMapping("/me")
    public ResponseEntity<DeviceListResponse> getTrashcanInfo(HttpServletRequest request) {
        TrashcanData trashcan = authenticateTrashcan(request);
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.ok(convertToDeviceListResponse(trashcan));
    }
    
    @PutMapping("/status")
    public ResponseEntity<DeviceListResponse> updateStatus(
            HttpServletRequest request,
            @Valid @RequestBody DeviceListResponse req) {
        TrashcanData trashcan = authenticateTrashcan(request);
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (req.getCapacityLevel() != null) {
            trashcan.setCapacityLevel(req.getCapacityLevel());
        }
        
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return ResponseEntity.ok(convertToDeviceListResponse(trashcan));
    }
    
    @PostMapping("/classification")
    public ResponseEntity<Void> submitClassification(
            HttpServletRequest request,
            @RequestBody ClassificationRequest req) {
        TrashcanData trashcan = authenticateTrashcan(request);
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        Classification classification = new Classification();
        classification.setUserId(null);
        classification.setTrashcanId(trashcan.getId());
        classification.setWasteCategoryId(req.getCategoryId());
        classification.setImageUrl(req.getImageUrl());
        classification.setConfidenceScore(req.getConfidence());
        classification.setAiSuggestion("AI识别结果");
        classification.setIpAddress(request.getRemoteAddr());
        classification.setUserAgent(request.getHeader("User-Agent"));
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
