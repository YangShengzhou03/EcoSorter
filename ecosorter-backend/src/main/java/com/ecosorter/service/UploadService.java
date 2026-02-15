package com.ecosorter.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class UploadService {
    
    private final OSS ossClient;
    private final String bucketName;
    private final String baseUrl;
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    
    public UploadService(OSS ossClient, 
                        @Value("${aliyun.oss.bucket-name}") String bucketName,
                        @Value("${aliyun.oss.base-url}") String baseUrl) {
        this.ossClient = ossClient;
        this.bucketName = bucketName;
        this.baseUrl = baseUrl;
    }
    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小不能超过10MB");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名不能为空");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        boolean allowed = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equals(extension)) {
                allowed = true;
                break;
            }
        }
        
        if (!allowed) {
            throw new RuntimeException("不支持的文件类型");
        }
    }
    
    public String uploadAvatar(MultipartFile file) {
        validateFile(file);
        
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = "avatars/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) 
                               + "/" + UUID.randomUUID().toString() + extension;
            
            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                
                ossClient.putObject(bucketName, newFilename, inputStream, metadata);
                ossClient.setObjectAcl(bucketName, newFilename, com.aliyun.oss.model.CannedAccessControlList.PublicRead);
            }
            
            return baseUrl + "/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("上传头像失败", e);
        }
    }
    
    public String uploadImage(MultipartFile file) {
        validateFile(file);
        
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = "images/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) 
                               + "/" + UUID.randomUUID().toString() + extension;
            
            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                
                ossClient.putObject(bucketName, newFilename, inputStream, metadata);
                ossClient.setObjectAcl(bucketName, newFilename, com.aliyun.oss.model.CannedAccessControlList.PublicRead);
            }
            
            return baseUrl + "/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("上传图片失败", e);
        }
    }
}
