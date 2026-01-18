package com.ecosorter.repository;

import com.ecosorter.model.PointRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRecordRepository extends JpaRepository<PointRecord, Long> {
    
    List<PointRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Page<PointRecord> findAllByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT SUM(pr.points) FROM PointRecord pr WHERE pr.userId = :userId")
    Integer getTotalPointsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(pr) FROM PointRecord pr WHERE pr.userId = :userId AND pr.points > 0")
    Long countEarnedPoints(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(pr) FROM PointRecord pr WHERE pr.userId = :userId AND pr.points < 0")
    Long countSpentPoints(@Param("userId") Long userId);
}
