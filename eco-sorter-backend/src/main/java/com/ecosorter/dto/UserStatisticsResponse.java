package com.ecosorter.dto;

import java.math.BigDecimal;

public class UserStatisticsResponse {
    
    private Long totalCount;
    private BigDecimal totalWeight;
    private Long totalPoints;
    private BigDecimal carbonSaved;
    private Integer correctClassifications;
    private Integer accuracyRate;
    private Integer streakDays;
    private Integer weeklyGoal;
    private Integer monthlyGoal;

    public UserStatisticsResponse() {
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public BigDecimal getCarbonSaved() {
        return carbonSaved;
    }

    public void setCarbonSaved(BigDecimal carbonSaved) {
        this.carbonSaved = carbonSaved;
    }

    public Integer getCorrectClassifications() {
        return correctClassifications;
    }

    public void setCorrectClassifications(Integer correctClassifications) {
        this.correctClassifications = correctClassifications;
    }

    public Integer getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(Integer accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public Integer getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(Integer streakDays) {
        this.streakDays = streakDays;
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
}
