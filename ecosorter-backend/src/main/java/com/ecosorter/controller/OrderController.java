package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecosorter.dto.CreateOrderRequest;
import com.ecosorter.dto.OrderResponse;
import com.ecosorter.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IPage<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.getUserOrders(user.getId(), page, pageSize, status));
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IPage<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(orderService.getAllOrders(page, pageSize, status));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @PathVariable Long id) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.getOrderById(id, user.getId()));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @Valid @RequestBody CreateOrderRequest request) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.createOrderFromRequest(user.getId(), request));
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
    
    @PutMapping("/{id}/tracking-number")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateTrackingNumber(
            @PathVariable Long id,
            @RequestParam String trackingNumber) {
        return ResponseEntity.ok(orderService.updateTrackingNumber(id, trackingNumber));
    }
}
