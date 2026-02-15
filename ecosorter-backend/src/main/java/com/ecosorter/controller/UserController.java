package com.ecosorter.controller;

import com.ecosorter.dto.UserStatisticsResponse;
import com.ecosorter.service.UserStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserStatisticsService userStatisticsService;
    
    public UserController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserStatisticsResponse> getUserStatistics(
            @AuthenticationPrincipal com.ecosorter.model.User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        UserStatisticsResponse response = userStatisticsService.getUserStatistics(user.getId());
        return ResponseEntity.ok(response);
    }
}
