package com.ecosorter.repository;

import com.ecosorter.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    
    List<Banner> findByIsActiveOrderBySortOrderAsc(Boolean isActive);
    
    List<Banner> findByIsActiveAndTargetOrderBySortOrderAsc(Boolean isActive, String target);
}
