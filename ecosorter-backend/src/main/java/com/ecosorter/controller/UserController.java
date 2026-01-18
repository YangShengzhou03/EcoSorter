package com.ecosorter.controller;

import com.ecosorter.dto.UserStatisticsResponse;
import com.ecosorter.service.UserStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserStatisticsService userStatisticsService;
    
    public UserController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<UserStatisticsResponse> getUserStatistics(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        UserStatisticsResponse response = userStatisticsService.getUserStatistics(userId);
        return ResponseEntity.ok(response);
    }
    
    private Long getUserIdFromToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String userIdStr = token.replace("simple-token-", "");
            try {
                return Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
