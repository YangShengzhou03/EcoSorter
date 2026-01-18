package com.ecosorter.service;

import com.ecosorter.dto.PointRecordResponse;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.PointRecord;
import com.ecosorter.model.User;
import com.ecosorter.model.UserStatistics;
import com.ecosorter.repository.PointRecordRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointService {
    
    @Autowired
    private PointRecordRepository pointRecordRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserStatisticsRepository userStatisticsRepository;
    
    @Transactional
    public PointRecordResponse addPoints(Long userId, Integer points, String type, Long referenceId, String description) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(type);
        record.setReferenceId(referenceId);
        record.setDescription(description);
        
        record = pointRecordRepository.save(record);
        
        updateUserPoints(userId);
        
        return toResponse(record);
    }
    
    @Transactional
    public PointRecordResponse deductPoints(Long userId, Integer points, String type, Long referenceId, String description) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        Integer currentPoints = pointRecordRepository.getTotalPointsByUserId(userId);
        if (currentPoints == null) {
            currentPoints = 0;
        }
        
        if (currentPoints < points) {
            throw new BadRequestException("积分不足");
        }
        
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(-points);
        record.setType(type);
        record.setReferenceId(referenceId);
        record.setDescription(description);
        
        record = pointRecordRepository.save(record);
        
        updateUserPoints(userId);
        
        return toResponse(record);
    }
    
    public List<PointRecordResponse> getUserPointRecords(Long userId) {
        List<PointRecord> records = pointRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return records.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public Page<PointRecordResponse> getUserPointRecords(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PointRecord> records = pointRecordRepository.findAllByUserId(userId, pageable);
        return records.map(this::toResponse);
    }
    
    public Integer getUserTotalPoints(Long userId) {
        Integer points = pointRecordRepository.getTotalPointsByUserId(userId);
        return points != null ? points : 0;
    }
    
    private void updateUserPoints(Long userId) {
        UserStatistics statistics = userStatisticsRepository.findByUserId(userId).orElse(null);
        if (statistics == null) {
            statistics = new UserStatistics();
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
            statistics.setUser(user);
        }
        
        Integer totalPoints = pointRecordRepository.getTotalPointsByUserId(userId);
        statistics.setTotalPoints(totalPoints != null ? totalPoints : 0);
        
        userStatisticsRepository.save(statistics);
    }
    
    private PointRecordResponse toResponse(PointRecord record) {
        return new PointRecordResponse(
            record.getId(),
            record.getPoints(),
            record.getType(),
            record.getReferenceId(),
            record.getDescription(),
            record.getCreatedAt()
        );
    }
}
