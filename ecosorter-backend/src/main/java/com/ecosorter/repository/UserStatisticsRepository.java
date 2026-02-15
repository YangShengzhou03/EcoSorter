package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.UserStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserStatisticsRepository extends BaseMapper<UserStatistics> {
    
    @Select("SELECT * FROM user_statistics WHERE user_id = #{userId}")
    Optional<UserStatistics> findByUserId(Long userId);
    
    default UserStatistics save(UserStatistics statistics) {
        if (statistics.getId() == null) {
            insert(statistics);
        } else {
            updateById(statistics);
        }
        return statistics;
    }
}
