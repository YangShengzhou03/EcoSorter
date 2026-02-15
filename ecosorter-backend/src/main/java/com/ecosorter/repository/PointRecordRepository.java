package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.PointRecord;
import org.apache.ibatis.annotations.Mapper;

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
}
