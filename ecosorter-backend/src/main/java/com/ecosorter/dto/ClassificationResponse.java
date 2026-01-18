package com.ecosorter.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ClassificationResponse {
    
    private String id;
    
    private String userId;
    
    private String username;
    
    private WasteCategoryResponse category;
    
    private String categoryName;
    
    private Double confidence;
    
    private String result;
    
    private String status;
    
    private String imageUrl;
    
    private Long processingTime;
    
    private String model;
    
    private LocalDateTime createdAt;
    
    private String notes;
    
    private String disposalInstructions;
    
    private Integer environmentalImpact;
    
    private List<String> alternatives;
    
    private String userFeedback;
    
    private Integer points;
    
    private ClassificationMetrics metrics;
    
    private ProcessingDetails processingDetails;

    public ClassificationResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WasteCategoryResponse getCategory() {
        return category;
    }

    public void setCategory(WasteCategoryResponse category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDisposalInstructions() {
        return disposalInstructions;
    }

    public void setDisposalInstructions(String disposalInstructions) {
        this.disposalInstructions = disposalInstructions;
    }

    public Integer getEnvironmentalImpact() {
        return environmentalImpact;
    }

    public void setEnvironmentalImpact(Integer environmentalImpact) {
        this.environmentalImpact = environmentalImpact;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ClassificationMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(ClassificationMetrics metrics) {
        this.metrics = metrics;
    }

    public ProcessingDetails getProcessingDetails() {
        return processingDetails;
    }

    public void setProcessingDetails(ProcessingDetails processingDetails) {
        this.processingDetails = processingDetails;
    }
    
    public static class ClassificationMetrics {
        private Double accuracy;
        
        private Double precision;
        
        private Double recall;
        
        private Double f1Score;

        public ClassificationMetrics() {
        }

        public Double getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(Double accuracy) {
            this.accuracy = accuracy;
        }

        public Double getPrecision() {
            return precision;
        }

        public void setPrecision(Double precision) {
            this.precision = precision;
        }

        public Double getRecall() {
            return recall;
        }

        public void setRecall(Double recall) {
            this.recall = recall;
        }

        public Double getF1Score() {
            return f1Score;
        }

        public void setF1Score(Double f1Score) {
            this.f1Score = f1Score;
        }
    }
    
    public static class ProcessingDetails {
        private Long preprocessingTime;
        
        private Long inferenceTime;
        
        private Long postprocessingTime;
        
        private Integer stages;

        public ProcessingDetails() {
        }

        public Long getPreprocessingTime() {
            return preprocessingTime;
        }

        public void setPreprocessingTime(Long preprocessingTime) {
            this.preprocessingTime = preprocessingTime;
        }

        public Long getInferenceTime() {
            return inferenceTime;
        }

        public void setInferenceTime(Long inferenceTime) {
            this.inferenceTime = inferenceTime;
        }

        public Long getPostprocessingTime() {
            return postprocessingTime;
        }

        public void setPostprocessingTime(Long postprocessingTime) {
            this.postprocessingTime = postprocessingTime;
        }

        public Integer getStages() {
            return stages;
        }

        public void setStages(Integer stages) {
            this.stages = stages;
        }
    }
}
