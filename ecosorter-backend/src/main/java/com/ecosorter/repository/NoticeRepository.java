package com.ecosorter.repository;

import com.ecosorter.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    @Query("SELECT n FROM Notice n WHERE n.status = :status ORDER BY n.createdAt DESC")
    List<Notice> findByStatus(@Param("status") String status);
    
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Notice> searchNotices(@Param("keyword") String keyword);
}
