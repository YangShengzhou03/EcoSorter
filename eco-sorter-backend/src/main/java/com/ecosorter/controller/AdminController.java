package com.ecosorter.controller;

import com.ecosorter.dto.*;
import com.ecosorter.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final AdminService adminService;
    
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        AdminDashboardResponse response = adminService.getDashboard();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/device-status")
    public ResponseEntity<DeviceStatusResponse> getDeviceStatus() {
        DeviceStatusResponse response = adminService.getDeviceStatus();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/activities")
    public ResponseEntity<List<ActivityResponse>> getActivities() {
        List<ActivityResponse> response = adminService.getActivities();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<UserListResponse>> getUsers() {
        List<UserListResponse> response = adminService.getUsers();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/users")
    public ResponseEntity<UserListResponse> createUser(@RequestBody RegisterRequest request) {
        UserListResponse response = adminService.createUser(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserListResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request) {
        UserListResponse response = adminService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/users/{userId}/points")
    public ResponseEntity<Void> adjustUserPoints(
            @PathVariable Long userId,
            @RequestBody AdjustPointsRequest request) {
        adminService.adjustUserPoints(userId, request.getPoints().intValue(), request.getReason());
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/devices")
    public ResponseEntity<List<DeviceListResponse>> getDevices() {
        List<DeviceListResponse> response = adminService.getDevices();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/devices")
    public ResponseEntity<DeviceListResponse> createDevice(@RequestBody DeviceListResponse request) {
        DeviceListResponse response = adminService.createDevice(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceListResponse> updateDevice(
            @PathVariable Long deviceId,
            @RequestBody DeviceListResponse request) {
        DeviceListResponse response = adminService.updateDevice(deviceId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long deviceId) {
        adminService.deleteDevice(deviceId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/devices/{deviceId}/reset-admin-password")
    public ResponseEntity<Void> resetAdminPassword(@PathVariable Long deviceId) {
        adminService.resetAdminPassword(deviceId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getReports() {
        List<ReportResponse> response = adminService.getReports();
        return ResponseEntity.ok(response);
    }
}
