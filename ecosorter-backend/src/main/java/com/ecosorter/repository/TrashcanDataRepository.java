package com.ecosorter.repository;

import com.ecosorter.model.TrashcanData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrashcanDataRepository extends JpaRepository<TrashcanData, Long> {
    
    Optional<TrashcanData> findByDeviceId(String deviceId);
    
    List<TrashcanData> findByStatus(String status);
    
    List<TrashcanData> findByUserId(Long userId);
    
    List<TrashcanData> findByCapacityLevelGreaterThan(Integer level);
    
    @Query("SELECT t FROM TrashcanData t WHERE t.capacityLevel >= t.threshold")
    List<TrashcanData> findFullTrashcans();
    
    @Query("SELECT t FROM TrashcanData t WHERE t.status = 'offline'")
    List<TrashcanData> findOfflineTrashcans();
}
