package com.ecosorter.controller;

import com.ecosorter.dto.NoticeRequest;
import com.ecosorter.dto.NoticeResponse;
import com.ecosorter.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {
    
    private final NoticeService noticeService;
    
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    
    @GetMapping
    public ResponseEntity<Page<NoticeResponse>> getAllNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(noticeService.getAllNotices(page, pageSize, keyword));
    }
    
    @GetMapping("/published")
    public ResponseEntity<List<NoticeResponse>> getPublishedNotices() {
        return ResponseEntity.ok(noticeService.getPublishedNotices());
    }
    
    @GetMapping("/unread/count")
    public ResponseEntity<Integer> getUnreadCount(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        Integer count = noticeService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }
    
    @PostMapping
    public ResponseEntity<NoticeResponse> createNotice(@RequestBody NoticeRequest request) {
        return ResponseEntity.ok(noticeService.createNotice(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> updateNotice(@PathVariable Long id, @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(noticeService.updateNotice(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
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
