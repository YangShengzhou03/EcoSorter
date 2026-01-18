package com.ecosorter.repository;

import com.ecosorter.model.Classification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long> {
    
    List<Classification> findByUserId(Long userId);
    
    Page<Classification> findByUserId(Long userId, Pageable pageable);
    
    long countByUserId(Long userId);
    
    List<Classification> findTop10ByOrderByCreatedAtDesc();
    
    List<Classification> findTop20ByOrderByCreatedAtDesc();
}
