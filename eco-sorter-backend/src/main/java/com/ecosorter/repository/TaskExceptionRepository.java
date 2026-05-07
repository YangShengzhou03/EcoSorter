package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.TaskException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskExceptionRepository extends BaseMapper<TaskException> {
    
    @Select("SELECT * FROM task_exceptions WHERE task_id = #{taskId} AND status = #{status}")
    Optional<TaskException> findByTaskIdAndStatus(Long taskId, String status);
    
    @Select("SELECT * FROM task_exceptions WHERE status = #{status}")
    List<TaskException> findByStatus(String status);
    
    default TaskException save(TaskException exception) {
        if (exception.getId() == null) {
            insert(exception);
        } else {
            updateById(exception);
        }
        return exception;
    }
}
