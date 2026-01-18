package com.ecosorter.controller;

import com.ecosorter.dto.*;
import com.ecosorter.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
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
        UserListResponse response = adminService.updateUser(userId, request.getRole(), request.getIsActive());
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/users/{userId}/points")
    public ResponseEntity<Void> adjustUserPoints(
            @PathVariable Long userId,
            @RequestBody AdjustPointsRequest request) {
        adminService.adjustUserPoints(userId, request.getPoints(), request.getReason());
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
    
    @GetMapping("/collectors")
    public ResponseEntity<List<UserListResponse>> getCollectors() {
        List<UserListResponse> response = adminService.getCollectors();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/banners")
    public ResponseEntity<List<com.ecosorter.dto.BannerResponse>> getBanners() {
        List<com.ecosorter.dto.BannerResponse> response = adminService.getAllBanners();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/banners")
    public ResponseEntity<com.ecosorter.dto.BannerResponse> createBanner(@RequestBody com.ecosorter.dto.BannerRequest request) {
        com.ecosorter.dto.BannerResponse response = adminService.createBanner(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/banners/{id}")
    public ResponseEntity<com.ecosorter.dto.BannerResponse> updateBanner(
            @PathVariable Long id,
            @RequestBody com.ecosorter.dto.BannerRequest request) {
        com.ecosorter.dto.BannerResponse response = adminService.updateBanner(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/banners/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        adminService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<com.ecosorter.dto.WasteCategoryResponse>> getCategories() {
        List<com.ecosorter.dto.WasteCategoryResponse> response = adminService.getAllCategories();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/categories")
    public ResponseEntity<com.ecosorter.dto.WasteCategoryResponse> createCategory(@RequestBody com.ecosorter.dto.WasteCategoryRequest request) {
        com.ecosorter.dto.WasteCategoryResponse response = adminService.createCategory(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/categories/{id}")
    public ResponseEntity<com.ecosorter.dto.WasteCategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody com.ecosorter.dto.WasteCategoryRequest request) {
        com.ecosorter.dto.WasteCategoryResponse response = adminService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/logs")
    public ResponseEntity<List<LogResponse>> getLogs() {
        List<LogResponse> response = adminService.getLogs();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getReports() {
        List<ReportResponse> response = adminService.getReports();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/configuration")
    public ResponseEntity<ConfigurationResponse> getConfiguration() {
        ConfigurationResponse response = adminService.getConfiguration();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/configuration")
    public ResponseEntity<ConfigurationResponse> updateConfiguration(@RequestBody ConfigurationRequest request) {
        ConfigurationResponse response = adminService.updateConfiguration(request);
        return ResponseEntity.ok(response);
    }
}
