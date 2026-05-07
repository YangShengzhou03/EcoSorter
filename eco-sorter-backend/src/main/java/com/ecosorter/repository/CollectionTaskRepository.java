package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.CollectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CollectionTaskRepository extends BaseMapper<CollectionTask> {
    
    @Select("SELECT * FROM collection_tasks WHERE collector_id = #{collectorId}")
    List<CollectionTask> findByCollectorId(Long collectorId);
    
    @Select("SELECT * FROM collection_tasks WHERE collector_id = #{collectorId} AND status = #{status}")
    List<CollectionTask> findByCollectorIdAndStatus(Long collectorId, String status);
    
    @Select("SELECT * FROM collection_tasks WHERE task_id = #{taskId}")
    Optional<CollectionTask> findByTaskId(String taskId);
    
    @Select("SELECT * FROM collection_tasks WHERE trashcan_id = #{trashcanId} AND status = #{status}")
    List<CollectionTask> findByTrashcanIdAndStatus(Long trashcanId, String status);
    
    @Select("SELECT * FROM collection_tasks WHERE status = #{status} ORDER BY created_at DESC")
    List<CollectionTask> findTasksByStatusOrdered(String status);
    
    default Optional<CollectionTask> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }
    
    default CollectionTask save(CollectionTask task) {
        if (task.getId() == null) {
            insert(task);
        } else {
            updateById(task);
        }
        return task;
    }
}
