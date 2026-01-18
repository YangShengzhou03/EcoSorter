package com.ecosorter.repository;

import com.ecosorter.model.WasteCategoryExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteCategoryExampleRepository extends JpaRepository<WasteCategoryExample, Long> {
    
    List<WasteCategoryExample> findByWasteCategoryId(Long categoryId);
    
    void deleteByWasteCategoryId(Long categoryId);
}
