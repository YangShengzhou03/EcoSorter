package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecosorter.dto.DeviceListResponse;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.repository.TrashcanDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trashcans")
public class TrashcanPublicController {
    
    private final TrashcanDataRepository trashcanDataRepository;
    
    public TrashcanPublicController(TrashcanDataRepository trashcanDataRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
    }
    
    @GetMapping("/nearby")
    public ResponseEntity<List<DeviceListResponse>> getNearbyTrashcans(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false, defaultValue = "5000") Double radius) {
        
        List<TrashcanData> allTrashcans = trashcanDataRepository.selectList(
            new LambdaQueryWrapper<TrashcanData>()
                .eq(TrashcanData::getStatus, "online")
        );
        
        List<DeviceListResponse> response = allTrashcans.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        if (latitude != null && longitude != null) {
            response = response.stream()
                .filter(t -> t.getLatitude() != null && t.getLongitude() != null)
                .filter(t -> {
                    double distance = calculateDistance(
                        latitude, longitude,
                        t.getLatitude().doubleValue(), t.getLongitude().doubleValue()
                    );
                    return distance <= radius;
                })
                .sorted((a, b) -> {
                    double distA = calculateDistance(
                        latitude, longitude,
                        a.getLatitude().doubleValue(), a.getLongitude().doubleValue()
                    );
                    double distB = calculateDistance(
                        latitude, longitude,
                        b.getLatitude().doubleValue(), b.getLongitude().doubleValue()
                    );
                    return Double.compare(distA, distB);
                })
                .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DeviceListResponse> getTrashcanById(@PathVariable Long id) {
        TrashcanData trashcan = trashcanDataRepository.selectById(id);
        if (trashcan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToResponse(trashcan));
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<DeviceListResponse> getTrashcanByDeviceId(@PathVariable String deviceId) {
        TrashcanData trashcan = trashcanDataRepository.selectOne(
            new LambdaQueryWrapper<TrashcanData>()
                .eq(TrashcanData::getDeviceId, deviceId)
        );
        if (trashcan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToResponse(trashcan));
    }
    
    private DeviceListResponse convertToResponse(TrashcanData trashcan) {
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
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000;
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
}
