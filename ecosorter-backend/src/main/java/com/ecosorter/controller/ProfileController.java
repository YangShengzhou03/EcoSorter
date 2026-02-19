package com.ecosorter.controller;

import com.ecosorter.dto.UserResponse;
import com.ecosorter.model.User;
import com.ecosorter.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    private final ProfileService profileService;
    
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(profileService.getProfileByUserId(user.getId()));
    }
    
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody User profileData) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(profileService.updateProfile(user.getId(), profileData));
    }
    
    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateAvatar(
            @AuthenticationPrincipal User user,
            @RequestBody java.util.Map<String, String> request) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        String avatarUrl = request.get("avatar");
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(profileService.updateAvatar(user.getId(), avatarUrl));
    }
}
