package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecosorter.dto.ClassificationResponse;
import com.ecosorter.dto.ClassificationSubmitRequest;
import com.ecosorter.dto.WasteCategoryRequest;
import com.ecosorter.dto.WasteCategoryResponse;
import com.ecosorter.model.Classification;
import com.ecosorter.model.UserStatistics;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import com.ecosorter.service.ClassificationService;
import com.ecosorter.service.PointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/classification")
public class ClassificationController {
    
    private final ClassificationService classificationService;
    private final ClassificationRepository classificationRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final PointService pointService;
    
    public ClassificationController(ClassificationService classificationService,
                                     ClassificationRepository classificationRepository,
                                     WasteCategoryRepository wasteCategoryRepository,
                                     UserStatisticsRepository userStatisticsRepository,
                                     PointService pointService) {
        this.classificationService = classificationService;
        this.classificationRepository = classificationRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.pointService = pointService;
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
    
    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> submitClassification(
            @AuthenticationPrincipal com.ecosorter.model.User user,
            @Valid @RequestBody ClassificationSubmitRequest request) {
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        Classification classification = new Classification();
        classification.setUserId(user.getId());
        classification.setTrashcanId(request.getTrashcanId());
        classification.setWasteCategoryId(request.getCategoryId());
        classification.setConfidenceScore(request.getConfidence() != null ? request.getConfidence() : 1.0);
        classification.setCreatedAt(LocalDateTime.now());
        classification.setUpdatedAt(LocalDateTime.now());
        
        classificationRepository.insert(classification);
        
        WasteCategory category = wasteCategoryRepository.selectById(request.getCategoryId());
        Integer basePoints = (category != null && category.getPoints() != null) ? 
            category.getPoints() : 10;
        Double confidenceBonus = (request.getConfidence() != null ? request.getConfidence() : 1.0) * 10;
        Integer calculatedPoints = basePoints + confidenceBonus.intValue();
        
        pointService.addPoints(user.getId(), calculatedPoints, "classification", 
            classification.getId(), "垃圾分类: " + 
            (category != null ? category.getName() : "未知"));
        
        UserStatistics statistics = userStatisticsRepository.selectOne(
            new LambdaQueryWrapper<UserStatistics>()
                .eq(UserStatistics::getUserId, user.getId())
        );
        
        if (statistics == null) {
            statistics = new UserStatistics();
            statistics.setUserId(user.getId());
            statistics.setTotalClassifications(1);
            statistics.setCorrectClassifications(1);
            statistics.setStreakDays(1);
            statistics.setLongestStreak(1);
            statistics.setCreatedAt(LocalDateTime.now());
            userStatisticsRepository.insert(statistics);
        } else {
            statistics.setTotalClassifications(
                (statistics.getTotalClassifications() != null ? statistics.getTotalClassifications() : 0) + 1
            );
            statistics.setCorrectClassifications(
                (statistics.getCorrectClassifications() != null ? statistics.getCorrectClassifications() : 0) + 1
            );
            
            LocalDate today = LocalDate.now();
            LocalDate lastUpdateDate = statistics.getUpdatedAt() != null ? 
                statistics.getUpdatedAt().toLocalDate() : null;
            
            if (lastUpdateDate != null) {
                if (lastUpdateDate.equals(today.minusDays(1))) {
                    statistics.setStreakDays(
                        (statistics.getStreakDays() != null ? statistics.getStreakDays() : 0) + 1
                    );
                } else if (!lastUpdateDate.equals(today)) {
                    statistics.setStreakDays(1);
                }
            } else {
                statistics.setStreakDays(1);
            }
            
            Integer currentStreak = statistics.getStreakDays() != null ? statistics.getStreakDays() : 0;
            Integer longestStreak = statistics.getLongestStreak() != null ? statistics.getLongestStreak() : 0;
            if (currentStreak > longestStreak) {
                statistics.setLongestStreak(currentStreak);
            }
            
            statistics.setUpdatedAt(LocalDateTime.now());
            userStatisticsRepository.updateById(statistics);
        }
        
        return ResponseEntity.ok().build();
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
