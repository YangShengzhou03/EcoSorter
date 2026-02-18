package com.ecosorter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class FaceRecognitionService {
    
    private static final Logger logger = LoggerFactory.getLogger(FaceRecognitionService.class);
    
    private final RestTemplate restTemplate;
    
    @Value("${python.api.url:http://localhost:9000}")
    private String pythonApiUrl;
    
    public FaceRecognitionService() {
        this.restTemplate = new RestTemplate();
    }
    
    public Map<String, Object> verifyFaceWithFile(MultipartFile file) {
        try {
            String url = pythonApiUrl + "/api/face/verify-with-file";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            logger.info("[DEBUG] 调用Python人脸验证接口: {}", url);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                logger.info("[DEBUG] Python验证结果: {}", result);
                return result;
            }
            
            return Map.of("success", false, "verified", false, "message", "验证服务异常");
        } catch (Exception e) {
            logger.error("[DEBUG] 调用Python验证接口失败: {}", e.getMessage());
            return Map.of("success", false, "verified", false, "message", "验证服务异常: " + e.getMessage());
        }
    }
}
