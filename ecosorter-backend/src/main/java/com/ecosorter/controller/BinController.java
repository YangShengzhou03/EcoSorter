package com.ecosorter.controller;

import com.ecosorter.dto.BinStatusResponse;
import com.ecosorter.service.BinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bin")
public class BinController {
    
    private final BinService binService;
    
    public BinController(BinService binService) {
        this.binService = binService;
    }
    
    @GetMapping("/status/{deviceId}")
    public ResponseEntity<BinStatusResponse> getBinStatus(@PathVariable String deviceId) {
        BinStatusResponse response = binService.getBinStatus(deviceId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/status/{deviceId}")
    public ResponseEntity<BinStatusResponse> updateBinStatus(
            @PathVariable String deviceId,
            @RequestBody BinStatusResponse request) {
        BinStatusResponse response = binService.updateBinStatus(deviceId, request);
        return ResponseEntity.ok(response);
    }
}
