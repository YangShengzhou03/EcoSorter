package com.ecosorter.service;

import com.ecosorter.dto.CollectorDashboardResponse;
import com.ecosorter.dto.CollectorTaskResponse;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.enums.TaskStatus;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.CollectionTaskRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollectorService {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final CollectionTaskRepository collectionTaskRepository;
    private final UserRepository userRepository;
    
    public CollectorService(TrashcanDataRepository trashcanDataRepository, 
                           CollectionTaskRepository collectionTaskRepository,
                           UserRepository userRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.collectionTaskRepository = collectionTaskRepository;
        this.userRepository = userRepository;
    }
    
    public CollectorDashboardResponse getDashboard(Long userId) {
        CollectorDashboardResponse response = new CollectorDashboardResponse();
        
        List<com.ecosorter.model.CollectionTask> tasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, TaskStatus.PENDING.getValue());
        List<com.ecosorter.model.CollectionTask> inProgressTasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, TaskStatus.IN_PROGRESS.getValue());
        List<com.ecosorter.model.CollectionTask> completedTasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, TaskStatus.COMPLETED.getValue());
        
        response.setTodayTasks(tasks.size());
        response.setCompletedTasks(completedTasks.size());
        response.setInProgressTasks(inProgressTasks.size());
        response.setAbnormalDevices(trashcanDataRepository.findFullTrashcans().size());
        
        return response;
    }
    
    public List<CollectorTaskResponse> getTasks(Long userId) {
        List<com.ecosorter.model.CollectionTask> tasks = collectionTaskRepository.findByCollectorId(userId);
        
        return tasks.stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }
    
    public CollectorTaskResponse getTaskDetail(String taskId, Long userId) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        return convertToTaskResponse(task);
    }
    
    @Transactional
    public CollectorTaskResponse startTask(String taskId, Long userId) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        if (task.getStatus() != TaskStatus.PENDING) {
            throw new RuntimeException("Task is not in pending status");
        }
        
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setUpdatedAt(LocalDateTime.now());
        collectionTaskRepository.save(task);
        
        return convertToTaskResponse(task);
    }
    
    @Transactional
    public CollectorTaskResponse completeTask(String taskId, Long userId) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new RuntimeException("Task is not in progress status");
        }
        
        task.setStatus(TaskStatus.COMPLETED);
        task.setEndTime(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        collectionTaskRepository.save(task);
        
        return convertToTaskResponse(task);
    }
    
    @Transactional
    public void reportException(String taskId, Long userId, String description) {
        com.ecosorter.model.CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        if (!task.getCollectorId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        task.setStatus(TaskStatus.PENDING);
        task.setUpdatedAt(LocalDateTime.now());
        collectionTaskRepository.save(task);
    }
    
    public List<DeviceListResponse> getDevices() {
        List<com.ecosorter.model.TrashcanData> trashcans = trashcanDataRepository.findAll();
        
        return trashcans.stream()
                .map(this::convertToDeviceListResponse)
                .collect(Collectors.toList());
    }
    
    private CollectorTaskResponse convertToTaskResponse(com.ecosorter.model.CollectionTask task) {
        CollectorTaskResponse collectorTask = new CollectorTaskResponse();
        collectorTask.setTaskId(task.getTaskId());
        
        if (task.getTrashcanId() != null) {
            TrashcanData trashcan = trashcanDataRepository.selectById(task.getTrashcanId());
            if (trashcan != null) {
                collectorTask.setLocation(trashcan.getLocation());
                collectorTask.setDeviceId(trashcan.getDeviceId());
            }
        }
        
        if (task.getCollectorId() != null) {
            User collector = userRepository.selectById(task.getCollectorId());
            if (collector != null) {
                collectorTask.setCollectorName(collector.getUsername());
            }
        }
        
        collectorTask.setGarbageType(task.getGarbageType());
        collectorTask.setEstimatedWeight(task.getEstimatedWeight() != null ? task.getEstimatedWeight().doubleValue() : 0.0);
        collectorTask.setPriority(task.getPriority().name().toLowerCase());
        collectorTask.setStatus(task.getStatus().name().toLowerCase());
        collectorTask.setCreatedAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : "");
        collectorTask.setCompletedAt(task.getEndTime() != null ? task.getEndTime().toString() : "");
        return collectorTask;
    }
    
    private DeviceListResponse convertToDeviceListResponse(com.ecosorter.model.TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setLocation(trashcan.getLocation());
        device.setCapacityLevel(trashcan.getCapacityLevel() != null ? trashcan.getCapacityLevel().intValue() : 0);
        device.setMaxCapacity(trashcan.getMaxCapacity() != null ? trashcan.getMaxCapacity().intValue() : 0);
        device.setThreshold(trashcan.getThreshold() != null ? trashcan.getThreshold().intValue() : 0);
        device.setStatus(trashcan.getStatus());
        device.setStatusText(getStatusText(trashcan.getStatus()));
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? trashcan.getUpdatedAt().toString() : "");
        return device;
    }
    
    private String getStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "online": return "正常";
            case "offline": return "异常";
            case "maintenance": return "维护中";
            case "error": return "异常";
            default: return "未知";
        }
    }
}
