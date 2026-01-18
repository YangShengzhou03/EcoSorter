package com.ecosorter.service;

import com.ecosorter.dto.*;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.WasteCategoryRepository;
import com.ecosorter.repository.BannerRepository;
import com.ecosorter.repository.WasteCategoryExampleRepository;
import com.ecosorter.repository.PointRecordRepository;
import com.ecosorter.repository.UserStatisticsRepository;
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
    private final WasteCategoryExampleRepository wasteCategoryExampleRepository;
    private final PointRecordRepository pointRecordRepository;
    
    public AdminService(UserRepository userRepository,
                        TrashcanDataRepository trashcanDataRepository,
                        ClassificationRepository classificationRepository,
                        com.ecosorter.repository.UserStatisticsRepository userStatisticsRepository,
                        WasteCategoryRepository wasteCategoryRepository,
                        BannerRepository bannerRepository,
                        WasteCategoryExampleRepository wasteCategoryExampleRepository,
                        PointRecordRepository pointRecordRepository) {
        this.userRepository = userRepository;
        this.trashcanDataRepository = trashcanDataRepository;
        this.classificationRepository = classificationRepository;
        this.userStatisticsRepository = userStatisticsRepository;
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.bannerRepository = bannerRepository;
        this.wasteCategoryExampleRepository = wasteCategoryExampleRepository;
        this.pointRecordRepository = pointRecordRepository;
    }
    
    public AdminDashboardResponse getDashboard() {
        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setTotalUsers(userRepository.count());
        response.setTotalDevices(trashcanDataRepository.count());
        response.setTotalCollections(classificationRepository.count());
        response.setTotalPoints(classificationRepository.count() * 10);
        
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
        activity.setTime(classification.getCreatedAt());
        activity.setType("waste_classification");
        
        com.ecosorter.model.User user = userRepository.findById(classification.getUserId()).orElse(null);
        String userName = user != null ? user.getUsername() : "未知用户";
        activity.setDescription("用户 " + userName + " 分类垃圾");
        
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
        com.ecosorter.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("User not found with id: " + userId));
        
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
        com.ecosorter.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }
    
    private UserListResponse convertToUserListResponse(com.ecosorter.model.User user) {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setId(user.getId());
        userListResponse.setUsername(user.getUsername());
        userListResponse.setRole(user.getRole().toString());
        userListResponse.setStatus(user.getIsActive() ? "正常" : "禁用");
        
        com.ecosorter.model.UserStatistics stats = 
            userStatisticsRepository.findByUserId(user.getId()).orElse(null);
        userListResponse.setPoints(stats != null ? stats.getTotalClassifications() * 10L : 0L);
        
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
        trashcan.setStatus("online");
        
        com.ecosorter.model.TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        return convertToDeviceListResponse(savedTrashcan);
    }

    @Transactional
    public DeviceListResponse updateDevice(Long deviceId, DeviceListResponse request) {
        com.ecosorter.model.TrashcanData trashcan = trashcanDataRepository.findById(deviceId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Device not found with id: " + deviceId));
        
        if (request.getLocation() != null) {
            trashcan.setLocation(request.getLocation());
        }
        if (request.getMaxCapacity() != null) {
            trashcan.setMaxCapacity(request.getMaxCapacity());
        }
        if (request.getThreshold() != null) {
            trashcan.setThreshold(request.getThreshold());
        }
        if (request.getStatus() != null) {
            trashcan.setStatus(request.getStatus());
        }
        
        com.ecosorter.model.TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        return convertToDeviceListResponse(savedTrashcan);
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        trashcanDataRepository.deleteById(deviceId);
    }

    @Transactional(readOnly = true)
    public List<UserListResponse> getCollectors() {
        List<com.ecosorter.model.User> collectors = userRepository.findByRole(com.ecosorter.model.User.UserRole.COLLECTOR);
        
        return collectors.stream()
                .map(this::convertToUserListResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private DeviceListResponse convertToDeviceListResponse(com.ecosorter.model.TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setName("智能垃圾桶-" + trashcan.getDeviceId());
        device.setLocation(trashcan.getLocation());
        device.setCapacity(trashcan.getCapacityLevel());
        device.setMaxCapacity(trashcan.getMaxCapacity());
        device.setThreshold(trashcan.getThreshold());
        device.setStatus(trashcan.getStatus());
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? trashcan.getUpdatedAt().toString() : "");
        return device;
    }
    
    @Transactional(readOnly = true)
    public List<LogResponse> getLogs() {
        List<com.ecosorter.model.Classification> recentClassifications = 
            classificationRepository.findTop20ByOrderByCreatedAtDesc();
        
        return recentClassifications.stream()
                .map(this::convertToLogResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private LogResponse convertToLogResponse(com.ecosorter.model.Classification classification) {
        LogResponse log = new LogResponse();
        log.setId(classification.getId());
        log.setTimestamp(classification.getCreatedAt());
        log.setLevel("INFO");
        log.setType("垃圾分类");
        
        com.ecosorter.model.User user = userRepository.findById(classification.getUserId()).orElse(null);
        String userName = user != null ? user.getUsername() : "未知用户";
        log.setMessage("用户 " + userName + " 完成垃圾分类");
        
        return log;
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
        report.setTime(classification.getCreatedAt());
        
        com.ecosorter.model.WasteCategory category = null;
        if (classification.getWasteCategoryId() != null) {
            category = wasteCategoryRepository.findById(classification.getWasteCategoryId()).orElse(null);
        }
        String categoryName = category != null ? category.getName() : "未知";
        report.setType(categoryName);
        
        report.setWeight(1.0);
        
        com.ecosorter.model.User user = userRepository.findById(classification.getUserId()).orElse(null);
        String userName = user != null ? user.getUsername() : "未知用户";
        report.setUser(userName);
        
        return report;
    }
    
    public ConfigurationResponse getConfiguration() {
        ConfigurationResponse config = new ConfigurationResponse();
        config.setSystemName("EcoSorter 垃圾分类系统");
        config.setMaintenanceMode(false);
        config.setPointsRatio(10.0);
        config.setDailyLimit(100);
        config.setHeartbeatInterval(60);
        config.setOfflineTimeout(600);
        return config;
    }
    
    public ConfigurationResponse updateConfiguration(ConfigurationRequest request) {
        ConfigurationResponse config = new ConfigurationResponse();
        config.setSystemName(request.getSystemName());
        config.setMaintenanceMode(request.getMaintenanceMode());
        config.setPointsRatio(request.getPointsRatio());
        config.setDailyLimit(request.getDailyLimit());
        config.setHeartbeatInterval(request.getHeartbeatInterval());
        config.setOfflineTimeout(request.getOfflineTimeout());
        return config;
    }
    
    @Transactional(readOnly = true)
    public List<BannerResponse> getAllBanners() {
        List<com.ecosorter.model.Banner> banners = bannerRepository.findAll();
        return banners.stream()
                .map(this::convertToBannerResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Transactional
    public BannerResponse createBanner(BannerRequest request) {
        com.ecosorter.model.Banner banner = new com.ecosorter.model.Banner();
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setBackground(request.getBackground());
        banner.setSortOrder(request.getSortOrder());
        banner.setIsActive(request.getIsActive());
        
        com.ecosorter.model.Banner savedBanner = bannerRepository.save(banner);
        return convertToBannerResponse(savedBanner);
    }
    
    @Transactional
    public BannerResponse updateBanner(Long id, BannerRequest request) {
        com.ecosorter.model.Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Banner not found with id: " + id));
        
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setBackground(request.getBackground());
        banner.setSortOrder(request.getSortOrder());
        banner.setIsActive(request.getIsActive());
        
        com.ecosorter.model.Banner savedBanner = bannerRepository.save(banner);
        return convertToBannerResponse(savedBanner);
    }
    
    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
    
    private BannerResponse convertToBannerResponse(com.ecosorter.model.Banner banner) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getId());
        response.setTitle(banner.getTitle());
        response.setDescription(banner.getDescription());
        response.setBackground(banner.getBackground());
        response.setSortOrder(banner.getSortOrder());
        response.setIsActive(banner.getIsActive());
        return response;
    }
    
    @Transactional(readOnly = true)
    public List<WasteCategoryResponse> getAllCategories() {
        List<com.ecosorter.model.WasteCategory> categories = wasteCategoryRepository.findAll();
        return categories.stream()
                .map(this::convertToWasteCategoryResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Transactional
    public WasteCategoryResponse createCategory(WasteCategoryRequest request) {
        com.ecosorter.model.WasteCategory category = new com.ecosorter.model.WasteCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        category.setDisposalInstructions(request.getDisposalInstructions());
        category.setEnvironmentalImpact(request.getEnvironmentalImpact() != null ? request.getEnvironmentalImpact().toString() : null);
        category.setIsActive(request.getActive() != null ? request.getActive() : true);
        
        com.ecosorter.model.WasteCategory savedCategory = wasteCategoryRepository.save(category);
        return convertToWasteCategoryResponse(savedCategory);
    }
    
    @Transactional
    public WasteCategoryResponse updateCategory(Long id, WasteCategoryRequest request) {
        com.ecosorter.model.WasteCategory category = wasteCategoryRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Category not found with id: " + id));
        
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getColor() != null) {
            category.setColor(request.getColor());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getEnvironmentalImpact() != null) {
            category.setEnvironmentalImpact(request.getEnvironmentalImpact().toString());
        }
        if (request.getDisposalInstructions() != null) {
            category.setDisposalInstructions(request.getDisposalInstructions());
        }
        if (request.getActive() != null) {
            category.setIsActive(request.getActive());
        }
        
        com.ecosorter.model.WasteCategory savedCategory = wasteCategoryRepository.save(category);
        return convertToWasteCategoryResponse(savedCategory);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        wasteCategoryRepository.deleteById(id);
    }
    
    private WasteCategoryResponse convertToWasteCategoryResponse(com.ecosorter.model.WasteCategory category) {
        WasteCategoryResponse response = new WasteCategoryResponse();
        response.setId(category.getId() != null ? category.getId().toString() : null);
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setColor(category.getColor());
        response.setIcon(category.getIcon());
        response.setDisposalInstructions(category.getDisposalInstructions());
        response.setEnvironmentalImpact(category.getEnvironmentalImpact() != null ? Integer.parseInt(category.getEnvironmentalImpact()) : null);
        response.setActive(category.getIsActive());
        return response;
    }
    
    @Transactional
    public void adjustUserPoints(Long userId, Integer points, String reason) {
        com.ecosorter.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("User not found with id: " + userId));
        
        com.ecosorter.model.PointRecord record = new com.ecosorter.model.PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType("admin");
        record.setDescription(reason != null ? reason : "管理员调整积分");
        
        pointRecordRepository.save(record);
        
        com.ecosorter.model.UserStatistics statistics = userStatisticsRepository.findByUserId(userId).orElse(null);
        if (statistics == null) {
            statistics = new com.ecosorter.model.UserStatistics();
            statistics.setUser(user);
        }
        
        Integer currentPoints = pointRecordRepository.getTotalPointsByUserId(userId);
        if (currentPoints == null) {
            currentPoints = 0;
        }
        statistics.setTotalPoints(currentPoints + points);
        
        userStatisticsRepository.save(statistics);
    }
}
