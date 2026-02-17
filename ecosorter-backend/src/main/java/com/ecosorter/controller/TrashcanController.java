package com.ecosorter.controller;

import com.ecosorter.dto.ClassificationResponse;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.WasteCategoryResponse;
import com.ecosorter.model.Classification;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import com.ecosorter.service.ClassificationService;
import com.ecosorter.service.PointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/trashcan")
public class TrashcanController {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final ClassificationRepository classificationRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    private final UserRepository userRepository;
    private final PointService pointService;
    private final ClassificationService classificationService;
    
    public TrashcanController(TrashcanDataRepository trashcanDataRepository,
                             ClassificationRepository classificationRepository,
                             WasteCategoryRepository wasteCategoryRepository,
                             UserRepository userRepository,
                             PointService pointService,
                             ClassificationService classificationService) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.classificationRepository = classificationRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.userRepository = userRepository;
        this.pointService = pointService;
        this.classificationService = classificationService;
    }
    
    @GetMapping("/me")
    public ResponseEntity<DeviceListResponse> getTrashcanInfo(
            @AuthenticationPrincipal TrashcanData trashcan) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        DeviceListResponse response = convertToDeviceListResponse(trashcan);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/me")
    public ResponseEntity<DeviceListResponse> updateTrashcanInfo(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, Object> updates) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (updates.containsKey("deviceId")) {
            trashcan.setDeviceId((String) updates.get("deviceId"));
        }
        if (updates.containsKey("location")) {
            trashcan.setLocation((String) updates.get("location"));
        }
        if (updates.containsKey("binType")) {
            trashcan.setBinType((String) updates.get("binType"));
        }
        if (updates.containsKey("maxCapacity")) {
            trashcan.setMaxCapacity(((Number) updates.get("maxCapacity")).intValue());
        }
        if (updates.containsKey("threshold")) {
            trashcan.setThreshold(((Number) updates.get("threshold")).intValue());
        }
        
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return ResponseEntity.ok(convertToDeviceListResponse(trashcan));
    }
    
    @PutMapping("/status")
    public ResponseEntity<DeviceListResponse> updateTrashcanStatus(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, Object> statusData) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (statusData.containsKey("capacityLevel")) {
            trashcan.setCapacityLevel(((Number) statusData.get("capacityLevel")).intValue());
        }
        
        trashcan.setLastActive(LocalDateTime.now());
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return ResponseEntity.ok(convertToDeviceListResponse(trashcan));
    }
    
    @PostMapping("/classification")
    public ResponseEntity<Void> submitClassification(
            @AuthenticationPrincipal TrashcanData trashcan,
            @Valid @RequestBody Map<String, Object> classificationData) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        String imageUrl = (String) classificationData.get("imageUrl");
        Long categoryId = ((Number) classificationData.get("categoryId")).longValue();
        Double confidence = ((Number) classificationData.get("confidence")).doubleValue();
        Long userId = classificationData.containsKey("userId") ? 
            ((Number) classificationData.get("userId")).longValue() : null;
        
        Classification classification = new Classification();
        classification.setUserId(userId);
        classification.setTrashcanId(trashcan.getId());
        classification.setWasteCategoryId(categoryId);
        classification.setImageUrl(imageUrl);
        classification.setConfidenceScore(confidence);
        classification.setCreatedAt(LocalDateTime.now());
        classification.setUpdatedAt(LocalDateTime.now());
        
        classificationRepository.insert(classification);
        
        if (userId != null) {
            WasteCategory category = wasteCategoryRepository.selectById(categoryId);
            Integer basePoints = (category != null && category.getPoints() != null) ? 
                category.getPoints() : 10;
            Double confidenceBonus = confidence * 10;
            Integer calculatedPoints = basePoints + confidenceBonus.intValue();
            
            pointService.addPoints(userId, calculatedPoints, "classification", 
                classification.getId(), "垃圾分类: " + 
                (category != null ? category.getName() : "未知"));
        }
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(
            @AuthenticationPrincipal TrashcanData trashcan) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        trashcan.setLastActive(LocalDateTime.now());
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin-login")
    public ResponseEntity<Map<String, Object>> adminLogin(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, String> loginData) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        String password = loginData.get("password");
        
        if (trashcan.getAdminPassword() == null || 
            !trashcan.getAdminPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "管理员密码错误"
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "登录成功",
            "deviceId", trashcan.getDeviceId()
        ));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetAdminPassword(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, String> passwordData) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        String newPassword = passwordData.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        trashcan.setAdminPassword(newPassword);
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/clear-data")
    public ResponseEntity<Void> clearDeviceData(
            @AuthenticationPrincipal TrashcanData trashcan) {
        if (trashcan == null) {
            return ResponseEntity.status(401).build();
        }
        
        classificationRepository.deleteByTrashcanId(trashcan.getId());
        
        trashcan.setCapacityLevel(0);
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return ResponseEntity.ok().build();
    }
    
    private DeviceListResponse convertToDeviceListResponse(TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setDeviceName(trashcan.getDeviceName());
        device.setLocation(trashcan.getLocation());
        device.setBinType(trashcan.getBinType());
        device.setCapacityLevel(trashcan.getCapacityLevel() != null ? 
            trashcan.getCapacityLevel().intValue() : 0);
        device.setMaxCapacity(trashcan.getMaxCapacity() != null ? 
            trashcan.getMaxCapacity().intValue() : 0);
        device.setThreshold(trashcan.getThreshold() != null ? 
            trashcan.getThreshold().intValue() : 0);
        device.setStatus(trashcan.getStatus());
        device.setStatusText(getStatusText(trashcan.getStatus()));
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? 
            trashcan.getUpdatedAt().toString() : "");
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
}
