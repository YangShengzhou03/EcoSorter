package com.ecosorter.service;

import com.ecosorter.dto.ComplaintProcessRequest;
import com.ecosorter.dto.ComplaintResponse;
import com.ecosorter.dto.ComplaintSubmitRequest;
import com.ecosorter.model.Classification;
import com.ecosorter.model.Complaint;
import com.ecosorter.model.User;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.ComplaintRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {
    
    private final ComplaintRepository complaintRepository;
    private final ClassificationRepository classificationRepository;
    private final UserRepository userRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    
    public ComplaintService(ComplaintRepository complaintRepository,
                           ClassificationRepository classificationRepository,
                           UserRepository userRepository,
                           WasteCategoryRepository wasteCategoryRepository) {
        this.complaintRepository = complaintRepository;
        this.classificationRepository = classificationRepository;
        this.userRepository = userRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
    }
    
    @Transactional
    public ComplaintResponse submitComplaint(Long userId, ComplaintSubmitRequest request) {
        Classification classification = classificationRepository.findById(Long.parseLong(request.getClassificationId()))
                .orElseThrow(() -> new RuntimeException("Classification not found"));
        
        if (!classification.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to submit complaint for this record");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Complaint complaint = new Complaint();
        complaint.setUser(user);
        complaint.setClassification(classification);
        complaint.setType(request.getType());
        complaint.setDescription(request.getDescription());
        complaint.setStatus("pending");
        
        Complaint savedComplaint = complaintRepository.save(complaint);
        return convertToResponse(savedComplaint);
    }
    
    @Transactional(readOnly = true)
    public List<ComplaintResponse> getUserComplaints(Long userId) {
        List<Complaint> complaints = complaintRepository.findByUserId(userId);
        return complaints.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getAllComplaints(String status, Pageable pageable) {
        Page<Complaint> complaints = complaintRepository.findAllWithStatus(status, pageable);
        return complaints.map(this::convertToResponse);
    }
    
    @Transactional
    public ComplaintResponse processComplaint(Long complaintId, Long adminId, ComplaintProcessRequest request) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        complaint.setStatus(request.getStatus());
        complaint.setAdmin(admin);
        complaint.setAdminResponse(request.getAdminResponse());
        complaint.setProcessedAt(LocalDateTime.now());
        
        Complaint savedComplaint = complaintRepository.save(complaint);
        
        if ("resolved".equals(request.getStatus())) {
            adjustClassificationPoints(savedComplaint);
        }
        
        return convertToResponse(savedComplaint);
    }
    
    @Transactional
    public void deleteComplaint(Long complaintId, Long userId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        
        if (!complaint.getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this complaint");
        }
        
        complaintRepository.delete(complaint);
    }
    
    @Transactional(readOnly = true)
    public long getPendingCount() {
        return complaintRepository.countByStatus("pending");
    }
    
    private void adjustClassificationPoints(Complaint complaint) {
        Classification classification = complaint.getClassification();
        if ("misclassification".equals(complaint.getType()) || "points".equals(complaint.getType())) {
            classification.setNotes(classification.getNotes() + "\n\n申诉已处理: " + complaint.getAdminResponse());
            classificationRepository.save(classification);
        }
    }
    
    private ComplaintResponse convertToResponse(Complaint complaint) {
        ComplaintResponse response = new ComplaintResponse();
        response.setId(complaint.getId());
        response.setClassificationId(complaint.getClassification().getId().toString());
        
        WasteCategory category = wasteCategoryRepository.findById(complaint.getClassification().getWasteCategoryId()).orElse(null);
        response.setCategoryName(category != null ? category.getName() : "unknown");
        
        response.setType(complaint.getType());
        response.setTypeText(getTypeText(complaint.getType()));
        response.setDescription(complaint.getDescription());
        response.setStatus(complaint.getStatus());
        response.setStatusText(getStatusText(complaint.getStatus()));
        
        if (complaint.getAdmin() != null) {
            response.setAdminName(complaint.getAdmin().getUsername());
        }
        
        response.setAdminResponse(complaint.getAdminResponse());
        response.setProcessedAt(complaint.getProcessedAt());
        response.setCreatedAt(complaint.getCreatedAt());
        
        return response;
    }
    
    private String getTypeText(String type) {
        switch (type) {
            case "misclassification":
                return "分类错误";
            case "weight":
                return "重量争议";
            case "points":
                return "积分争议";
            case "other":
                return "其他";
            default:
                return type;
        }
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case "pending":
                return "待处理";
            case "processing":
                return "处理中";
            case "resolved":
                return "已解决";
            case "rejected":
                return "已驳回";
            default:
                return status;
        }
    }
}
