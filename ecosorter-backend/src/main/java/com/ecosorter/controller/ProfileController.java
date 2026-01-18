package com.ecosorter.controller;

import com.ecosorter.dto.ChangePasswordRequest;
import com.ecosorter.dto.ProfileResponse;
import com.ecosorter.model.User;
import com.ecosorter.model.UserProfile;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    public ProfileController(ProfileService profileService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(profileService.getProfileByUserId(user.getId()));
    }
    
    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserProfile profileData) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(profileService.updateProfile(user.getId(), profileData));
    }
    
    @PutMapping("/avatar")
    public ResponseEntity<ProfileResponse> updateAvatar(
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
    
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody ChangePasswordRequest request) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
