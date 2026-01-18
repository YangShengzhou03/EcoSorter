package com.ecosorter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadService {
    
    @Value("${upload.path:./uploads}")
    private String uploadPath;
    
    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;
    
    public String uploadAvatar(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;
            
            Path avatarDir = Paths.get(uploadPath, "avatars");
            if (!Files.exists(avatarDir)) {
                Files.createDirectories(avatarDir);
            }
            
            Path filePath = avatarDir.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);
            
            return baseUrl + "/uploads/avatars/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("上传头像失败", e);
        }
    }
    
    public String uploadImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;
            
            Path imageDir = Paths.get(uploadPath, "images");
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
            
            Path filePath = imageDir.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);
            
            return baseUrl + "/uploads/images/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("上传图片失败", e);
        }
    }
}
