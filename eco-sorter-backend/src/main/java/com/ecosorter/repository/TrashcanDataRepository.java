package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.TrashcanData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrashcanDataRepository extends BaseMapper<TrashcanData> {
    
    @Select("SELECT * FROM trashcan_data WHERE device_id = #{deviceId}")
    TrashcanData findByDeviceId(String deviceId);
    
    @Select("SELECT * FROM trashcan_data WHERE auth_token = #{authToken}")
    TrashcanData findByAuthToken(String authToken);
    
    @Select("SELECT * FROM trashcan_data WHERE status = #{status}")
    List<TrashcanData> findByStatus(String status);
    
    @Select("SELECT * FROM trashcan_data WHERE capacity_level >= threshold")
    List<TrashcanData> findFullTrashcans();
    
    @Select("SELECT COUNT(*) FROM trashcan_data")
    long count();
    
    default List<TrashcanData> findAll() {
        return selectList(null);
    }
    
    default TrashcanData save(TrashcanData trashcan) {
        if (trashcan.getId() == null) {
            insert(trashcan);
        } else {
            updateById(trashcan);
        }
        return trashcan;
    }
}
