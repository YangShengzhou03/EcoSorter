package com.ecosorter.repository;

import com.ecosorter.model.CollectionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionTaskRepository extends JpaRepository<CollectionTask, Long> {
    
    Optional<CollectionTask> findByTaskId(String taskId);
    
    List<CollectionTask> findByCollectorId(Long collectorId);
    
    List<CollectionTask> findByStatus(String status);
    
    List<CollectionTask> findByStatusOrderByPriorityDescCreatedAtAsc(String status);
    
    List<CollectionTask> findByCollectorIdAndStatus(Long collectorId, String status);
    
    List<CollectionTask> findByTrashcanIdAndStatus(Long trashcanId, String status);
    
    @Query("SELECT t FROM CollectionTask t WHERE t.status = :status ORDER BY " +
           "CASE t.priority WHEN 'high' THEN 1 WHEN 'medium' THEN 2 WHEN 'low' THEN 3 ELSE 4 END, " +
           "t.createdAt ASC")
    List<CollectionTask> findTasksByStatusOrdered(@Param("status") String status);
    
    @Query("SELECT COUNT(t) FROM CollectionTask t WHERE t.collector.id = :collectorId AND t.status = 'completed' AND t.createdAt >= :startDate")
    long countCompletedTasksByCollectorAndDate(@Param("collectorId") Long collectorId, @Param("startDate") java.time.LocalDateTime startDate);
}
