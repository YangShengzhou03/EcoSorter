package com.ecosorter.service;

import com.ecosorter.repository.UserRepository;
import com.ecosorter.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FaceRecognitionService {
    
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    
    @Value("${python.api.url:http://localhost:9000}")
    private String pythonApiUrl;
    
    public FaceRecognitionService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }
    
    public String extractFaceEncoding(String imageUrl) {
        try {
            String url = pythonApiUrl + "/api/face/encode";
            Map<String, String> request = new HashMap<>();
            request.put("image_url", imageUrl);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("encoding");
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public User findMatchingUser(String faceEncoding) {
        List<User> allUsers = userRepository.findAll();
        
        User bestMatch = null;
        double bestScore = 0.0;
        
        for (User user : allUsers) {
            if (user.getFaceEncoding() != null && !user.getFaceEncoding().isEmpty()) {
                double similarity = calculateSimilarity(faceEncoding, user.getFaceEncoding());
                if (similarity > bestScore && similarity > 0.6) {
                    bestScore = similarity;
                    bestMatch = user;
                }
            }
        }
        
        return bestMatch;
    }
    
    private double calculateSimilarity(String encoding1, String encoding2) {
        try {
            String[] arr1 = encoding1.replace("[", "").replace("]", "").split(",");
            String[] arr2 = encoding2.replace("[", "").replace("]", "").split(",");
            
            if (arr1.length != arr2.length) {
                return 0.0;
            }
            
            double sum = 0.0;
            for (int i = 0; i < arr1.length; i++) {
                double val1 = Double.parseDouble(arr1[i].trim());
                double val2 = Double.parseDouble(arr2[i].trim());
                sum += Math.pow(val1 - val2, 2);
            }
            
            double distance = Math.sqrt(sum);
            return 1.0 / (1.0 + distance);
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    public void saveFaceEncoding(Long userId, String faceEncoding) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFaceEncoding(faceEncoding);
            user.setFaceVerified(true);
            userRepository.save(user);
        }
    }
}
