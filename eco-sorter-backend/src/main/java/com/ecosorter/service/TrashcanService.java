package com.ecosorter.service;

import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.TrashcanFaultRequest;
import com.ecosorter.enums.TaskPriority;
import com.ecosorter.enums.TaskStatus;
import com.ecosorter.enums.TrashcanStatus;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.Classification;
import com.ecosorter.model.CollectionTask;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.model.UserStatistics;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.CollectionTaskRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import com.ecosorter.util.StatusUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TrashcanService {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final ClassificationRepository classificationRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    private final UserRepository userRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final PointService pointService;
    private final CollectionTaskRepository collectionTaskRepository;
    
    public TrashcanService(TrashcanDataRepository trashcanDataRepository,
                          ClassificationRepository classificationRepository,
                          WasteCategoryRepository wasteCategoryRepository,
                          UserRepository userRepository,
                          UserStatisticsRepository userStatisticsRepository,
                          PointService pointService,
                          CollectionTaskRepository collectionTaskRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.classificationRepository = classificationRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.userRepository = userRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.pointService = pointService;
        this.collectionTaskRepository = collectionTaskRepository;
    }

    public DeviceListResponse getDeviceInfo(TrashcanData trashcan) {
        return convertToDeviceListResponse(trashcan);
    }

    @Transactional
    public DeviceListResponse updateDeviceInfo(TrashcanData trashcan, Map<String, Object> updates) {
        if (updates.containsKey("deviceId")) {
            trashcan.setDeviceId((String) updates.get("deviceId"));
        }
        if (updates.containsKey("location")) {
            trashcan.setLocation((String) updates.get("location"));
        }
        if (updates.containsKey("deviceName")) {
            trashcan.setDeviceName((String) updates.get("deviceName"));
        }
        if (updates.containsKey("binType")) {
            trashcan.setBinType((String) updates.get("binType"));
        }
        if (updates.containsKey("maxCapacity")) {
            trashcan.setMaxCapacity(((Number) updates.get("maxCapacity")).intValue());
        }
        if (updates.containsKey("threshold")) {
            trashcan.setThreshold(((Number) updates.get("threshold")).intValue());
        }
        
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        return convertToDeviceListResponse(trashcan);
    }

    @Transactional
    public DeviceListResponse updateStatus(TrashcanData trashcan, Map<String, Object> statusData) {
        if (statusData.containsKey("capacityLevel")) {
            trashcan.setCapacityLevel(((Number) statusData.get("capacityLevel")).intValue());
        }
        
        if (statusData.containsKey("status")) {
            trashcan.setStatus((String) statusData.get("status"));
        }
        
        trashcan.setLastActive(LocalDateTime.now());
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        autoCreateCollectionTaskIfNeeded(trashcan);
        
        return convertToDeviceListResponse(trashcan);
    }

    @Transactional
    public void submitClassification(TrashcanData trashcan, Map<String, Object> data) {
        String imageUrl = (String) data.get("imageUrl");
        Long categoryId = ((Number) data.get("categoryId")).longValue();
        Double confidence = ((Number) data.get("confidence")).doubleValue();
        Long userId = data.containsKey("userId") ? ((Number) data.get("userId")).longValue() : 4L;
        
        Classification classification = new Classification();
        classification.setUserId(userId);
        classification.setTrashcanId(trashcan.getId());
        classification.setWasteCategoryId(categoryId);
        classification.setImageUrl(imageUrl);
        classification.setConfidenceScore(confidence);
        classification.setCreatedAt(LocalDateTime.now());
        classification.setUpdatedAt(LocalDateTime.now());
        
        classificationRepository.insert(classification);
        
        if (trashcan.getCapacityLevel() != null) {
            trashcan.setCapacityLevel(trashcan.getCapacityLevel() + 1);
        } else {
            trashcan.setCapacityLevel(1);
        }
        trashcan.setLastActive(LocalDateTime.now());
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
        
        autoCreateCollectionTaskIfNeeded(trashcan);
        
        if (userId != null && userId != 4L) {
            WasteCategory category = wasteCategoryRepository.selectById(categoryId);
            Integer basePoints = (category != null && category.getPoints() != null) ? category.getPoints() : 10;
            Double confidenceBonus = confidence * 10;
            Integer calculatedPoints = basePoints + confidenceBonus.intValue();
            
            pointService.addPoints(userId, calculatedPoints, "classification", 
                classification.getId(), "垃圾分类: " + (category != null ? category.getName() : "未知"));
            
            updateUserStatistics(userId);
        }
    }

    @Transactional
    public void heartbeat(TrashcanData trashcan) {
        trashcan.setLastActive(LocalDateTime.now());
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
    }

    public boolean verifyAdminPassword(TrashcanData trashcan, String password) {
        return trashcan.getAdminPassword() != null && trashcan.getAdminPassword().equals(password);
    }

    @Transactional
    public void resetAdminPassword(TrashcanData trashcan, String newPassword) {
        trashcan.setAdminPassword(newPassword);
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
    }

    @Transactional
    public void clearDeviceData(TrashcanData trashcan) {
        classificationRepository.deleteByTrashcanId(trashcan.getId());
        trashcan.setCapacityLevel(0);
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
    }

    @Transactional
    public void reportFault(TrashcanData trashcan, TrashcanFaultRequest request) {
        trashcan.setStatus("error");
        trashcan.setUpdatedAt(LocalDateTime.now());
        trashcanDataRepository.updateById(trashcan);
    }

    private void autoCreateCollectionTaskIfNeeded(TrashcanData trashcan) {
        if (trashcan.getCapacityLevel() == null || trashcan.getThreshold() == null) {
            return;
        }
        
        if (trashcan.getCapacityLevel() < trashcan.getThreshold()) {
            return;
        }
        
        List<CollectionTask> existingTasks = collectionTaskRepository.findByTrashcanIdAndStatus(
            trashcan.getId(), TaskStatus.PENDING.getValue());
        if (!existingTasks.isEmpty()) {
            return;
        }
        
        CollectionTask task = new CollectionTask();
        task.setTaskId("TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        task.setTrashcanId(trashcan.getId());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(determinePriority(trashcan));
        task.setEstimatedWeight(BigDecimal.valueOf(trashcan.getCapacityLevel() * 0.5));
        task.setGarbageType(trashcan.getBinType() != null ? trashcan.getBinType() : "混合垃圾");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        User assignedCollector = assignTaskToCollector();
        if (assignedCollector != null) {
            task.setCollectorId(assignedCollector.getId());
        }
        
        collectionTaskRepository.insert(task);
    }

    private TaskPriority determinePriority(TrashcanData trashcan) {
        if (trashcan.getMaxCapacity() == null || trashcan.getMaxCapacity() <= 0) {
            return TaskPriority.MEDIUM;
        }
        int capacityPercentage = (trashcan.getCapacityLevel() * 100) / trashcan.getMaxCapacity();
        if (capacityPercentage >= 90) return TaskPriority.HIGH;
        if (capacityPercentage >= trashcan.getThreshold()) return TaskPriority.MEDIUM;
        return TaskPriority.LOW;
    }

    private User assignTaskToCollector() {
        List<User> collectors = userRepository.findByRole(User.UserRole.COLLECTOR.toString());
        if (collectors.isEmpty()) {
            return null;
        }
        
        User leastBusyCollector = collectors.get(0);
        int minPendingTasks = Integer.MAX_VALUE;
        
        for (User collector : collectors) {
            int pendingCount = collectionTaskRepository.findByCollectorIdAndStatus(
                collector.getId(), TaskStatus.PENDING.getValue()).size();
            if (pendingCount < minPendingTasks) {
                minPendingTasks = pendingCount;
                leastBusyCollector = collector;
            }
        }
        
        return leastBusyCollector;
    }

    private void updateUserStatistics(Long userId) {
        UserStatistics statistics = userStatisticsRepository.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserStatistics>()
                .eq(UserStatistics::getUserId, userId)
        );
        if (statistics == null) {
            statistics = new UserStatistics();
            statistics.setUserId(userId);
            statistics.setTotalClassifications(1);
            statistics.setCorrectClassifications(1);
            statistics.setCreatedAt(LocalDateTime.now());
            userStatisticsRepository.insert(statistics);
        } else {
            statistics.setTotalClassifications(
                (statistics.getTotalClassifications() != null ? statistics.getTotalClassifications() : 0) + 1
            );
            statistics.setCorrectClassifications(
                (statistics.getCorrectClassifications() != null ? statistics.getCorrectClassifications() : 0) + 1
            );
            statistics.setUpdatedAt(LocalDateTime.now());
            userStatisticsRepository.updateById(statistics);
        }
    }

    private DeviceListResponse convertToDeviceListResponse(TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setDeviceName(trashcan.getDeviceName());
        device.setLocation(trashcan.getLocation());
        device.setBinType(trashcan.getBinType());
        device.setCapacityLevel(trashcan.getCapacityLevel() != null ? trashcan.getCapacityLevel().intValue() : 0);
        device.setMaxCapacity(trashcan.getMaxCapacity() != null ? trashcan.getMaxCapacity().intValue() : 0);
        device.setThreshold(trashcan.getThreshold() != null ? trashcan.getThreshold().intValue() : 0);
        device.setStatus(trashcan.getStatus());
        device.setStatusText(StatusUtil.getTrashcanStatusText(trashcan.getStatus()));
        device.setLastUpdate(trashcan.getUpdatedAt());
        return device;
    }
}
