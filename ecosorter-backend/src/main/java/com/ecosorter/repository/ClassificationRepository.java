package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Classification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassificationRepository extends BaseMapper<Classification> {
    
    @Select("SELECT * FROM classifications ORDER BY created_at DESC LIMIT 10")
    List<Classification> findTop10ByOrderByCreatedAtDesc();
    
    @Select("SELECT * FROM classifications ORDER BY created_at DESC LIMIT 20")
    List<Classification> findTop20ByOrderByCreatedAtDesc();
    
    @Select("SELECT * FROM classifications")
    List<Classification> findAll();
    
    default Classification save(Classification classification) {
        if (classification.getId() == null) {
            insert(classification);
        } else {
            updateById(classification);
        }
        return classification;
    }
    
    default long count() {
        return selectCount(null);
    }
}
