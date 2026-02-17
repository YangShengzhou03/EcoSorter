package com.ecosorter.controller;

import com.ecosorter.dto.CollectorDashboardResponse;
import com.ecosorter.dto.CollectorTaskResponse;
import com.ecosorter.dto.CreateOrderRequest;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.OrderResponse;
import com.ecosorter.dto.PointRecordResponse;
import com.ecosorter.service.CollectorService;
import jakarta.validation.Valid;
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
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<CollectorTaskResponse> response = collectorService.getTasks(user.getId(), page, pageSize);
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
    
    @GetMapping("/statistics")
    public ResponseEntity<com.ecosorter.dto.UserStatisticsResponse> getStatistics(
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        com.ecosorter.dto.UserStatisticsResponse response = collectorService.getStatistics(user.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/point-records")
    public ResponseEntity<List<PointRecordResponse>> getPointRecords(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<PointRecordResponse> response = collectorService.getPointRecords(user.getId(), type, startDate, endDate, page, pageSize);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<OrderResponse> response = collectorService.getOrders(user.getId(), status, startDate, endDate, page, pageSize);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @Valid @RequestBody CreateOrderRequest request) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        OrderResponse response = collectorService.createOrder(user.getId(), request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(
            @PathVariable Long orderId,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        OrderResponse response = collectorService.getOrderDetail(orderId, user.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        collectorService.cancelOrder(orderId, user.getId());
        return ResponseEntity.ok().build();
    }
}
