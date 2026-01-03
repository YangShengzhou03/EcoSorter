package com.ecosorter.repository;

import com.ecosorter.model.Classification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Classification repository interface
 */
@Repository
public interface ClassificationRepository extends MongoRepository<Classification, String> {
    
    List<Classification> findByUserId(String userId);
    
    Page<Classification> findByUserId(String userId, Pageable pageable);
    
    List<Classification> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Classification> findByPredictedCategory(String predictedCategory);
    
    List<Classification> findByConfidenceScoreBetween(Double minScore, Double maxScore);
    
    List<Classification> findByUserVerified(Boolean userVerified);
    
    List<Classification> findByVerificationStatus(Classification.VerificationStatus verificationStatus);
    
    @Query("{'createdAt': {'$gte': ?0, '$lte': ?1}}")
    List<Classification> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'userId': ?0, 'createdAt': {'$gte': ?1, '$lte': ?2}}")
    List<Classification> findByUserIdAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query(value = "{'userId': ?0}", count = true)
    long countByUserId(String userId);
    
    @Query("{'userId': ?0, 'userVerified': true}")
    List<Classification> findVerifiedByUserId(String userId);
    
    @Query("{'predictedCategory': ?0, 'createdAt': {'$gte': ?1, '$lte': ?2}}")
    List<Classification> findByCategoryAndDateRange(String category, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Classification> findByAiModel(String aiModel);
    
    List<Classification> findByTagsContaining(String tag);
    
    @Query("{'confidenceScore': {'$gte': ?0}}")
    List<Classification> findByConfidenceScoreGreaterThanEqual(Double confidenceScore);
    
    @Query("{'processingTime': {'$lte': ?0}}")
    List<Classification> findByProcessingTimeLessThanEqual(Long processingTime);
    
    Optional<Classification> findTopByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Classification> findTop10ByUserIdOrderByCreatedAtDesc(String userId);
}