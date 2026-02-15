package com.ecosorter.service;

import com.ecosorter.dto.*;
import com.ecosorter.enums.PointType;
import com.ecosorter.enums.TrashcanStatus;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import com.ecosorter.repository.BannerRepository;
import com.ecosorter.repository.PointRecordRepository;
import com.ecosorter.repository.UserStatisticsRepository;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.enums.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminService {
    
    private final UserRepository userRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    private final ClassificationRepository classificationRepository;
    private final com.ecosorter.repository.UserStatisticsRepository userStatisticsRepository;
    private final WasteCategoryRepository wasteCategoryRepository;
    private final BannerRepository bannerRepository;
    private final PointRecordRepository pointRecordRepository;
    private final OrderRepository orderRepository;
    
    public AdminService(UserRepository userRepository,
                        TrashcanDataRepository trashcanDataRepository,
                        ClassificationRepository classificationRepository,
                        com.ecosorter.repository.UserStatisticsRepository userStatisticsRepository,
                        WasteCategoryRepository wasteCategoryRepository,
                        BannerRepository bannerRepository,
                        PointRecordRepository pointRecordRepository,
                        OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.trashcanDataRepository = trashcanDataRepository;
        this.classificationRepository = classificationRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.bannerRepository = bannerRepository;
        this.pointRecordRepository = pointRecordRepository;
        this.orderRepository = orderRepository;
    }
    
    public AdminDashboardResponse getDashboard() {
        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setResidentCount(userRepository.selectList(null).stream()
            .filter(user -> user.getRole() != null && user.getRole().name().equals("RESIDENT"))
            .count());
        response.setTotalDevices(trashcanDataRepository.count());
        response.setCollectorCount(userRepository.selectList(null).stream()
            .filter(user -> user.getRole() != null && user.getRole().name().equals("COLLECTOR"))
            .count());
        response.setPendingOrders(orderRepository.selectList(null).stream()
            .filter(order -> order.getStatus() != null && order.getStatus().equals(OrderStatus.PENDING))
            .count());
        
        return response;
    }
    
    public DeviceStatusResponse getDeviceStatus() {
        DeviceStatusResponse response = new DeviceStatusResponse();
        response.setOnline(trashcanDataRepository.findByStatus("online").size());
        response.setOffline(trashcanDataRepository.findByStatus("offline").size());
        response.setError(trashcanDataRepository.findByStatus("error").size());
        response.setMaintenance(trashcanDataRepository.findByStatus("maintenance").size());
        
        return response;
    }
    
    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivities() {
        List<com.ecosorter.model.Classification> recentClassifications = 
            classificationRepository.findTop10ByOrderByCreatedAtDesc();
        
        return recentClassifications.stream()
                .map(this::convertToActivityResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private ActivityResponse convertToActivityResponse(com.ecosorter.model.Classification classification) {
        ActivityResponse activity = new ActivityResponse();
        activity.setId(classification.getId());
        activity.setCreatedAt(classification.getCreatedAt());
        activity.setType("waste_classification");
        activity.setTypeText("垃圾分类");

        com.ecosorter.model.User user = userRepository.selectById(classification.getUserId());
        String userName = user != null ? user.getUsername() : "未知用户";
        activity.setDescription("用户 " + userName + " 分类垃圾");
        activity.setUserName(userName);

        return activity;
    }
    
    @Transactional(readOnly = true)
    public List<UserListResponse> getUsers() {
        List<com.ecosorter.model.User> users = userRepository.findAll();
        
        return users.stream()
                .map(this::convertToUserListResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public UserListResponse createUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new com.ecosorter.exception.BadRequestException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new com.ecosorter.exception.BadRequestException("Email is already in use!");
        }
        
        com.ecosorter.model.User user = new com.ecosorter.model.User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(com.ecosorter.model.User.UserRole.RESIDENT);
        user.setIsActive(true);
        
        com.ecosorter.model.User savedUser = userRepository.save(user);
        return convertToUserListResponse(savedUser);
    }

    @Transactional
    public UserListResponse updateUser(Long userId, String role, Boolean isActive) {
        com.ecosorter.model.User user = userRepository.selectById(userId);
        if (user == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("User not found with id: " + userId);
        }
        
        if (role != null) {
            try {
                user.setRole(com.ecosorter.model.User.UserRole.valueOf(role.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new com.ecosorter.exception.BadRequestException("Invalid role: " + role);
            }
        }
        
        if (isActive != null) {
            user.setIsActive(isActive);
        }
        
        userRepository.save(user);
        return convertToUserListResponse(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        com.ecosorter.model.User user = userRepository.selectById(userId);
        if (user == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
    
    private UserListResponse convertToUserListResponse(com.ecosorter.model.User user) {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setId(user.getId());
        userListResponse.setUsername(user.getUsername());
        userListResponse.setEmail(user.getEmail());
        userListResponse.setRole(user.getRole().name());
        userListResponse.setStatus(user.getIsActive() ? "正常" : "禁用");
        
        userListResponse.setPoints(user.getCurrentPoints() != null ? user.getCurrentPoints().longValue() : 0L);
        
        return userListResponse;
    }
    
    @Transactional(readOnly = true)
    public List<DeviceListResponse> getDevices() {
        List<com.ecosorter.model.TrashcanData> trashcans = trashcanDataRepository.findAll();
        
        return trashcans.stream()
                .map(this::convertToDeviceListResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public DeviceListResponse createDevice(DeviceListResponse request) {
        com.ecosorter.model.TrashcanData trashcan = new com.ecosorter.model.TrashcanData();
        trashcan.setDeviceId(request.getDeviceId());
        trashcan.setLocation(request.getLocation());
        trashcan.setCapacityLevel(0);
        trashcan.setMaxCapacity(request.getMaxCapacity());
        trashcan.setThreshold(request.getThreshold());
        trashcan.setStatus(TrashcanStatus.ONLINE.getCode());
        trashcan.setLatitude(request.getLatitude());
        trashcan.setLongitude(request.getLongitude());
        
        String authToken = generateAuthToken();
        trashcan.setAuthToken(authToken);
        trashcan.setLastActive(java.time.LocalDateTime.now());
        
        com.ecosorter.model.TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        DeviceListResponse response = convertToDeviceListResponse(savedTrashcan);
        response.setAuthToken(authToken);
        return response;
    }
    
    private String generateAuthToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional
    public DeviceListResponse updateDevice(Long deviceId, DeviceListResponse request) {
        com.ecosorter.model.TrashcanData trashcan = trashcanDataRepository.selectById(deviceId);
        if (trashcan == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("Device not found with id: " + deviceId);
        }
        
        if (request.getLocation() != null) {
            trashcan.setLocation(request.getLocation());
        }
        if (request.getLatitude() != null) {
            trashcan.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            trashcan.setLongitude(request.getLongitude());
        }
        if (request.getMaxCapacity() != null) {
            trashcan.setMaxCapacity(request.getMaxCapacity());
        }
        if (request.getThreshold() != null) {
            trashcan.setThreshold(request.getThreshold());
        }
        if (request.getStatus() != null) {
            TrashcanStatus status = TrashcanStatus.fromCode(request.getStatus());
            if (status != null) {
                trashcan.setStatus(status.getCode());
            }
        }
        
        com.ecosorter.model.TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        return convertToDeviceListResponse(savedTrashcan);
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        trashcanDataRepository.deleteById(deviceId);
    }

    @Transactional
    public DeviceListResponse regenerateAuthToken(Long deviceId) {
        com.ecosorter.model.TrashcanData trashcan = trashcanDataRepository.selectById(deviceId);
        if (trashcan == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("Device not found with id: " + deviceId);
        }
        
        String newAuthToken = generateAuthToken();
        trashcan.setAuthToken(newAuthToken);
        trashcan.setLastActive(java.time.LocalDateTime.now());
        
        com.ecosorter.model.TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        DeviceListResponse response = convertToDeviceListResponse(savedTrashcan);
        response.setAuthToken(newAuthToken);
        return response;
    }

    private DeviceListResponse convertToDeviceListResponse(com.ecosorter.model.TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setLocation(trashcan.getLocation());
        device.setCapacityLevel(trashcan.getCapacityLevel());
        device.setMaxCapacity(trashcan.getMaxCapacity());
        device.setThreshold(trashcan.getThreshold());
        device.setStatus(trashcan.getStatus());
        device.setStatusText(getStatusText(trashcan.getStatus()));
        device.setLatitude(trashcan.getLatitude());
        device.setLongitude(trashcan.getLongitude());
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? trashcan.getUpdatedAt().toString() : "");
        return device;
    }

    private String getStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "online": return "在线";
            case "offline": return "离线";
            case "maintenance": return "维护中";
            case "error": return "故障";
            default: return "未知";
        }
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> getReports() {
        List<com.ecosorter.model.Classification> recentClassifications = 
            classificationRepository.findTop20ByOrderByCreatedAtDesc();
        
        return recentClassifications.stream()
                .map(this::convertToReportResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private ReportResponse convertToReportResponse(com.ecosorter.model.Classification classification) {
        ReportResponse report = new ReportResponse();
        report.setId(classification.getId());
        report.setCreatedAt(classification.getCreatedAt());
        report.setType("waste_classification");
        report.setTypeText("垃圾分类");
        report.setTitle("垃圾分类记录");
        report.setDescription("用户完成垃圾分类操作");
        report.setStatus("completed");
        report.setStatusText("已完成");

        com.ecosorter.model.WasteCategory category = null;
        if (classification.getWasteCategoryId() != null) {
            category = wasteCategoryRepository.selectById(classification.getWasteCategoryId());
        }
        String categoryName = category != null ? category.getName() : "未知";

        com.ecosorter.model.User user = userRepository.selectById(classification.getUserId());
        String userName = user != null ? user.getUsername() : "未知用户";
        report.setUserName(userName);
        
        Double weight = 0.0;
        if (classification.getTrashcanId() != null) {
            com.ecosorter.model.TrashcanData trashcan = trashcanDataRepository.selectById(classification.getTrashcanId());
            if (trashcan != null) {
                weight = trashcan.getCapacityLevel() != null ? trashcan.getCapacityLevel().doubleValue() : 0.0;
            }
        }
        report.setWeight(weight);

        return report;
    }
    
    @Transactional
    public void adjustUserPoints(Long userId, Integer points, String reason) {
        com.ecosorter.model.User user = userRepository.selectById(userId);
        if (user == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("User not found with id: " + userId);
        }
        
        com.ecosorter.model.PointRecord record = new com.ecosorter.model.PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(PointType.ADMIN);
        record.setDescription(reason != null ? reason : "管理员调整积分");
        record.setCreatedAt(java.time.LocalDateTime.now());
        
        pointRecordRepository.insert(record);
        
        com.ecosorter.model.UserStatistics statistics = userStatisticsRepository.findByUserId(userId).orElse(null);
        if (statistics == null) {
            statistics = new com.ecosorter.model.UserStatistics();
            statistics.setUserId(userId);
            statistics.setCreatedAt(java.time.LocalDateTime.now());
        }
        
        Integer currentPoints = statistics.getTotalPoints();
        if (currentPoints == null) {
            currentPoints = 0;
        }
        statistics.setTotalPoints(currentPoints + points);
        statistics.setUpdatedAt(java.time.LocalDateTime.now());
        
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
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.updateById(user);
    }
}
