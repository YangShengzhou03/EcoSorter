package com.ecosorter.controller;

import com.ecosorter.dto.CollectionTaskResponse;
import com.ecosorter.service.CollectionTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection-tasks")
public class CollectionTaskController {
    
    private final CollectionTaskService collectionTaskService;
    
    public CollectionTaskController(CollectionTaskService collectionTaskService) {
        this.collectionTaskService = collectionTaskService;
    }
    
    @GetMapping("/collector/{collectorId}")
    public ResponseEntity<List<CollectionTaskResponse>> getTasksByCollector(
            @PathVariable Long collectorId) {
        List<CollectionTaskResponse> tasks = collectionTaskService.getTasks(collectorId);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/navigation/{collectorId}")
    public ResponseEntity<List<CollectionTaskResponse>> getNavigationRoute(
            @PathVariable Long collectorId) {
        List<CollectionTaskResponse> route = collectionTaskService.getNavigationRoute(collectorId);
        return ResponseEntity.ok(route);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CollectionTaskResponse>> getTasksByStatus(
            @PathVariable String status) {
        List<CollectionTaskResponse> tasks = collectionTaskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
    
    @PostMapping("/{taskId}/start")
    public ResponseEntity<CollectionTaskResponse> startTask(
            @PathVariable String taskId,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long collectorId = getUserIdFromToken(authorization);
        if (collectorId == null) {
            return ResponseEntity.status(401).build();
        }
        CollectionTaskResponse task = collectionTaskService.startTask(taskId, collectorId);
        return ResponseEntity.ok(task);
    }
    
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<CollectionTaskResponse> completeTask(
            @PathVariable String taskId,
            @RequestBody CompleteTaskRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long collectorId = getUserIdFromToken(authorization);
        if (collectorId == null) {
            return ResponseEntity.status(401).build();
        }
        java.math.BigDecimal actualWeight = request.getActualWeight() != null 
            ? java.math.BigDecimal.valueOf(request.getActualWeight()) 
            : null;
        CollectionTaskResponse task = collectionTaskService.completeTask(
            taskId, collectorId, actualWeight, request.getNotes());
        return ResponseEntity.ok(task);
    }
    
    @PostMapping("/generate")
    public ResponseEntity<Void> generateTasks() {
        collectionTaskService.generateTasksForFullTrashcans();
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
    
    public static class CompleteTaskRequest {
        private Double actualWeight;
        private String notes;

        public Double getActualWeight() {
            return actualWeight;
        }

        public void setActualWeight(Double actualWeight) {
            this.actualWeight = actualWeight;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}
