package com.ecosorter.controller;

import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.TrashcanFaultRequest;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.service.TrashcanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/trashcan")
public class TrashcanController {
    
    private final TrashcanService trashcanService;
    
    public TrashcanController(TrashcanService trashcanService) {
        this.trashcanService = trashcanService;
    }
    
    @GetMapping("/me")
    public ResponseEntity<DeviceListResponse> getTrashcanInfo(
            @AuthenticationPrincipal TrashcanData trashcan) {
        DeviceListResponse response = trashcanService.getDeviceInfo(trashcan);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/me")
    public ResponseEntity<DeviceListResponse> updateTrashcanInfo(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, Object> updates) {
        DeviceListResponse response = trashcanService.updateDeviceInfo(trashcan, updates);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/status")
    public ResponseEntity<DeviceListResponse> updateTrashcanStatus(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, Object> statusData) {
        DeviceListResponse response = trashcanService.updateStatus(trashcan, statusData);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/classification")
    public ResponseEntity<Void> submitClassification(
            @AuthenticationPrincipal TrashcanData trashcan,
            @Valid @RequestBody Map<String, Object> classificationData) {
        trashcanService.submitClassification(trashcan, classificationData);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(
            @AuthenticationPrincipal TrashcanData trashcan) {
        trashcanService.heartbeat(trashcan);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin-login")
    public ResponseEntity<Map<String, Object>> adminLogin(
            @AuthenticationPrincipal TrashcanData trashcan,
            @RequestBody Map<String, String> loginData) {
        
        String password = loginData.get("password");
        
        if (!trashcanService.verifyAdminPassword(trashcan, password)) {
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
        
        String newPassword = passwordData.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        trashcanService.resetAdminPassword(trashcan, newPassword);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/clear-data")
    public ResponseEntity<Void> clearDeviceData(
            @AuthenticationPrincipal TrashcanData trashcan) {
        trashcanService.clearDeviceData(trashcan);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/fault")
    public ResponseEntity<Void> reportFault(
            @AuthenticationPrincipal TrashcanData trashcan,
            @Valid @RequestBody TrashcanFaultRequest faultRequest) {
        trashcanService.reportFault(trashcan, faultRequest);
        return ResponseEntity.ok().build();
    }
}
