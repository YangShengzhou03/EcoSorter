package com.ecosorter.service;

import com.ecosorter.dto.*;
import com.ecosorter.enums.OrderStatus;
import com.ecosorter.model.Classification;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.repository.ClassificationRepository;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    
    private final UserRepository userRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    private final ClassificationRepository classificationRepository;
    private final OrderRepository orderRepository;
    private final UserAdminService userAdminService;
    private final DeviceAdminService deviceAdminService;
    private final ActivityService activityService;
    
    public AdminService(UserRepository userRepository,
                        TrashcanDataRepository trashcanDataRepository,
                        ClassificationRepository classificationRepository,
                        OrderRepository orderRepository,
                        UserAdminService userAdminService,
                        DeviceAdminService deviceAdminService,
                        ActivityService activityService) {
        this.userRepository = userRepository;
        this.trashcanDataRepository = trashcanDataRepository;
        this.classificationRepository = classificationRepository;
        this.orderRepository = orderRepository;
        this.userAdminService = userAdminService;
        this.deviceAdminService = deviceAdminService;
        this.activityService = activityService;
    }

    public AdminDashboardResponse getDashboard() {
        AdminDashboardResponse response = new AdminDashboardResponse();
        
        response.setTotalUsers(userRepository.count());
        response.setTotalDevices(trashcanDataRepository.count());
        
        List<Classification> allClassifications = classificationRepository.selectList(null);
        response.setRecentClassifications((long) allClassifications.size());
        
        Long totalWeight = 0L;
        for (Classification classification : allClassifications) {
            if (classification.getTrashcanId() != null) {
                TrashcanData trashcan = trashcanDataRepository.selectById(classification.getTrashcanId());
                if (trashcan != null && trashcan.getCapacityLevel() != null) {
                    totalWeight += trashcan.getCapacityLevel();
                }
            }
        }
        response.setTotalWeight(totalWeight);
        
        Long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        response.setPendingOrders(pendingOrders != null ? pendingOrders : 0L);
        
        return response;
    }

    public DeviceStatusResponse getDeviceStatus() {
        return deviceAdminService.getDeviceStatus();
    }

    public List<ActivityResponse> getActivities() {
        return activityService.getActivities();
    }

    public List<UserListResponse> getUsers() {
        return userAdminService.getUsers();
    }

    public UserListResponse createUser(RegisterRequest request) {
        return userAdminService.createUser(request);
    }

    public UserListResponse updateUser(Long userId, UpdateUserRequest request) {
        return userAdminService.updateUser(userId, request);
    }

    public void deleteUser(Long userId) {
        userAdminService.deleteUser(userId);
    }

    public void adjustUserPoints(Long userId, Integer points, String reason) {
        userAdminService.adjustUserPoints(userId, points, reason);
    }

    public List<DeviceListResponse> getDevices() {
        return deviceAdminService.getDevices();
    }

    public DeviceListResponse createDevice(DeviceListResponse request) {
        return deviceAdminService.createDevice(request);
    }

    public DeviceListResponse updateDevice(Long deviceId, DeviceListResponse request) {
        return deviceAdminService.updateDevice(deviceId, request);
    }

    public void deleteDevice(Long deviceId) {
        deviceAdminService.deleteDevice(deviceId);
    }

    public void resetAdminPassword(Long deviceId) {
        deviceAdminService.resetAdminPassword(deviceId);
    }

    public List<ReportResponse> getReports() {
        return activityService.getReports();
    }
}
