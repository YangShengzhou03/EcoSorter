package com.ecosorter.repository;

import com.ecosorter.model.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    @Query("SELECT c FROM Complaint c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    List<Complaint> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT c FROM Complaint c WHERE c.status = :status ORDER BY c.createdAt DESC")
    Page<Complaint> findByStatus(@Param("status") String status, Pageable pageable);
    
    @Query("SELECT c FROM Complaint c WHERE (:status IS NULL OR c.status = :status) ORDER BY c.createdAt DESC")
    Page<Complaint> findAllWithStatus(@Param("status") String status, Pageable pageable);
    
    @Query("SELECT c FROM Complaint c WHERE c.admin.id = :adminId ORDER BY c.createdAt DESC")
    Page<Complaint> findByAdminId(@Param("adminId") Long adminId, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.status = :status")
    long countByStatus(@Param("status") String status);
}
