package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Complaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ComplaintRepository extends BaseMapper<Complaint> {
    
    @Select("SELECT * FROM complaints WHERE user_id = #{userId}")
    List<Complaint> findByUserId(Long userId);
    
    @Select("SELECT * FROM complaints")
    List<Complaint> findAll();
    
    default Complaint save(Complaint complaint) {
        if (complaint.getId() == null) {
            insert(complaint);
        } else {
            updateById(complaint);
        }
        return complaint;
    }
    
    default void deleteById(Long id) {
        BaseMapper.super.deleteById(id);
    }
}
