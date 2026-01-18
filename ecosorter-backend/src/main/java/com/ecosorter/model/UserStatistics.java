package com.ecosorter.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_statistics")
public class UserStatistics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
    
    @Column(name = "total_classifications")
    private Integer totalClassifications = 0;
    
    @Column(name = "correct_classifications")
    private Integer correctClassifications = 0;
    
    @Column(name = "total_waste_weight", precision = 10, scale = 2)
    private BigDecimal totalWasteWeight = BigDecimal.ZERO;
    
    @Column(name = "carbon_saved", precision = 10, scale = 2)
    private BigDecimal carbonSaved = BigDecimal.ZERO;
    
    @Column(name = "total_points")
    private Integer totalPoints = 0;

    @Column(name = "streak_days")
    private Integer streakDays = 0;

    @Column(name = "longest_streak")
    private Integer longestStreak = 0;

    @Column(name = "weekly_goal")
    private Integer weeklyGoal = 50;

    @Column(name = "monthly_goal")
    private Integer monthlyGoal = 200;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserStatistics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotalClassifications() {
        return totalClassifications;
    }

    public void setTotalClassifications(Integer totalClassifications) {
        this.totalClassifications = totalClassifications;
    }

    public Integer getCorrectClassifications() {
        return correctClassifications;
    }

    public void setCorrectClassifications(Integer correctClassifications) {
        this.correctClassifications = correctClassifications;
    }

    public BigDecimal getTotalWasteWeight() {
        return totalWasteWeight;
    }

    public void setTotalWasteWeight(BigDecimal totalWasteWeight) {
        this.totalWasteWeight = totalWasteWeight;
    }

    public BigDecimal getCarbonSaved() {
        return carbonSaved;
    }

    public void setCarbonSaved(BigDecimal carbonSaved) {
        this.carbonSaved = carbonSaved;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(Integer streakDays) {
        this.streakDays = streakDays;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Integer getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(Integer weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public Integer getMonthlyGoal() {
        return monthlyGoal;
    }

    public void setMonthlyGoal(Integer monthlyGoal) {
        this.monthlyGoal = monthlyGoal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
