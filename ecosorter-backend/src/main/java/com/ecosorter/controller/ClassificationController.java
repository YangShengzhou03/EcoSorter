package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecosorter.dto.ClassificationResponse;
import com.ecosorter.dto.WasteCategoryRequest;
import com.ecosorter.dto.WasteCategoryResponse;
import com.ecosorter.service.ClassificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classification")
public class ClassificationController {
    
    private final ClassificationService classificationService;
    
    public ClassificationController(ClassificationService classificationService) {
        this.classificationService = classificationService;
    }
    
    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IPage<ClassificationResponse>> getClassificationHistory(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String status) {
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        IPage<ClassificationResponse> history = classificationService.getClassificationHistory(user.getId(), page, pageSize, sortBy, sortDirection, categoryName, status);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<WasteCategoryResponse>> getWasteCategories() {
        List<WasteCategoryResponse> categories = classificationService.getWasteCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public ResponseEntity<List<WasteCategoryResponse>> searchWasteCategories(
            @RequestParam String keyword) {
        List<WasteCategoryResponse> results = classificationService.searchWasteCategories(keyword);
        return ResponseEntity.ok(results);
    }
    
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WasteCategoryResponse> createCategory(@Valid @RequestBody WasteCategoryRequest request) {
        WasteCategoryResponse response = classificationService.createCategory(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WasteCategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody WasteCategoryRequest request) {
        WasteCategoryResponse response = classificationService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        classificationService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
