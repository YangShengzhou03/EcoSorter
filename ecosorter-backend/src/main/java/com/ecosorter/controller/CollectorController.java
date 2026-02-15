package com.ecosorter.controller;

import com.ecosorter.dto.CollectorDashboardResponse;
import com.ecosorter.dto.CollectorTaskResponse;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.service.CollectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collector")
@PreAuthorize("hasRole('COLLECTOR')")
public class CollectorController {
    
    private final CollectorService collectorService;
    
    public CollectorController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<CollectorDashboardResponse> getDashboard(
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CollectorDashboardResponse response = collectorService.getDashboard(user.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tasks")
    public ResponseEntity<List<CollectorTaskResponse>> getTasks(
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<CollectorTaskResponse> response = collectorService.getTasks(user.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<CollectorTaskResponse> getTaskDetail(
            @PathVariable String taskId,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CollectorTaskResponse response = collectorService.getTaskDetail(taskId, user.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/tasks/{taskId}/start")
    public ResponseEntity<CollectorTaskResponse> startTask(
            @PathVariable String taskId,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CollectorTaskResponse response = collectorService.startTask(taskId, user.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<CollectorTaskResponse> completeTask(
            @PathVariable String taskId,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CollectorTaskResponse response = collectorService.completeTask(taskId, user.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/tasks/{taskId}/exception")
    public ResponseEntity<Void> reportException(
            @PathVariable String taskId,
            @RequestBody java.util.Map<String, String> request,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        collectorService.reportException(taskId, user.getId(), request.get("description"));
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/devices")
    public ResponseEntity<List<DeviceListResponse>> getDevices() {
        List<DeviceListResponse> response = collectorService.getDevices();
        return ResponseEntity.ok(response);
    }
}
