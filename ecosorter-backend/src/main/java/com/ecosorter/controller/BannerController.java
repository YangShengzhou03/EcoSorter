package com.ecosorter.controller;

import com.ecosorter.dto.BannerRequest;
import com.ecosorter.dto.BannerResponse;
import com.ecosorter.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
public class BannerController {
    
    private final BannerService bannerService;
    
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }
    
    @GetMapping
    public ResponseEntity<List<BannerResponse>> getActiveBanners(
            @RequestParam(required = false) String target) {
        return ResponseEntity.ok(bannerService.getActiveBanners(target));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BannerResponse> getBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(bannerService.getBannerById(id));
    }
    
    @PostMapping
    public ResponseEntity<BannerResponse> createBanner(@RequestBody BannerRequest request) {
        return ResponseEntity.ok(bannerService.createBanner(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BannerResponse> updateBanner(@PathVariable Long id, @RequestBody BannerRequest request) {
        return ResponseEntity.ok(bannerService.updateBanner(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
}
