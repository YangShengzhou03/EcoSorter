package com.ecosorter.repository;

import com.ecosorter.model.Order;
import com.ecosorter.model.Product;
import com.ecosorter.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
    List<Order> findByUserAndProduct(User user, Product product);
    Page<Order> findByUserAndStatus(User user, String status, Pageable pageable);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(o.quantity), 0) FROM Order o WHERE o.product.id = :productId")
    Integer getTotalPurchasedByProductId(@Param("productId") Long productId);
}
