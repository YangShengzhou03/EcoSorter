package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.ClassificationResponse;
import com.ecosorter.dto.WasteCategoryRequest;
import com.ecosorter.dto.WasteCategoryResponse;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.Classification;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import com.ecosorter.service.PointService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClassificationService {

    private final ClassificationRepository classificationRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    private final PointService pointService;

    public ClassificationService(ClassificationRepository classificationRepository,
                                 WasteCategoryRepository wasteCategoryRepository,
                                 PointService pointService) {
        this.classificationRepository = classificationRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.pointService = pointService;
    }

    public IPage<ClassificationResponse> getClassificationHistory(Long userId, int page, int size, String sortBy, String sortDirection) {
        long current = page <= 0 ? 1 : page;
        Page<Classification> mpPage = new Page<>(current, size);

        String sortColumn = switch (sortBy) {
            case "updatedAt" -> "updated_at";
            case "createdAt" -> "created_at";
            default -> "created_at";
        };
        boolean isAsc = "asc".equalsIgnoreCase(sortDirection);

        QueryWrapper<Classification> wrapper = new QueryWrapper<Classification>()
                .eq("user_id", userId)
                .orderBy(true, isAsc, sortColumn);

        IPage<Classification> classificationPage = classificationRepository.selectPage(mpPage, wrapper);

        List<Long> categoryIds = classificationPage.getRecords().stream()
                .map(Classification::getWasteCategoryId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, WasteCategory> categoriesById = categoryIds.isEmpty()
                ? Map.of()
                : wasteCategoryRepository.selectBatchIds(categoryIds).stream()
                        .collect(Collectors.toMap(WasteCategory::getId, Function.identity()));

        Page<ClassificationResponse> responsePage = new Page<>(classificationPage.getCurrent(), classificationPage.getSize(), classificationPage.getTotal());
        responsePage.setRecords(
                classificationPage.getRecords().stream()
                        .map(c -> {
                            WasteCategory category = c.getWasteCategoryId() == null ? null : categoriesById.get(c.getWasteCategoryId());
                            return convertToResponse(c, category);
                        })
                        .collect(Collectors.toList())
        );
        return responsePage;
    }

    @Transactional(readOnly = true)
    public List<WasteCategoryResponse> getWasteCategories() {
        List<WasteCategory> categories = wasteCategoryRepository.selectList(
                new LambdaQueryWrapper<WasteCategory>()
                        .eq(WasteCategory::getIsActive, true)
                        .orderByAsc(WasteCategory::getSortOrder)
        );

        return categories.stream()
                .map(category -> convertToWasteCategoryResponse(category))
                .collect(Collectors.toList());
    }

    @Transactional
    public WasteCategoryResponse createCategory(WasteCategoryRequest request) {
        WasteCategory category = new WasteCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        category.setDisposalMethod(request.getDisposalMethod());
        category.setEnvironmentalImpact(request.getEnvironmentalImpact() != null ? request.getEnvironmentalImpact().toString() : null);
        category.setRecyclingRate(request.getRecyclingRate());
        category.setDisposalInstructions(request.getDisposalInstructions());
        category.setSpecialHandling(request.getSpecialHandling());
        category.setHazardous(request.getHazardous());
        category.setIsActive(request.getActive());
        category.setSortOrder(0);
        if (request.getCommonItems() != null && request.getCommonItems().length > 0) {
            category.setCommonItems(String.join(",", request.getCommonItems()));
        }
        
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        
        wasteCategoryRepository.insert(category);
        return convertToWasteCategoryResponse(category);
    }

    @Transactional
    public WasteCategoryResponse updateCategory(Long categoryId, WasteCategoryRequest request) {
        WasteCategory category = wasteCategoryRepository.selectById(categoryId);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        category.setDisposalMethod(request.getDisposalMethod());
        category.setEnvironmentalImpact(request.getEnvironmentalImpact() != null ? request.getEnvironmentalImpact().toString() : null);
        category.setRecyclingRate(request.getRecyclingRate());
        category.setDisposalInstructions(request.getDisposalInstructions());
        category.setSpecialHandling(request.getSpecialHandling());
        category.setHazardous(request.getHazardous());
        category.setIsActive(request.getActive());
        category.setUpdatedAt(LocalDateTime.now());
        if (request.getCommonItems() != null && request.getCommonItems().length > 0) {
            category.setCommonItems(String.join(",", request.getCommonItems()));
        } else {
            category.setCommonItems(null);
        }
        
        wasteCategoryRepository.updateById(category);
        
        return convertToWasteCategoryResponse(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        WasteCategory category = wasteCategoryRepository.selectById(categoryId);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }
        wasteCategoryRepository.deleteById(categoryId);
    }

    private ClassificationResponse convertToResponse(Classification classification, WasteCategory category) {
        ClassificationResponse response = new ClassificationResponse();
        response.setId(classification.getId() != null ? classification.getId().toString() : null);
        response.setUserId(classification.getUserId() != null ? classification.getUserId().toString() : null);
        response.setCategory(convertToWasteCategoryResponse(category));
        response.setCategoryName(category != null ? category.getName() : "unknown");
        response.setConfidence(classification.getConfidenceScore());
        response.setResult(category != null ? category.getName() : "unknown");
        response.setStatus("completed");
        response.setImageUrl(classification.getImageUrl());
        response.setProcessingTime(1000L);
        response.setModel("eco-sorter-v1.0");
        response.setCreatedAt(classification.getCreatedAt());
        response.setNotes(classification.getNotes());
        response.setPoints(10);

        if (classification.getUserFeedback() != null) {
            response.setUserFeedback(classification.getUserFeedback());
        }

        ClassificationResponse.ClassificationMetrics metrics = new ClassificationResponse.ClassificationMetrics();
        metrics.setAccuracy(classification.getConfidenceScore());
        metrics.setPrecision(classification.getConfidenceScore() * 0.95);
        metrics.setRecall(classification.getConfidenceScore() * 0.9);
        metrics.setF1Score(classification.getConfidenceScore() * 0.92);
        response.setMetrics(metrics);

        ClassificationResponse.ProcessingDetails details = new ClassificationResponse.ProcessingDetails();
        details.setPreprocessingTime(250L);
        details.setInferenceTime(500L);
        details.setPostprocessingTime(250L);
        details.setStages(3);
        response.setProcessingDetails(details);

        return response;
    }

    private WasteCategoryResponse convertToWasteCategoryResponse(WasteCategory category) {
        if (category == null) {
            return null;
        }

        WasteCategoryResponse response = new WasteCategoryResponse();
        response.setId(category.getId() != null ? category.getId().toString() : null);
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setColor(category.getColor());
        response.setIcon(category.getIcon());
        try {
            response.setEnvironmentalImpact(category.getEnvironmentalImpact() != null ? Integer.parseInt(category.getEnvironmentalImpact()) : 8);
        } catch (NumberFormatException e) {
            response.setEnvironmentalImpact(8);
        }
        response.setRecyclingRate(category.getRecyclingRate());
        if (category.getCommonItems() != null && !category.getCommonItems().isEmpty()) {
            response.setCommonItems(category.getCommonItems().split(","));
        } else {
            response.setCommonItems(new String[0]);
        }
        response.setDisposalInstructions(category.getDisposalInstructions() != null 
                ? category.getDisposalInstructions() : "Follow local waste disposal guidelines");
        response.setDisposalMethod(category.getDisposalMethod());
        response.setSpecialHandling(category.getSpecialHandling());
        response.setHazardous(category.getHazardous());
        response.setActive(category.getIsActive());

        return response;
    }
}
