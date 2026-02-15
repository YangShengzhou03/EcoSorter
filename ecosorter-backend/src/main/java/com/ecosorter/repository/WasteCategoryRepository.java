package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.WasteCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WasteCategoryRepository extends BaseMapper<WasteCategory> {
    
    @Select("SELECT * FROM waste_categories")
    List<WasteCategory> findAll();
    
    default WasteCategory save(WasteCategory category) {
        if (category.getId() == null) {
            insert(category);
        } else {
            updateById(category);
        }
        return category;
    }
    
    default void deleteById(Long id) {
        BaseMapper.super.deleteById(id);
    }
}
