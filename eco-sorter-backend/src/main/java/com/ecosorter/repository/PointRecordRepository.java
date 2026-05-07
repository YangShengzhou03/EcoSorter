package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.PointRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PointRecordRepository extends BaseMapper<PointRecord> {
    
    default PointRecord save(PointRecord record) {
        if (record.getId() == null) {
            insert(record);
        } else {
            updateById(record);
        }
        return record;
    }
    
    @Select("SELECT * FROM point_records WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<PointRecord> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM point_records WHERE user_id = #{userId} AND type = #{type} ORDER BY created_at DESC")
    List<PointRecord> findByUserIdAndType(@Param("userId") Long userId, @Param("type") String type);
}
