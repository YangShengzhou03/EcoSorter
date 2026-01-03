package com.ecosorter.controller;

import com.ecosorter.dto.ClassificationRequest;
import com.ecosorter.dto.ClassificationResponse;
import com.ecosorter.service.ClassificationService;
import com.ecosorter.security.CurrentUser;
import com.ecosorter.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/classification")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Classification", description = "Waste classification API endpoints")
public class ClassificationController {
    
    private final ClassificationService classificationService;
    
    @PostMapping("/classify")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Classify waste item",
        description = "Classify a waste item using AI model and return category information",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ClassificationResponse> classifyWaste(
            @Parameter(description = "Classification request data") @Valid @RequestBody ClassificationRequest request,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        
        log.info("User {} is classifying waste", currentUser.getUsername());
        ClassificationResponse response = classificationService.classifyWaste(request, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/classify-image")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Classify waste from image file",
        description = "Upload an image file and classify the waste item",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ClassificationResponse> classifyWasteFromImage(
            @Parameter(description = "Image file to classify") @RequestParam("image") MultipartFile image,
            @Parameter(description = "Additional description") @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "Location information") @RequestParam(value = "location", required = false) String location,
            @Parameter(description = "User's guess for waste category") @RequestParam(value = "userGuess", required = false) String userGuess,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        
        log.info("User {} is uploading image for classification", currentUser.getUsername());
        ClassificationResponse response = classificationService.classifyWasteFromImage(image, description, location, userGuess, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Get classification history",
        description = "Get paginated classification history for the current user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Page<ClassificationResponse>> getClassificationHistory(
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDirection) {
        
        log.info("User {} is retrieving classification history", currentUser.getUsername());
        Page<ClassificationResponse> history = classificationService.getClassificationHistory(currentUser.getId(), page, size, sortBy, sortDirection);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all classifications (Admin only)",
        description = "Get all classifications for administrative purposes",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Page<ClassificationResponse>> getAllClassifications(
            @PageableDefault(size = 20, sort = "createdAt,desc") Pageable pageable,
            @Parameter(description = "Filter by user ID") @RequestParam(required = false) String userId,
            @Parameter(description = "Filter by category ID") @RequestParam(required = false) String categoryId,
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by date range (start)") @RequestParam(required = false) String startDate,
            @Parameter(description = "Filter by date range (end)") @RequestParam(required = false) String endDate) {
        
        log.info("Admin is retrieving all classifications");
        Page<ClassificationResponse> classifications = classificationService.getAllClassifications(userId, categoryId, status, startDate, endDate, pageable);
        return ResponseEntity.ok(classifications);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Get classification by ID",
        description = "Get detailed information about a specific classification",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ClassificationResponse> getClassificationById(
            @Parameter(description = "Classification ID") @PathVariable String id,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        
        log.info("User {} is retrieving classification {}", currentUser.getUsername(), id);
        ClassificationResponse response = classificationService.getClassificationById(id, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/feedback")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Submit user feedback",
        description = "Submit user feedback for a classification to improve the model",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ClassificationResponse> submitFeedback(
            @Parameter(description = "Classification ID") @PathVariable String id,
            @Parameter(description = "User feedback (accurate/inaccurate)") @RequestParam String feedback,
            @Parameter(description = "Optional comment") @RequestParam(required = false) String comment,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        
        log.info("User {} is submitting feedback for classification {}", currentUser.getUsername(), id);
        ClassificationResponse response = classificationService.submitFeedback(id, feedback, comment, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete classification (Admin only)",
        description = "Delete a classification record",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteClassification(
            @Parameter(description = "Classification ID") @PathVariable String id) {
        
        log.info("Admin is deleting classification {}", id);
        classificationService.deleteClassification(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Get classification statistics",
        description = "Get statistics about user's classifications",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ClassificationStatisticsResponse> getClassificationStatistics(
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser,
            @Parameter(description = "Time period (daily/weekly/monthly/yearly)") @RequestParam(defaultValue = "monthly") String period) {
        
        log.info("User {} is retrieving classification statistics", currentUser.getUsername());
        ClassificationStatisticsResponse statistics = classificationService.getClassificationStatistics(currentUser.getId(), period);
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/categories")
    @Operation(
        summary = "Get waste categories",
        description = "Get all available waste categories for classification"
    )
    public ResponseEntity<List<WasteCategoryResponse>> getWasteCategories() {
        log.info("Retrieving waste categories");
        List<WasteCategoryResponse> categories = classificationService.getWasteCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/suggestions")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
        summary = "Get classification suggestions",
        description = "Get AI-powered suggestions for waste classification",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<ClassificationSuggestionResponse>> getClassificationSuggestions(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        
        log.info("User {} is getting classification suggestions for query: {}", currentUser.getUsername(), query);
        List<ClassificationSuggestionResponse> suggestions = classificationService.getClassificationSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
}