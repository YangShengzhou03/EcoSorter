package com.ecosorter.repository;

import com.ecosorter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(User.UserRole role);

    List<User> findByIsActive(Boolean isActive);

    List<User> findByLastLoginAfter(LocalDateTime date);
    
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT u FROM User u WHERE u.lockUntil > :now")
    List<User> findLockedUsers(@Param("now") LocalDateTime now);
    
    @Query("SELECT u FROM User u WHERE u.emailVerified = false")
    List<User> findUnverifiedUsers();
    
    @Query("SELECT u FROM User u")
    List<User> findAllWithoutSensitiveData();

    long countByIsActive(Boolean isActive);

    long countByRole(User.UserRole role);
    
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
