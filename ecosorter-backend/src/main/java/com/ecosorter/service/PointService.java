package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.PointRecordResponse;
import com.ecosorter.enums.PointType;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.PointRecord;
import com.ecosorter.model.User;
import com.ecosorter.model.UserStatistics;
import com.ecosorter.repository.PointRecordRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PointService {
    
    private final PointRecordRepository pointRecordRepository;
    private final UserRepository userRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    
    public PointService(PointRecordRepository pointRecordRepository,
                       UserRepository userRepository,
                       UserStatisticsRepository userStatisticsRepository) {
        this.pointRecordRepository = pointRecordRepository;
        this.userRepository = userRepository;
        this.userStatisticsRepository = userStatisticsRepository;
    }
    
    @Transactional
    public PointRecordResponse addPoints(Long userId, Integer points, String type, Long referenceId, String description) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(PointType.valueOf(type.toUpperCase()));
        record.setReferenceId(referenceId);
        record.setDescription(description);
        record.setCreatedAt(LocalDateTime.now());
        
        pointRecordRepository.insert(record);
        
        updateUserPoints(userId);
        
        return toResponse(record);
    }
    
    @Transactional
    public PointRecordResponse deductPoints(Long userId, Integer points, String type, Long referenceId, String description) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        
        Integer currentPoints = user.getCurrentPoints();
        
        if (currentPoints < points) {
            throw new BadRequestException("积分不足");
        }
        
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(-points);
        record.setType(PointType.valueOf(type.toUpperCase()));
        record.setReferenceId(referenceId);
        record.setDescription(description);
        record.setCreatedAt(LocalDateTime.now());
        
        pointRecordRepository.insert(record);
        
        updateUserPoints(userId);
        
        return toResponse(record);
    }
    
    public List<PointRecordResponse> getUserPointRecords(Long userId) {
        List<PointRecord> records = pointRecordRepository.selectList(
                new LambdaQueryWrapper<PointRecord>()
                        .eq(PointRecord::getUserId, userId)
                        .orderByDesc(PointRecord::getCreatedAt)
        );
        return toResponses(records);
    }
    
    public IPage<PointRecordResponse> getUserPointRecords(Long userId, int page, int size) {
        long current = page <= 0 ?1 : page;
        Page<PointRecord> mpPage = new Page<>(current, size);
        LambdaQueryWrapper<PointRecord> wrapper = new LambdaQueryWrapper<PointRecord>()
                .eq(PointRecord::getUserId, userId)
                .orderByDesc(PointRecord::getCreatedAt);
        IPage<PointRecord> recordPage = pointRecordRepository.selectPage(mpPage, wrapper);

        Page<PointRecordResponse> responsePage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        responsePage.setRecords(toResponses(recordPage.getRecords()));
        return responsePage;
    }
    
    public Integer getUserTotalPoints(Long userId) {
        QueryWrapper<PointRecord> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(SUM(points), 0) as totalPoints").eq("user_id", userId);
        List<Map<String, Object>> rows = pointRecordRepository.selectMaps(wrapper);
        if (rows.isEmpty()) {
            return 0;
        }
        Object value = rows.get(0).get("totalPoints");
        return value == null ? 0 : ((Number) value).intValue();
    }
    
    private void updateUserPoints(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return;
        }
        
        Integer totalPoints = getUserTotalPoints(userId);
        user.setCurrentPoints(totalPoints);
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.updateById(user);
        
        UserStatistics statistics = userStatisticsRepository.selectOne(
                new LambdaQueryWrapper<UserStatistics>().eq(UserStatistics::getUserId, userId)
        );
        if (statistics == null) {
            statistics = new UserStatistics();
            statistics.setUserId(userId);
            statistics.setCreatedAt(LocalDateTime.now());
        }
        
        statistics.setTotalPoints(totalPoints);
        statistics.setUpdatedAt(LocalDateTime.now());

        if (statistics.getId() == null) {
            userStatisticsRepository.insert(statistics);
        } else {
            userStatisticsRepository.updateById(statistics);
        }
    }
    
    private List<PointRecordResponse> toResponses(List<PointRecord> records) {
        if (records == null || records.isEmpty()) {
            return List.of();
        }
        
        int balance = getUserTotalPoints(records.get(0).getUserId());
        List<PointRecordResponse> responses = new ArrayList<>();
        
        for (int i = records.size() - 1; i >= 0; i--) {
            PointRecord record = records.get(i);
            PointRecordResponse response = toResponse(record);
            response.setPointsChange(record.getPoints());
            response.setBalance(balance);
            responses.add(0, response);
            balance -= record.getPoints();
        }
        
        return responses;
    }
    
    private PointRecordResponse toResponse(PointRecord record) {
        PointRecordResponse response = new PointRecordResponse();
        response.setId(record.getId());
        response.setPoints(record.getPoints());
        response.setType(record.getType().name().toLowerCase());
        response.setReferenceId(record.getReferenceId());
        response.setDescription(record.getDescription());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}
