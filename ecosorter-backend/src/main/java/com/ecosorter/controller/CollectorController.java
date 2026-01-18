package com.ecosorter.controller;

import com.ecosorter.dto.CollectorDashboardResponse;
import com.ecosorter.dto.CollectorTaskResponse;
import com.ecosorter.dto.CollectionRecordResponse;
import com.ecosorter.service.CollectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collector")
public class CollectorController {
    
    private final CollectorService collectorService;
    
    public CollectorController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<CollectorDashboardResponse> getDashboard(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        CollectorDashboardResponse response = collectorService.getDashboard(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tasks")
    public ResponseEntity<List<CollectorTaskResponse>> getTasks(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<CollectorTaskResponse> response = collectorService.getTasks(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/collection-records")
    public ResponseEntity<List<CollectionRecordResponse>> getCollectionRecords(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<CollectionRecordResponse> response = collectorService.getCollectionRecords(userId);
        return ResponseEntity.ok(response);
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
