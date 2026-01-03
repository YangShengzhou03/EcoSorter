package com.ecosorter.repository;

import com.ecosorter.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User repository interface
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByStatus(User.UserStatus status);
    
    List<User> findByLastLoginAtAfter(LocalDateTime date);
    
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'lockUntil': {'$gt': ?0}}")
    List<User> findLockedUsers(LocalDateTime now);
    
    @Query("{'emailVerified': false}")
    List<User> findUnverifiedUsers();
    
    @Query(value = "{}", fields = "{'password': 0, 'passwordResetToken': 0, 'passwordResetExpires': 0, 'emailVerificationToken': 0, 'twoFactorSecret': 0}")
    List<User> findAllWithoutSensitiveData();
    
    long countByStatus(User.UserStatus status);
    
    long countByRole(User.UserRole role);
    
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}