package com.ecosorter.service;

import com.ecosorter.dto.UserStatisticsResponse;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.User;
import com.ecosorter.model.UserStatistics;
import com.ecosorter.repository.UserStatisticsRepository;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticsService {
    
    private final UserStatisticsRepository userStatisticsRepository;
    private final ClassificationRepository classificationRepository;
    private final UserRepository userRepository;
    
    public UserStatisticsService(UserStatisticsRepository userStatisticsRepository,
                                  ClassificationRepository classificationRepository,
                                  UserRepository userRepository) {
        this.userStatisticsRepository = userStatisticsRepository;
        this.classificationRepository = classificationRepository;
        this.userRepository = userRepository;
    }
    
    public UserStatisticsResponse getUserStatistics(Long userId) {
        UserStatistics statistics = userStatisticsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultStatistics(userId));
        
        UserStatisticsResponse response = new UserStatisticsResponse();
        response.setTotalCount(statistics.getTotalClassifications() != null ? statistics.getTotalClassifications().longValue() : 0L);
        response.setTotalWeight(statistics.getTotalWasteWeight());
        response.setTotalPoints(statistics.getTotalPoints() != null ? statistics.getTotalPoints().longValue() : 0L);
        response.setCarbonSaved(statistics.getCarbonSaved());
        response.setStreakDays(statistics.getStreakDays());
        response.setWeeklyGoal(statistics.getWeeklyGoal());
        response.setMonthlyGoal(statistics.getMonthlyGoal());
        response.setCorrectClassifications(statistics.getCorrectClassifications() != null ? statistics.getCorrectClassifications() : 0);
        
        if (statistics.getTotalClassifications() != null && statistics.getTotalClassifications() > 0) {
            int accuracyRate = (int) ((statistics.getCorrectClassifications() * 100.0) / statistics.getTotalClassifications());
            response.setAccuracyRate(accuracyRate);
        } else {
            response.setAccuracyRate(0);
        }
        
        return response;
    }
    
    private UserStatistics createDefaultStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        UserStatistics statistics = new UserStatistics();
        statistics.setUser(user);
        statistics.setTotalClassifications(0);
        statistics.setCorrectClassifications(0);
        statistics.setTotalWasteWeight(java.math.BigDecimal.ZERO);
        statistics.setCarbonSaved(java.math.BigDecimal.ZERO);
        statistics.setStreakDays(0);
        statistics.setLongestStreak(0);
        statistics.setWeeklyGoal(50);
        statistics.setMonthlyGoal(200);
        return userStatisticsRepository.save(statistics);
    }
}
