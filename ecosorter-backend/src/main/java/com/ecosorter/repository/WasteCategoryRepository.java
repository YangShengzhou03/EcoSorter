package com.ecosorter.repository;

import com.ecosorter.model.WasteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WasteCategoryRepository extends JpaRepository<WasteCategory, Long> {
    
    Optional<WasteCategory> findByName(String name);
    
    List<WasteCategory> findByIsActive(Boolean isActive);
}
