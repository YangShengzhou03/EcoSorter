package com.ecosorter.service;

import com.ecosorter.dto.CollectionTaskResponse;
import com.ecosorter.dto.TaskExceptionRequest;
import com.ecosorter.dto.TaskExceptionResponse;
import com.ecosorter.enums.TaskPriority;
import com.ecosorter.enums.TaskStatus;
import com.ecosorter.model.CollectionTask;
import com.ecosorter.model.TaskException;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.CollectionTaskRepository;
import com.ecosorter.repository.TaskExceptionRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CollectionTaskService {
    
    private final CollectionTaskRepository collectionTaskRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    private final UserRepository userRepository;
    private final TaskExceptionRepository taskExceptionRepository;
    
    public CollectionTaskService(CollectionTaskRepository collectionTaskRepository, 
                               TrashcanDataRepository trashcanDataRepository,
                               UserRepository userRepository,
                               TaskExceptionRepository taskExceptionRepository) {
        this.collectionTaskRepository = collectionTaskRepository;
        this.trashcanDataRepository = trashcanDataRepository;
        this.userRepository = userRepository;
        this.taskExceptionRepository = taskExceptionRepository;
    }
    
    @Transactional(readOnly = true)
    public List<CollectionTaskResponse> getTasksByStatus(String status) {
        List<CollectionTask> tasks = collectionTaskRepository.findTasksByStatusOrdered(status);
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void generateTasksForFullTrashcans() {
        List<TrashcanData> fullTrashcans = trashcanDataRepository.findFullTrashcans();
        
        for (TrashcanData trashcan : fullTrashcans) {
            List<CollectionTask> existingTasks = collectionTaskRepository.findByTrashcanIdAndStatus(trashcan.getId(), TaskStatus.PENDING.getValue());
            boolean taskExists = existingTasks.stream()
                    .anyMatch(t -> t.getTrashcanId() != null && t.getTrashcanId().equals(trashcan.getId()));
            
            if (!taskExists) {
                CollectionTask task = new CollectionTask();
                task.setTaskId("TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                task.setTrashcanId(trashcan.getId());
                task.setStatus(TaskStatus.PENDING);
                task.setPriority(determinePriority(trashcan));
                task.setEstimatedWeight(BigDecimal.valueOf(trashcan.getCapacityLevel() * 0.5));
                task.setGarbageType("混合垃圾");
                
                User assignedCollector = assignTaskToCollector(trashcan);
                if (assignedCollector != null) {
                    task.setCollectorId(assignedCollector.getId());
                }
                
                collectionTaskRepository.save(task);
            }
        }
    }
    
    private User assignTaskToCollector(TrashcanData trashcan) {
        List<User> collectors = userRepository.findByRole(User.UserRole.COLLECTOR.toString());
        if (collectors.isEmpty()) {
            return null;
        }
        
        User leastBusyCollector = collectors.get(0);
        int minPendingTasks = Integer.MAX_VALUE;
        
        for (User collector : collectors) {
            int pendingCount = collectionTaskRepository.findByCollectorIdAndStatus(collector.getId(), TaskStatus.PENDING.getValue()).size();
            if (pendingCount < minPendingTasks) {
                minPendingTasks = pendingCount;
                leastBusyCollector = collector;
            }
        }
        
        return leastBusyCollector;
    }
    
    @Transactional
    public CollectionTaskResponse reassignTask(String taskId, Long newCollectorId, Long adminId) {
        CollectionTask task = collectionTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        User newCollector = userRepository.selectById(newCollectorId);
        if (newCollector == null) {
            throw new RuntimeException("New collector not found");
        }
        
        task.setCollectorId(newCollectorId);
        task.setStatus(TaskStatus.PENDING);
        
        CollectionTask savedTask = collectionTaskRepository.save(task);
        return convertToResponse(savedTask);
    }
    
    @Transactional
    public List<TaskExceptionResponse> getPendingExceptions() {
        List<TaskException> exceptions = taskExceptionRepository.findByStatus(TaskStatus.PENDING.getValue());
        return exceptions.stream()
                .map(e -> convertExceptionToResponse(e, null, null))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TaskExceptionResponse reviewException(Long exceptionId, String status, String reviewNotes, Long reviewerId) {
        TaskException exception = taskExceptionRepository.selectById(exceptionId);
        if (exception == null) {
            throw new RuntimeException("Exception not found");
        }
        
        User reviewer = userRepository.selectById(reviewerId);
        if (reviewer == null) {
            throw new RuntimeException("Reviewer not found");
        }
        
        exception.setStatus(TaskStatus.valueOf(status.toUpperCase()));
        exception.setReviewerId(reviewerId);
        exception.setReviewNotes(reviewNotes);
        exception.setReviewedAt(LocalDateTime.now());
        
        if ("RESOLVED".equals(status) && exception.getTaskId() != null) {
            CollectionTask task = collectionTaskRepository.selectById(exception.getTaskId());
            if (task != null) {
                task.setStatus(TaskStatus.CANCELLED);
                collectionTaskRepository.save(task);
            }
        }
        
        TaskException savedException = taskExceptionRepository.save(exception);
        return convertExceptionToResponse(savedException, null, reviewer);
    }
    
    private TaskExceptionResponse convertExceptionToResponse(TaskException exception, CollectionTask task, User reporter) {
        TaskExceptionResponse response = new TaskExceptionResponse();
        response.setId(exception.getId());
        response.setTaskId(exception.getTaskId() != null ? exception.getTaskId().toString() : "");
        
        if (task == null && exception.getTaskId() != null) {
            task = collectionTaskRepository.selectById(exception.getTaskId());
        }
        
        if (task != null && task.getTrashcanId() != null) {
            TrashcanData trashcan = trashcanDataRepository.selectById(task.getTrashcanId());
            if (trashcan != null) {
                response.setTaskLocation(trashcan.getLocation());
            }
        }
        
        if (reporter == null && exception.getReporterId() != null) {
            reporter = userRepository.selectById(exception.getReporterId());
        }
        
        response.setReporterName(reporter != null ? reporter.getUsername() : "");
        response.setExceptionType(exception.getExceptionType() != null ? exception.getExceptionType().name() : "");
        response.setDescription(exception.getDescription());
        response.setStatus(exception.getStatus().name().toLowerCase());
        
        if (exception.getReviewerId() != null) {
            User reviewer = userRepository.selectById(exception.getReviewerId());
            if (reviewer != null) {
                response.setReviewerName(reviewer.getUsername());
            }
        }
        
        response.setReviewNotes(exception.getReviewNotes());
        response.setReviewedAt(exception.getReviewedAt());
        response.setCreatedAt(exception.getCreatedAt());
        return response;
    }
    
    private TaskPriority determinePriority(TrashcanData trashcan) {
        int capacityPercentage = (int) ((trashcan.getCapacityLevel() * 100.0) / trashcan.getMaxCapacity());
        if (capacityPercentage >= 90) return TaskPriority.HIGH;
        if (capacityPercentage >= trashcan.getThreshold()) return TaskPriority.MEDIUM;
        return TaskPriority.LOW;
    }
    
    private int getPriorityOrder(TaskPriority priority) {
        switch (priority) {
            case HIGH: return 1;
            case MEDIUM: return 2;
            case LOW: return 3;
            default: return 4;
        }
    }
    
    private int getStatusOrder(TaskStatus status) {
        switch (status) {
            case IN_PROGRESS: return 1;
            case PENDING: return 2;
            case COMPLETED: return 3;
            case CANCELLED: return 4;
            default: return 5;
        }
    }
    
    private CollectionTaskResponse convertToResponse(CollectionTask task) {
        CollectionTaskResponse response = new CollectionTaskResponse();
        response.setId(task.getId());
        response.setTaskId(task.getTaskId());
        
        if (task.getTrashcanId() != null) {
            TrashcanData trashcan = trashcanDataRepository.selectById(task.getTrashcanId());
            if (trashcan != null) {
                response.setLocation(trashcan.getLocation());
                response.setDeviceId(trashcan.getDeviceId());
                response.setLatitude(trashcan.getLatitude() != null ? trashcan.getLatitude().doubleValue() : null);
                response.setLongitude(trashcan.getLongitude() != null ? trashcan.getLongitude().doubleValue() : null);
            }
        }
        
        response.setGarbageType(task.getGarbageType());
        response.setEstimatedWeight(task.getEstimatedWeight() != null ? task.getEstimatedWeight().doubleValue() : null);
        response.setActualWeight(task.getActualWeight() != null ? task.getActualWeight().doubleValue() : null);
        response.setPriority(task.getPriority().name().toLowerCase());
        response.setStatus(task.getStatus().name().toLowerCase());
        response.setStartTime(task.getStartTime());
        response.setEndTime(task.getEndTime());
        response.setNotes(task.getNotes());
        
        if (task.getCollectorId() != null) {
            User collector = userRepository.selectById(task.getCollectorId());
            if (collector != null) {
                response.setCollectorName(collector.getUsername());
            }
        }
        
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}
