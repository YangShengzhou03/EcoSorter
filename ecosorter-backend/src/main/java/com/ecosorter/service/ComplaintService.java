package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.ComplaintProcessRequest;
import com.ecosorter.dto.ComplaintResponse;
import com.ecosorter.dto.ComplaintSubmitRequest;
import com.ecosorter.enums.ComplaintStatus;
import com.ecosorter.enums.ComplaintType;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.Classification;
import com.ecosorter.model.Complaint;
import com.ecosorter.model.User;
import com.ecosorter.model.WasteCategory;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.ComplaintRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
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
        Classification classification = classificationRepository.selectById(Long.parseLong(request.getClassificationId()));
        if (classification == null) {
            throw new ResourceNotFoundException("Classification not found");
        }
        
        if (!classification.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to submit complaint for this record");
        }
        
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        
        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setClassificationId(classification.getId());
        complaint.setType(ComplaintType.valueOf(request.getType().toUpperCase()));
        complaint.setDescription(request.getDescription());
        complaint.setStatus(ComplaintStatus.PENDING);
        
        LocalDateTime now = LocalDateTime.now();
        complaint.setCreatedAt(now);
        complaint.setUpdatedAt(now);
        complaintRepository.insert(complaint);

        return convertToResponse(complaint, classification, null, null, user);
    }
    
    @Transactional(readOnly = true)
    public List<ComplaintResponse> getUserComplaints(Long userId) {
        List<Complaint> complaints = complaintRepository.selectList(
                new LambdaQueryWrapper<Complaint>()
                        .eq(Complaint::getUserId, userId)
                        .orderByDesc(Complaint::getCreatedAt)
        );
        return toResponses(complaints);
    }
    
    @Transactional(readOnly = true)
    public IPage<ComplaintResponse> getAllComplaints(String status, int page, int pageSize) {
        Page<Complaint> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<Complaint>()
                .orderByDesc(Complaint::getCreatedAt);
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Complaint::getStatus, ComplaintStatus.valueOf(status.toUpperCase()));
        }
        IPage<Complaint> complaintPage = complaintRepository.selectPage(mpPage, wrapper);

        Page<ComplaintResponse> responsePage = new Page<>(complaintPage.getCurrent(), complaintPage.getSize(), complaintPage.getTotal());
        responsePage.setRecords(toResponses(complaintPage.getRecords()));
        return responsePage;
    }
    
    @Transactional
    public ComplaintResponse processComplaint(Long complaintId, Long adminId, ComplaintProcessRequest request) {
        Complaint complaint = complaintRepository.selectById(complaintId);
        if (complaint == null) {
            throw new ResourceNotFoundException("Complaint not found");
        }
        
        User admin = userRepository.selectById(adminId);
        if (admin == null) {
            throw new ResourceNotFoundException("Admin not found");
        }
        
        complaint.setStatus(ComplaintStatus.valueOf(request.getStatus().toUpperCase()));
        complaint.setAdminId(adminId);
        complaint.setAdminResponse(request.getAdminResponse());
        complaint.setProcessedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());
        
        complaintRepository.updateById(complaint);
        
        if (complaint.getStatus() == ComplaintStatus.RESOLVED) {
            adjustClassificationPoints(complaint);
        }
        
        Classification classification = complaint.getClassificationId() == null ? null : classificationRepository.selectById(complaint.getClassificationId());
        WasteCategory category = (classification == null || classification.getWasteCategoryId() == null)
                ? null
                : wasteCategoryRepository.selectById(classification.getWasteCategoryId());
        User user = complaint.getUserId() == null ? null : userRepository.selectById(complaint.getUserId());

        return convertToResponse(complaint, classification, category, admin, user);
    }
    
    @Transactional
    public void deleteComplaint(Long complaintId, Long userId) {
        Complaint complaint = complaintRepository.selectById(complaintId);
        if (complaint == null) {
            throw new ResourceNotFoundException("Complaint not found");
        }
        
        if (!complaint.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this complaint");
        }
        
        complaintRepository.deleteById(complaintId);
    }
    
    @Transactional(readOnly = true)
    public long getPendingCount() {
        return complaintRepository.selectCount(new LambdaQueryWrapper<Complaint>().eq(Complaint::getStatus, ComplaintStatus.PENDING));
    }
    
    private void adjustClassificationPoints(Complaint complaint) {
        if (complaint.getClassificationId() == null) {
            return;
        }
        Classification classification = classificationRepository.selectById(complaint.getClassificationId());
        if (classification == null) {
            return;
        }
        if (complaint.getType() == ComplaintType.MISCLASSIFICATION || complaint.getType() == ComplaintType.POINTS) {
            String notes = classification.getNotes() == null ? "" : classification.getNotes();
            classification.setNotes(notes + "\n\n申诉已处理: " + complaint.getAdminResponse());
            classification.setUpdatedAt(LocalDateTime.now());
            classificationRepository.updateById(classification);
        }
    }
    
    private List<ComplaintResponse> toResponses(List<Complaint> complaints) {
        if (complaints == null || complaints.isEmpty()) {
            return List.of();
        }

        List<Long> classificationIds = complaints.stream()
                .map(Complaint::getClassificationId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, Classification> classificationsById = classificationIds.isEmpty()
                ? Map.of()
                : classificationRepository.selectBatchIds(classificationIds).stream()
                        .collect(Collectors.toMap(Classification::getId, c -> c));

        List<Long> categoryIds = classificationsById.values().stream()
                .map(Classification::getWasteCategoryId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, WasteCategory> categoriesById = categoryIds.isEmpty()
                ? Map.of()
                : wasteCategoryRepository.selectBatchIds(categoryIds).stream()
                        .collect(Collectors.toMap(WasteCategory::getId, c -> c));

        List<Long> adminIds = complaints.stream()
                .map(Complaint::getAdminId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, User> adminsById = adminIds.isEmpty()
                ? Map.of()
                : userRepository.selectBatchIds(adminIds).stream()
                        .collect(Collectors.toMap(User::getId, u -> u));

        List<Long> userIds = complaints.stream()
                .map(Complaint::getUserId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, User> usersById = userIds.isEmpty()
                ? Map.of()
                : userRepository.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, u -> u));

        return complaints.stream()
                .map(complaint -> {
                    Classification classification = complaint.getClassificationId() == null ? null : classificationsById.get(complaint.getClassificationId());
                    WasteCategory category = (classification == null || classification.getWasteCategoryId() == null) ? null : categoriesById.get(classification.getWasteCategoryId());
                    User admin = complaint.getAdminId() == null ? null : adminsById.get(complaint.getAdminId());
                    User user = complaint.getUserId() == null ? null : usersById.get(complaint.getUserId());
                    return convertToResponse(complaint, classification, category, admin, user);
                })
                .collect(Collectors.toList());
    }

    private ComplaintResponse convertToResponse(Complaint complaint, Classification classification, WasteCategory category, User admin, User user) {
        ComplaintResponse response = new ComplaintResponse();
        response.setId(complaint.getId());
        response.setClassificationId(complaint.getClassificationId() != null ? complaint.getClassificationId().toString() : null);

        response.setCategoryName(category != null ? category.getName() : "unknown");
        
        response.setType(complaint.getType().name().toLowerCase());
        response.setTypeText(getTypeText(complaint.getType()));
        response.setDescription(complaint.getDescription());
        response.setStatus(complaint.getStatus().name().toLowerCase());
        response.setStatusText(getStatusText(complaint.getStatus()));
        
        if (user != null) {
            response.setUserName(user.getUsername());
        }
        
        if (admin != null) {
            response.setAdminName(admin.getUsername());
        }
        
        response.setAdminResponse(complaint.getAdminResponse());
        response.setProcessedAt(complaint.getProcessedAt());
        response.setCreatedAt(complaint.getCreatedAt());
        
        return response;
    }
    
    private String getTypeText(ComplaintType type) {
        switch (type) {
            case MISCLASSIFICATION:
                return "分类错误";
            case WEIGHT:
                return "重量争议";
            case POINTS:
                return "积分争议";
            case OTHER:
                return "其他";
            default:
                return type.name();
        }
    }
    
    private String getStatusText(ComplaintStatus status) {
        switch (status) {
            case PENDING:
                return "待处理";
            case PROCESSING:
                return "处理中";
            case RESOLVED:
                return "已解决";
            case REJECTED:
                return "已驳回";
            default:
                return status.name();
        }
    }
}
