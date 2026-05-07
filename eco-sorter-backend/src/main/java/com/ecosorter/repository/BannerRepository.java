package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BannerRepository extends BaseMapper<Banner> {
    
    @Select("SELECT * FROM banners WHERE target = #{target} ORDER BY sort_order ASC")
    List<Banner> findByTargetOrderBySortOrderAsc(String target);
    
    @Select("SELECT * FROM banners ORDER BY sort_order ASC")
    List<Banner> findAllByOrderBySortOrderAsc();
    
    @Select("SELECT * FROM banners")
    List<Banner> findAll();
    
    default Banner save(Banner banner) {
        if (banner.getId() == null) {
            insert(banner);
        } else {
            updateById(banner);
        }
        return banner;
    }
    
    default void deleteById(Long id) {
        BaseMapper.super.deleteById(id);
    }
}
