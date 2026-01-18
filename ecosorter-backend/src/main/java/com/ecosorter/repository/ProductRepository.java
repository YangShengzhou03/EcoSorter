package com.ecosorter.repository;

import com.ecosorter.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
    List<Product> findByCategory(String category);
    List<Product> findByStatusAndCategory(String status, String category);
    
    Page<Product> findByStatus(String status, Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByStatusAndCategory(String status, String category, Pageable pageable);
}
