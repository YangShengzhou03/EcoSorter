package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NoticeRepository extends BaseMapper<Notice> {
    
    @Select("SELECT * FROM notices WHERE status = 'published'")
    List<Notice> findPublished();
    
    @Select("SELECT * FROM notices")
    List<Notice> findAll();
    
    default Optional<Notice> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }
    
    default Notice save(Notice notice) {
        if (notice.getId() == null) {
            insert(notice);
        } else {
            updateById(notice);
        }
        return notice;
    }
    
    default void deleteById(Long id) {
        BaseMapper.super.deleteById(id);
    }
}
