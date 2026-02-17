package com.ecosorter.service;

import com.ecosorter.config.JwtUtil;
import com.ecosorter.dto.AuthResponse;
import com.ecosorter.dto.DeviceActivateRequest;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.dto.LoginRequest;
import com.ecosorter.dto.RegisterRequest;
import com.ecosorter.dto.UserResponse;
import com.ecosorter.enums.TrashcanStatus;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TrashcanDataRepository trashcanDataRepository;
    private final FaceRecognitionService faceRecognitionService;
    
    public AuthService(UserRepository userRepository, 
                       JwtUtil jwtUtil,
                       TrashcanDataRepository trashcanDataRepository,
                       FaceRecognitionService faceRecognitionService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.trashcanDataRepository = trashcanDataRepository;
        this.faceRecognitionService = faceRecognitionService;
    }
    
    private UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setId(user.getId() != null ? user.getId().toString() : null);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole() != null ? user.getRole().name() : null);
        response.setIsActive(user.getIsActive());
        response.setLastLogin(user.getLastLogin() != null ? user.getLastLogin().toString() : null);
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
        
        UserResponse.UserProfileDto profileDto = new UserResponse.UserProfileDto();
        profileDto.setAvatar(user.getAvatarUrl());
        profileDto.setPhone(user.getPhone());
        profileDto.setFullName(user.getUsername());
        response.setProfile(profileDto);
        
        return response;
    }
    
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("用户名已存在");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("邮箱已被使用");
        }
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        
        if (registerRequest.getRole() != null && !registerRequest.getRole().isEmpty()) {
            try {
                user.setRole(User.UserRole.valueOf(registerRequest.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setRole(User.UserRole.RESIDENT);
            }
        } else {
            user.setRole(User.UserRole.RESIDENT);
        }
        
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        
        String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getUsername(), savedUser.getRole().name());
        
        UserResponse userResponse = convertToUserResponse(savedUser);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public DeviceListResponse activateDevice(DeviceActivateRequest request) {
        String deviceId = generateDeviceId();
        
        if (trashcanDataRepository.findByDeviceId(deviceId) != null) {
            throw new BadRequestException("设备ID已存在");
        }
        
        String authToken = jwtUtil.generateDeviceToken(deviceId);
        
        TrashcanData trashcan = new TrashcanData();
        trashcan.setDeviceId(deviceId);
        trashcan.setDeviceName(request.getDeviceName());
        trashcan.setLocation(request.getLocation());
        trashcan.setBinType(request.getBinType() != null ? request.getBinType() : "recyclable");
        trashcan.setCapacityLevel(0);
        trashcan.setMaxCapacity(100);
        trashcan.setThreshold(80);
        trashcan.setStatus(TrashcanStatus.ONLINE.getCode());
        
        trashcan.setAuthToken(authToken);
        trashcan.setAdminPassword(request.getPassword());
        trashcan.setLastActive(LocalDateTime.now());
        
        TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        
        DeviceListResponse response = convertToDeviceListResponse(savedTrashcan);
        response.setAuthToken(authToken);
        
        return response;
    }
    
    private String generateDeviceId() {
        return "TC" + System.currentTimeMillis() + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private DeviceListResponse convertToDeviceListResponse(TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setDeviceName(trashcan.getDeviceName());
        device.setLocation(trashcan.getLocation());
        device.setBinType(trashcan.getBinType());
        device.setCapacityLevel(trashcan.getCapacityLevel() != null ? 
            trashcan.getCapacityLevel().intValue() : 0);
        device.setMaxCapacity(trashcan.getMaxCapacity() != null ? 
            trashcan.getMaxCapacity().intValue() : 0);
        device.setThreshold(trashcan.getThreshold() != null ? 
            trashcan.getThreshold().intValue() : 0);
        device.setStatus(trashcan.getStatus());
        device.setStatusText(getStatusText(trashcan.getStatus()));
        device.setLatitude(trashcan.getLatitude());
        device.setLongitude(trashcan.getLongitude());
        device.setLastUpdate(trashcan.getUpdatedAt() != null ? 
            trashcan.getUpdatedAt().toString() : "");
        device.setAuthToken(trashcan.getAuthToken());
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
    
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("用户不存在"));
        
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        
        UserResponse userResponse = convertToUserResponse(user);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid or expired token");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String newToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        
        UserResponse userResponse = convertToUserResponse(user);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(newToken);
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public void logout(String token) {
    }
    
    public AuthResponse faceLogin(String faceImageUrl) {
        String faceEncoding = faceRecognitionService.extractFaceEncoding(faceImageUrl);
        
        if (faceEncoding == null || faceEncoding.isEmpty()) {
            throw new BadRequestException("人脸特征提取失败");
        }
        
        User user = faceRecognitionService.findMatchingUser(faceEncoding);
        
        if (user == null) {
            throw new BadRequestException("未找到匹配的用户");
        }
        
        if (!user.getIsActive()) {
            throw new BadRequestException("用户已被禁用");
        }
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        
        UserResponse userResponse = convertToUserResponse(user);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setExpiresIn(86400000L);
        authResponse.setUser(userResponse);
        
        return authResponse;
    }
    
    public UserResponse getCurrentUser(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new BadRequestException("Invalid or expired token");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return convertToUserResponse(user);
    }
}