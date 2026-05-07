package com.ecosorter.service;

import com.ecosorter.dto.*;
import com.ecosorter.enums.PointType;
import com.ecosorter.model.PointRecord;
import com.ecosorter.model.User;
import com.ecosorter.model.UserStatistics;
import com.ecosorter.repository.PointRecordRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserAdminService {
    
    private final UserRepository userRepository;
    private final PointRecordRepository pointRecordRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    
    public UserAdminService(UserRepository userRepository,
                           PointRecordRepository pointRecordRepository,
                           UserStatisticsRepository userStatisticsRepository) {
        this.userRepository = userRepository;
        this.pointRecordRepository = pointRecordRepository;
        this.userStatisticsRepository = userStatisticsRepository;
    }

    @Transactional(readOnly = true)
    public List<UserListResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserListResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public UserListResponse createUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new com.ecosorter.exception.BadRequestException("用户名已存在");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new com.ecosorter.exception.BadRequestException("邮箱已被使用");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(User.UserRole.RESIDENT);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        return convertToUserListResponse(savedUser);
    }

    @Transactional
    public UserListResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("用户不存在: " + userId);
        }
        
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            user.setUsername(request.getUsername().trim());
        }
        
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }
        
        if (request.getRole() != null) {
            try {
                user.setRole(User.UserRole.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new com.ecosorter.exception.BadRequestException("无效角色: " + request.getRole());
            }
        }
        
        if (request.isActive() != null) {
            user.setIsActive(request.isActive());
        }
        
        userRepository.save(user);
        return convertToUserListResponse(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("用户不存在: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void adjustUserPoints(Long userId, Integer points, String reason) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("用户不存在: " + userId);
        }
        
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(PointType.ADMIN);
        record.setDescription(reason != null ? reason : "管理员调整积分");
        record.setCreatedAt(LocalDateTime.now());
        
        pointRecordRepository.insert(record);
        
        UserStatistics statistics = userStatisticsRepository.findByUserId(userId).orElse(null);
        if (statistics == null) {
            statistics = new UserStatistics();
            statistics.setUserId(userId);
            statistics.setCreatedAt(LocalDateTime.now());
        }
        
        Integer currentPoints = statistics.getTotalPoints();
        if (currentPoints == null) {
            currentPoints = 0;
        }
        statistics.setTotalPoints(currentPoints + points);
        statistics.setUpdatedAt(LocalDateTime.now());
        
        if (statistics.getId() == null) {
            userStatisticsRepository.insert(statistics);
        } else {
            userStatisticsRepository.updateById(statistics);
        }
        
        Integer userCurrentPoints = user.getCurrentPoints();
        if (userCurrentPoints == null) {
            userCurrentPoints = 0;
        }
        user.setCurrentPoints(userCurrentPoints + points);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.updateById(user);
    }

    private UserListResponse convertToUserListResponse(User user) {
        UserListResponse response = new UserListResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setStatus(user.getIsActive() ? "正常" : "禁用");
        response.setPoints(user.getCurrentPoints() != null ? user.getCurrentPoints().longValue() : 0L);
        return response;
    }
}
