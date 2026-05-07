package com.ecosorter.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class UploadService {

    private final OSS ossClient;
    private final String bucketName;
    private final String baseUrl;

    private static final long MAX_FILE_SIZE = 512 * 1024;
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};

    public UploadService(@Value("${aliyun.oss.endpoint}") String endpoint,
                        @Value("${aliyun.oss.access-key-id}") String accessKeyId,
                        @Value("${aliyun.oss.access-key-secret}") String accessKeySecret,
                        @Value("${aliyun.oss.bucket-name}") String bucketName,
                        @Value("${aliyun.oss.base-url}") String baseUrl) {
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        this.bucketName = bucketName;
        this.baseUrl = baseUrl;
    }

    @PreDestroy
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小不能超过512KB");
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
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String objectName = "avatars/" + datePath + "/" + UUID.randomUUID().toString() + extension;

            byte[] bytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream, metadata);
            ossClient.putObject(putObjectRequest);

            String fileUrl = baseUrl + "/" + objectName;
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("上传头像失败: " + e.getMessage(), e);
        }
    }

    public String uploadImage(MultipartFile file) {
        validateFile(file);

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String objectName = "images/" + datePath + "/" + UUID.randomUUID().toString() + extension;

            byte[] bytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream, metadata);
            ossClient.putObject(putObjectRequest);

            String fileUrl = baseUrl + "/" + objectName;
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("上传图片失败: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String objectName = fileUrl.substring(baseUrl.length() + 1);
            ossClient.deleteObject(bucketName, objectName);
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage(), e);
        }
    }
}
