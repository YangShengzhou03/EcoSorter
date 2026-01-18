package com.ecosorter.service;

import com.ecosorter.dto.CollectionTaskResponse;
import com.ecosorter.model.CollectionTask;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.repository.CollectionTaskRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CollectionTaskService {
    
    private final CollectionTaskRepository collectionTaskRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    
    public CollectionTaskService(CollectionTaskRepository collectionTaskRepository, 
                               TrashcanDataRepository trashcanDataRepository) {
        this.collectionTaskRepository = collectionTaskRepository;
        this.trashcanDataRepository = trashcanDataRepository;
    }
    
    @Transactional(readOnly = true)
    public List<CollectionTaskResponse> getTasks(Long collectorId) {
        List<CollectionTask> tasks = collectionTaskRepository.findByCollectorIdAndStatus(collectorId, "pending");
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CollectionTaskResponse> getTasksByStatus(String status) {
        List<CollectionTask> tasks = collectionTaskRepository.findTasksByStatusOrdered(status);
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CollectionTaskResponse> getNavigationRoute(Long collectorId) {
        List<CollectionTask> tasks = collectionTaskRepository.findByCollectorIdAndStatus(collectorId, "pending");
        return tasks.stream()
                .sorted((t1, t2) -> {
                    int priorityCompare = getPriorityOrder(t1.getPriority()) - getPriorityOrder(t2.getPriority());
                    if (priorityCompare != 0) return priorityCompare;
                    return t1.getCreatedAt().compareTo(t2.getCreatedAt());
                })
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CollectionTaskResponse startTask(String taskId, Long collectorId) {
        CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setStatus("in_progress");
        task.setStartTime(LocalDateTime.now());
        
        CollectionTask savedTask = collectionTaskRepository.save(task);
        return convertToResponse(savedTask);
    }
    
    @Transactional
    public CollectionTaskResponse completeTask(String taskId, Long collectorId, BigDecimal actualWeight, String notes) {
        CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setStatus("completed");
        task.setEndTime(LocalDateTime.now());
        task.setActualWeight(actualWeight);
        task.setNotes(notes);
        
        TrashcanData trashcan = task.getTrashcan();
        if (trashcan != null) {
            trashcan.setCapacityLevel(0);
            trashcanDataRepository.save(trashcan);
        }
        
        CollectionTask savedTask = collectionTaskRepository.save(task);
        return convertToResponse(savedTask);
    }
    
    @Transactional
    public void generateTasksForFullTrashcans() {
        List<TrashcanData> fullTrashcans = trashcanDataRepository.findFullTrashcans();
        
        for (TrashcanData trashcan : fullTrashcans) {
            List<CollectionTask> existingTasks = collectionTaskRepository.findByTrashcanIdAndStatus(trashcan.getId(), "pending");
            boolean taskExists = existingTasks.stream()
                    .anyMatch(t -> t.getTrashcan() != null && t.getTrashcan().getId().equals(trashcan.getId()));
            
            if (!taskExists) {
                CollectionTask task = new CollectionTask();
                task.setTaskId("TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                task.setTrashcan(trashcan);
                task.setStatus("pending");
                task.setPriority(determinePriority(trashcan));
                task.setEstimatedWeight(BigDecimal.valueOf(trashcan.getCapacityLevel() * 0.5));
                task.setGarbageType("混合垃圾");
                
                collectionTaskRepository.save(task);
            }
        }
    }
    
    private String determinePriority(TrashcanData trashcan) {
        int capacityPercentage = (int) ((trashcan.getCapacityLevel() * 100.0) / trashcan.getMaxCapacity());
        if (capacityPercentage >= 90) return "high";
        if (capacityPercentage >= trashcan.getThreshold()) return "medium";
        return "low";
    }
    
    private int getPriorityOrder(String priority) {
        switch (priority) {
            case "high": return 1;
            case "medium": return 2;
            case "low": return 3;
            default: return 4;
        }
    }
    
    private CollectionTaskResponse convertToResponse(CollectionTask task) {
        CollectionTaskResponse response = new CollectionTaskResponse();
        response.setId(task.getId());
        response.setTaskId(task.getTaskId());
        response.setLocation(task.getTrashcan() != null ? task.getTrashcan().getLocation() : "");
        response.setGarbageType(task.getGarbageType());
        response.setEstimatedWeight(task.getEstimatedWeight() != null ? task.getEstimatedWeight().doubleValue() : 0.0);
        response.setActualWeight(task.getActualWeight() != null ? task.getActualWeight().doubleValue() : 0.0);
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        response.setStartTime(task.getStartTime());
        response.setEndTime(task.getEndTime());
        response.setNotes(task.getNotes());
        response.setDeviceId(task.getTrashcan() != null ? task.getTrashcan().getDeviceId() : "");
        response.setLatitude(task.getTrashcan() != null ? task.getTrashcan().getLatitude() : null);
        response.setLongitude(task.getTrashcan() != null ? task.getTrashcan().getLongitude() : null);
        response.setCollectorName(task.getCollector() != null ? task.getCollector().getUsername() : "");
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}
