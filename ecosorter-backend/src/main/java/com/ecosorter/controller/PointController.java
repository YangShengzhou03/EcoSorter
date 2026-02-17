package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecosorter.dto.PointRecordResponse;
import com.ecosorter.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {
    
    private final PointService pointService;
    
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }
    
    @GetMapping("/records/page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IPage<PointRecordResponse>> getPointRecordsPage(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        IPage<PointRecordResponse> records = pointService.getUserPointRecords(user.getId(), page, size);
        return ResponseEntity.ok(records);
    }
}
