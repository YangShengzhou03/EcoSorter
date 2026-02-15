package com.ecosorter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("user_statistics")
public class UserStatistics {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer totalClassifications = 0;
    
    private Integer correctClassifications = 0;
    
    private BigDecimal totalWasteWeight = BigDecimal.ZERO;
    
    private BigDecimal carbonSaved = BigDecimal.ZERO;
    
    private Integer totalPoints = 0;

    private Integer streakDays = 0;

    private Integer longestStreak = 0;

    private Integer weeklyGoal = 50;

    private Integer monthlyGoal = 200;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    public UserStatistics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
