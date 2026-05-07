package com.ecosorter.controller;

import com.ecosorter.dto.UpdateUserRequest;
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
        return ResponseEntity.ok(profileService.getProfileByUserId(user.getId()));
    }
    
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(user.getId(), request));
    }
    
    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateAvatar(
            @AuthenticationPrincipal User user,
            @RequestBody java.util.Map<String, String> request) {
        String avatarUrl = request.get("avatar");
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(profileService.updateAvatar(user.getId(), avatarUrl));
    }
}
