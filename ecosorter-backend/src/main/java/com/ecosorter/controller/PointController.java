package com.ecosorter.controller;

import com.ecosorter.dto.PointRecordResponse;
import com.ecosorter.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {
    
    @Autowired
    private PointService pointService;
    
    @GetMapping("/records")
    public ResponseEntity<List<PointRecordResponse>> getPointRecords(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<PointRecordResponse> records = pointService.getUserPointRecords(userId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/records/page")
    public ResponseEntity<Page<PointRecordResponse>> getPointRecordsPage(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Page<PointRecordResponse> records = pointService.getUserPointRecords(userId, page, size);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalPoints(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Integer totalPoints = pointService.getUserTotalPoints(userId);
        return ResponseEntity.ok(totalPoints);
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
