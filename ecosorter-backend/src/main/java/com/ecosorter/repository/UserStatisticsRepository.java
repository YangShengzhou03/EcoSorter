package com.ecosorter.repository;

import com.ecosorter.model.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
    
    Optional<UserStatistics> findByUserId(Long userId);
    
    @Query("SELECT u FROM UserStatistics u ORDER BY u.totalClassifications DESC")
    java.util.List<UserStatistics> findTopClassifiers();
    
    @Query("SELECT u FROM UserStatistics u WHERE u.streakDays > 0 ORDER BY u.streakDays DESC")
    java.util.List<UserStatistics> findActiveUsers();
}
