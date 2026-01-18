package com.ecosorter.controller;

import com.ecosorter.dto.ComplaintProcessRequest;
import com.ecosorter.dto.ComplaintResponse;
import com.ecosorter.dto.ComplaintSubmitRequest;
import com.ecosorter.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
    
    private final ComplaintService complaintService;
    
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }
    
    @PostMapping
    public ResponseEntity<ComplaintResponse> submitComplaint(
            @RequestBody ComplaintSubmitRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        ComplaintResponse response = complaintService.submitComplaint(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/my")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<ComplaintResponse> response = complaintService.getUserComplaints(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin")
    public ResponseEntity<Page<ComplaintResponse>> getAllComplaints(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<ComplaintResponse> response = complaintService.getAllComplaints(status, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin/pending-count")
    public ResponseEntity<Long> getPendingCount(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        long count = complaintService.getPendingCount();
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/admin/{id}")
    public ResponseEntity<ComplaintResponse> processComplaint(
            @PathVariable Long id,
            @RequestBody ComplaintProcessRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long adminId = getUserIdFromToken(authorization);
        if (adminId == null) {
            return ResponseEntity.status(401).build();
        }
        ComplaintResponse response = complaintService.processComplaint(id, adminId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        complaintService.deleteComplaint(id, userId);
        return ResponseEntity.ok().build();
    }
    
    private Long getUserIdFromToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String userIdStr = token.replace("simple-token-", "");
            try {
                return Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
