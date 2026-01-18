package com.ecosorter.service;

import com.ecosorter.dto.CollectorDashboardResponse;
import com.ecosorter.dto.CollectorTaskResponse;
import com.ecosorter.dto.CollectionRecordResponse;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.CollectionTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectorService {
    
    private final TrashcanDataRepository trashcanDataRepository;
    private final CollectionTaskRepository collectionTaskRepository;
    
    public CollectorService(TrashcanDataRepository trashcanDataRepository, 
                           CollectionTaskRepository collectionTaskRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
        this.collectionTaskRepository = collectionTaskRepository;
    }
    
    public CollectorDashboardResponse getDashboard(Long userId) {
        CollectorDashboardResponse response = new CollectorDashboardResponse();
        
        List<com.ecosorter.model.CollectionTask> tasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, "pending");
        List<com.ecosorter.model.CollectionTask> inProgressTasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, "in_progress");
        List<com.ecosorter.model.CollectionTask> completedTasks = collectionTaskRepository.findByCollectorIdAndStatus(userId, "completed");
        
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
    
    private CollectorTaskResponse convertToTaskResponse(com.ecosorter.model.CollectionTask task) {
        CollectorTaskResponse collectorTask = new CollectorTaskResponse();
        collectorTask.setTaskId(task.getTaskId());
        collectorTask.setLocation(task.getTrashcan() != null ? task.getTrashcan().getLocation() : "");
        collectorTask.setGarbageType(task.getGarbageType());
        collectorTask.setEstimatedWeight(task.getEstimatedWeight() != null ? task.getEstimatedWeight().doubleValue() : 0.0);
        collectorTask.setPriority(task.getPriority());
        collectorTask.setStatus(task.getStatus());
        return collectorTask;
    }
    
    @Transactional(readOnly = true)
    public List<CollectionRecordResponse> getCollectionRecords(Long userId) {
        List<com.ecosorter.model.CollectionTask> tasks = collectionTaskRepository.findByCollectorId(userId);
        
        return tasks.stream()
                .filter(t -> "completed".equals(t.getStatus()))
                .map(this::convertToRecordResponse)
                .collect(Collectors.toList());
    }
    
    private CollectionRecordResponse convertToRecordResponse(com.ecosorter.model.CollectionTask task) {
        CollectionRecordResponse record = new CollectionRecordResponse();
        record.setId(task.getId());
        record.setTaskId(task.getTaskId());
        record.setLocation(task.getTrashcan() != null ? task.getTrashcan().getLocation() : "");
        record.setGarbageType(task.getGarbageType());
        record.setActualWeight(task.getActualWeight() != null ? task.getActualWeight().doubleValue() : 0.0);
        record.setStatus(task.getStatus());
        record.setStartTime(task.getStartTime());
        record.setEndTime(task.getEndTime());
        return record;
    }
}
