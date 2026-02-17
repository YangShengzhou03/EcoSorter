package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecosorter.dto.ComplaintProcessRequest;
import com.ecosorter.dto.ComplaintResponse;
import com.ecosorter.dto.ComplaintSubmitRequest;
import com.ecosorter.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComplaintResponse> submitComplaint(
            @Valid @RequestBody ComplaintSubmitRequest request,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        ComplaintResponse response = complaintService.submitComplaint(user.getId(), request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<ComplaintResponse> response = complaintService.getUserComplaints(user.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IPage<ComplaintResponse>> getAllComplaints(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status) {
        IPage<ComplaintResponse> response = complaintService.getAllComplaints(status, page, pageSize);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponse> processComplaint(
            @PathVariable Long id,
            @Valid @RequestBody ComplaintProcessRequest request,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        ComplaintResponse response = complaintService.processComplaint(id, user.getId(), request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComplaint(
            @PathVariable Long id,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        complaintService.deleteComplaint(id, user.getId());
        return ResponseEntity.ok().build();
    }
}
