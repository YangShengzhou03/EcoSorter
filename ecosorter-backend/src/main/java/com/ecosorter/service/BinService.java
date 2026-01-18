package com.ecosorter.service;

import com.ecosorter.dto.BinStatusResponse;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.repository.TrashcanDataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BinService {
    
    private final TrashcanDataRepository trashcanDataRepository;
    
    public BinService(TrashcanDataRepository trashcanDataRepository) {
        this.trashcanDataRepository = trashcanDataRepository;
    }
    
    public BinStatusResponse getBinStatus(String deviceId) {
        Optional<TrashcanData> trashcan = trashcanDataRepository.findByDeviceId(deviceId);
        
        if (trashcan.isEmpty()) {
            throw new RuntimeException("Bin not found with device id: " + deviceId);
        }
        
        TrashcanData data = trashcan.get();
        
        BinStatusResponse response = new BinStatusResponse();
        response.setBinId(data.getDeviceId());
        response.setLocation(data.getLocation());
        response.setFillLevel(data.getCapacityLevel());
        response.setStatus(data.getStatus());
        
        return response;
    }
    
    public BinStatusResponse updateBinStatus(String deviceId, BinStatusResponse request) {
        Optional<TrashcanData> trashcan = trashcanDataRepository.findByDeviceId(deviceId);
        
        if (trashcan.isEmpty()) {
            throw new RuntimeException("Bin not found with device id: " + deviceId);
        }
        
        TrashcanData data = trashcan.get();
        data.setCapacityLevel(request.getFillLevel());
        data.setStatus(request.getStatus());
        data.setUpdatedAt(java.time.LocalDateTime.now());
        
        TrashcanData saved = trashcanDataRepository.save(data);
        
        BinStatusResponse response = new BinStatusResponse();
        response.setBinId(saved.getDeviceId());
        response.setLocation(saved.getLocation());
        response.setFillLevel(saved.getCapacityLevel());
        response.setStatus(saved.getStatus());
        
        return response;
    }
}
