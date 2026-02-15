package com.ecosorter.controller;

import com.ecosorter.config.JwtUtil;
import com.ecosorter.dto.CollectionTaskResponse;
import com.ecosorter.dto.TaskExceptionRequest;
import com.ecosorter.dto.TaskExceptionResponse;
import com.ecosorter.service.CollectionTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection-tasks")
public class CollectionTaskController {
    
    private final CollectionTaskService collectionTaskService;
    
    public CollectionTaskController(CollectionTaskService collectionTaskService) {
        this.collectionTaskService = collectionTaskService;
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CollectionTaskResponse>> getTasksByStatus(
            @PathVariable String status) {
        List<CollectionTaskResponse> tasks = collectionTaskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
    
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> generateTasks() {
        collectionTaskService.generateTasksForFullTrashcans();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{taskId}/reassign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollectionTaskResponse> reassignTask(
            @PathVariable String taskId,
            @Valid @RequestBody ReassignTaskRequest request,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        Long adminId = user.getId();
        CollectionTaskResponse task = collectionTaskService.reassignTask(taskId, request.getNewCollectorId(), adminId);
        return ResponseEntity.ok(task);
    }
    
    @GetMapping("/exceptions/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskExceptionResponse>> getPendingExceptions() {
        List<TaskExceptionResponse> exceptions = collectionTaskService.getPendingExceptions();
        return ResponseEntity.ok(exceptions);
    }
    
    @PostMapping("/exceptions/{exceptionId}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskExceptionResponse> reviewException(
            @PathVariable Long exceptionId,
            @Valid @RequestBody ReviewExceptionRequest request,
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        Long reviewerId = user.getId();
        TaskExceptionResponse exception = collectionTaskService.reviewException(
            exceptionId, request.getStatus(), request.getReviewNotes(), reviewerId);
        return ResponseEntity.ok(exception);
    }
    
    public static class ReassignTaskRequest {
        private Long newCollectorId;

        public Long getNewCollectorId() {
            return newCollectorId;
        }

        public void setNewCollectorId(Long newCollectorId) {
            this.newCollectorId = newCollectorId;
        }
    }
    
    public static class ReviewExceptionRequest {
        private String status;
        private String reviewNotes;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReviewNotes() {
            return reviewNotes;
        }

        public void setReviewNotes(String reviewNotes) {
            this.reviewNotes = reviewNotes;
        }
    }
}
