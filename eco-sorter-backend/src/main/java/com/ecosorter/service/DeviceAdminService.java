package com.ecosorter.service;

import com.ecosorter.dto.*;
import com.ecosorter.enums.TrashcanStatus;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.util.StatusUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceAdminService {
    
    private final TrashcanDataRepository trashcanDataRepository;
    
    public DeviceAdminService(TrashcanDataRepository trashcanDataRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
    }

    @Transactional(readOnly = true)
    public List<DeviceListResponse> getDevices() {
        List<TrashcanData> trashcans = trashcanDataRepository.findAll();
        return trashcans.stream()
                .map(this::convertToDeviceListResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public DeviceListResponse createDevice(DeviceListResponse request) {
        TrashcanData trashcan = new TrashcanData();
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
        trashcan.setLastActive(LocalDateTime.now());
        
        TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        DeviceListResponse response = convertToDeviceListResponse(savedTrashcan);
        response.setAuthToken(authToken);
        return response;
    }

    @Transactional
    public DeviceListResponse updateDevice(Long deviceId, DeviceListResponse request) {
        TrashcanData trashcan = trashcanDataRepository.selectById(deviceId);
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
        
        TrashcanData savedTrashcan = trashcanDataRepository.save(trashcan);
        return convertToDeviceListResponse(savedTrashcan);
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        trashcanDataRepository.deleteById(deviceId);
    }

    @Transactional
    public void resetAdminPassword(Long deviceId) {
        TrashcanData trashcan = trashcanDataRepository.selectById(deviceId);
        if (trashcan == null) {
            throw new com.ecosorter.exception.ResourceNotFoundException("Device not found with id: " + deviceId);
        }
        
        trashcan.setAdminPassword("123456");
        trashcan.setLastActive(LocalDateTime.now());
        trashcanDataRepository.save(trashcan);
    }

    public DeviceStatusResponse getDeviceStatus() {
        DeviceStatusResponse response = new DeviceStatusResponse();
        
        List<TrashcanData> allDevices = trashcanDataRepository.findAll();
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        
        int online = 0;
        int offline = 0;
        int error = 0;
        int maintenance = 0;
        
        for (TrashcanData device : allDevices) {
            String status = device.getStatus();
            LocalDateTime lastActive = device.getLastActive();
            
            if ("maintenance".equals(status)) {
                maintenance++;
            } else if ("error".equals(status)) {
                error++;
            } else if (lastActive != null && lastActive.isAfter(fiveMinutesAgo)) {
                online++;
            } else {
                offline++;
            }
        }
        
        response.setOnline(online);
        response.setOffline(offline);
        response.setError(error);
        response.setMaintenance(maintenance);
        
        return response;
    }

    private String generateAuthToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    private DeviceListResponse convertToDeviceListResponse(TrashcanData trashcan) {
        DeviceListResponse device = new DeviceListResponse();
        device.setId(trashcan.getId());
        device.setDeviceId(trashcan.getDeviceId());
        device.setLocation(trashcan.getLocation());
        device.setCapacityLevel(trashcan.getCapacityLevel() != null ? trashcan.getCapacityLevel().intValue() : 0);
        device.setMaxCapacity(trashcan.getMaxCapacity() != null ? trashcan.getMaxCapacity().intValue() : 0);
        device.setThreshold(trashcan.getThreshold() != null ? trashcan.getThreshold().intValue() : 0);
        
        String actualStatus = getActualStatus(trashcan);
        device.setStatus(actualStatus);
        device.setStatusText(StatusUtil.getTrashcanStatusText(actualStatus));
        
        device.setLatitude(trashcan.getLatitude());
        device.setLongitude(trashcan.getLongitude());
        device.setLastUpdate(trashcan.getUpdatedAt());
        return device;
    }

    private String getActualStatus(TrashcanData trashcan) {
        String status = trashcan.getStatus();
        if ("maintenance".equals(status)) {
            return "maintenance";
        }
        if ("error".equals(status)) {
            return "error";
        }
        
        LocalDateTime lastActive = trashcan.getLastActive();
        if (lastActive != null && lastActive.isAfter(LocalDateTime.now().minusMinutes(5))) {
            return "online";
        }
        return "offline";
    }
}
