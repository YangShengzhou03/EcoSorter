package com.ecosorter.controller;

import com.ecosorter.dto.ClassificationResponse;
import com.ecosorter.dto.WasteCategoryResponse;
import com.ecosorter.service.ClassificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/classification")
public class ClassificationController {
    
    private final ClassificationService classificationService;
    
    public ClassificationController(ClassificationService classificationService) {
        this.classificationService = classificationService;
    }
    
    @PostMapping("/classify-image")
    public ResponseEntity<ClassificationResponse> classifyWasteFromImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "userGuess", required = false) String userGuess,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        Long userId = getUserIdFromToken(authorization);
        ClassificationResponse response = classificationService.classifyWasteFromImage(image, description, location, userGuess, userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history")
    public ResponseEntity<Page<ClassificationResponse>> getClassificationHistory(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        Page<ClassificationResponse> history = classificationService.getClassificationHistory(userId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClassificationResponse> getClassificationById(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        Long userId = getUserIdFromToken(authorization);
        ClassificationResponse response = classificationService.getClassificationById(id, userId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/feedback")
    public ResponseEntity<ClassificationResponse> submitFeedback(
            @PathVariable String id,
            @RequestParam String feedback,
            @RequestParam(required = false) String comment,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        Long userId = getUserIdFromToken(authorization);
        ClassificationResponse response = classificationService.submitFeedback(id, feedback, comment, userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<WasteCategoryResponse>> getWasteCategories() {
        List<WasteCategoryResponse> categories = classificationService.getWasteCategories();
        return ResponseEntity.ok(categories);
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