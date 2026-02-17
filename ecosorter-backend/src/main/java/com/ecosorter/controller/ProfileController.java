package com.ecosorter.controller;

import com.ecosorter.dto.ProfileResponse;
import com.ecosorter.model.User;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.service.FaceRecognitionService;
import com.ecosorter.service.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final FaceRecognitionService faceRecognitionService;
    private final RestTemplate restTemplate;
    
    @Value("${python.api.url:http://localhost:9000}")
    private String pythonApiUrl;
    
    public ProfileController(ProfileService profileService, 
                         UserRepository userRepository,
                         FaceRecognitionService faceRecognitionService,
                         RestTemplate restTemplate) {
        this.profileService = profileService;
        this.userRepository = userRepository;
        this.faceRecognitionService = faceRecognitionService;
        this.restTemplate = restTemplate;
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(profileService.getProfileByUserId(user.getId()));
    }
    
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody User profileData) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(profileService.updateProfile(user.getId(), profileData));
    }
    
    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
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
    
    @PostMapping("/face")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> registerFace(
            @AuthenticationPrincipal User user,
            @RequestBody java.util.Map<String, String> request) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        String faceEncoding = request.get("faceEncoding");
        if (faceEncoding == null || faceEncoding.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("faceEncoding is required");
        }
        
        faceRecognitionService.saveFaceEncoding(user.getId(), faceEncoding);
        
        return ResponseEntity.ok("人脸注册成功");
    }
    
    @PostMapping("/face/register-from-file")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> registerFaceFromFile(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件不能为空");
        }
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", resource);
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            ResponseEntity<java.util.Map> response = restTemplate.postForEntity(
                pythonApiUrl + "/api/face/encode-from-file",
                requestEntity,
                java.util.Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                java.util.Map<String, Object> responseBody = response.getBody();
                if (responseBody.containsKey("encoding")) {
                    String faceEncoding = (String) responseBody.get("encoding");
                    faceRecognitionService.saveFaceEncoding(user.getId(), faceEncoding);
                    return ResponseEntity.ok("人脸注册成功");
                }
            }
            
            return ResponseEntity.badRequest().body("人脸特征提取失败");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("人脸注册失败: " + e.getMessage());
        }
    }
}
