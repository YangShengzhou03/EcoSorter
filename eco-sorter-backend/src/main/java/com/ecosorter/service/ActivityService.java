package com.ecosorter.service;

import com.ecosorter.dto.ActivityResponse;
import com.ecosorter.dto.ReportResponse;
import com.ecosorter.model.Classification;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {
    
    private final ClassificationRepository classificationRepository;
    private final UserRepository userRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    
    public ActivityService(ClassificationRepository classificationRepository,
                          UserRepository userRepository,
                          WasteCategoryRepository wasteCategoryRepository,
                          TrashcanDataRepository trashcanDataRepository) {
        this.classificationRepository = classificationRepository;
        this.userRepository = userRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.trashcanDataRepository = trashcanDataRepository;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivities() {
        List<Classification> recentClassifications = 
            classificationRepository.findTop10ByOrderByCreatedAtDesc();
        
        return recentClassifications.stream()
                .map(this::convertToActivityResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getReports() {
        List<Classification> recentClassifications = 
            classificationRepository.findTop20ByOrderByCreatedAtDesc();
        
        return recentClassifications.stream()
                .map(this::convertToReportResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private ActivityResponse convertToActivityResponse(Classification classification) {
        ActivityResponse activity = new ActivityResponse();
        activity.setId(classification.getId());
        activity.setCreatedAt(classification.getCreatedAt());
        activity.setType("waste_classification");
        activity.setTypeText("垃圾分类");

        User user = userRepository.selectById(classification.getUserId());
        String userName = user != null ? user.getUsername() : "未知用户";
        activity.setDescription("用户 " + userName + " 分类垃圾");
        activity.setUserName(userName);

        return activity;
    }

    private ReportResponse convertToReportResponse(Classification classification) {
        ReportResponse report = new ReportResponse();
        report.setId(classification.getId());
        report.setCreatedAt(classification.getCreatedAt());
        report.setType("waste_classification");
        
        WasteCategory category = null;
        if (classification.getWasteCategoryId() != null) {
            category = wasteCategoryRepository.selectById(classification.getWasteCategoryId());
        }
        String categoryName = category != null ? category.getName() : "未知";
        report.setTypeText(categoryName);
        report.setTitle("垃圾分类记录");
        report.setDescription("用户完成垃圾分类操作");
        report.setStatus("completed");
        report.setStatusText("已完成");

        User user = userRepository.selectById(classification.getUserId());
        String userName = user != null ? user.getUsername() : "未知用户";
        report.setUserName(userName);
        
        Double weight = 0.0;
        if (classification.getTrashcanId() != null) {
            TrashcanData trashcan = trashcanDataRepository.selectById(classification.getTrashcanId());
            if (trashcan != null && trashcan.getCapacityLevel() != null) {
                weight = trashcan.getCapacityLevel().doubleValue();
            }
        }
        report.setWeight(weight);

        return report;
    }
}
