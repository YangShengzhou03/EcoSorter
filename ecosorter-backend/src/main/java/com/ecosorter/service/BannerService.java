package com.ecosorter.service;

import com.ecosorter.dto.BannerRequest;
import com.ecosorter.dto.BannerResponse;
import com.ecosorter.model.Banner;
import com.ecosorter.repository.BannerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerService {
    
    private final BannerRepository bannerRepository;
    
    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }
    
    public List<BannerResponse> getActiveBanners(String target) {
        List<Banner> banners;
        if (target != null && !target.trim().isEmpty()) {
            banners = bannerRepository.findByIsActiveAndTargetOrderBySortOrderAsc(true, target);
        } else {
            banners = bannerRepository.findByIsActiveOrderBySortOrderAsc(true);
        }
        return banners.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public BannerResponse getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Banner not found"));
        return convertToResponse(banner);
    }
    
    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = new Banner();
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setBackground(request.getBackground());
        banner.setSortOrder(request.getSortOrder());
        banner.setTarget(request.getTarget() != null ? request.getTarget() : "user");
        banner.setIsActive(request.getIsActive());
        
        Banner savedBanner = bannerRepository.save(banner);
        return convertToResponse(savedBanner);
    }
    
    public BannerResponse updateBanner(Long id, BannerRequest request) {
        Banner existingBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Banner not found"));
        
        existingBanner.setTitle(request.getTitle());
        existingBanner.setDescription(request.getDescription());
        existingBanner.setBackground(request.getBackground());
        existingBanner.setSortOrder(request.getSortOrder());
        existingBanner.setIsActive(request.getIsActive());
        
        Banner updatedBanner = bannerRepository.save(existingBanner);
        return convertToResponse(updatedBanner);
    }
    
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new com.ecosorter.exception.ResourceNotFoundException("Banner not found");
        }
        bannerRepository.deleteById(id);
    }
    
    private BannerResponse convertToResponse(Banner banner) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getId());
        response.setTitle(banner.getTitle());
        response.setDescription(banner.getDescription());
        response.setBackground(banner.getBackground());
        response.setSortOrder(banner.getSortOrder());
        response.setIsActive(banner.getIsActive());
        return response;
    }
}
