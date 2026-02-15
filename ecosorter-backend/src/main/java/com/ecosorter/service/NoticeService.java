package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.NoticeRequest;
import com.ecosorter.dto.NoticeResponse;
import com.ecosorter.enums.NoticeStatus;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.Notice;
import com.ecosorter.model.User;
import com.ecosorter.repository.NoticeRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }
    
    public IPage<NoticeResponse> getAllNotices(int page, int pageSize, String keyword) {
        Page<Notice> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<Notice>()
                .orderByDesc(Notice::getCreatedAt);

        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            wrapper.and(w -> w.like(Notice::getTitle, kw).or().like(Notice::getContent, kw));
        }

        IPage<Notice> noticePage = noticeRepository.selectPage(mpPage, wrapper);
        Page<NoticeResponse> responsePage = new Page<>(noticePage.getCurrent(), noticePage.getSize(), noticePage.getTotal());
        responsePage.setRecords(noticePage.getRecords().stream().map(this::convertToResponse).collect(Collectors.toList()));
        return responsePage;
    }
    
    public NoticeResponse getNoticeById(Long id) {
        Notice notice = noticeRepository.selectById(id);
        if (notice == null) {
            throw new ResourceNotFoundException("Notice not found");
        }
        return convertToResponse(notice);
    }
    
    public NoticeResponse createNotice(NoticeRequest request) {
        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setStatus(NoticeStatus.valueOf(request.getStatus().toUpperCase()));
        notice.setTargetAudience(request.getTargetAudience() != null ? request.getTargetAudience() : "all");
        
        if (request.getAuthorId() != null) {
            User author = userRepository.selectById(request.getAuthorId());
            if (author == null) {
                throw new ResourceNotFoundException("Author not found");
            }
            notice.setAuthorId(author.getId());
        }
        
        LocalDateTime now = LocalDateTime.now();
        notice.setCreatedAt(now);
        notice.setUpdatedAt(now);
        noticeRepository.insert(notice);
        return convertToResponse(notice);
    }
    
    public NoticeResponse updateNotice(Long id, NoticeRequest request) {
        Notice existingNotice = noticeRepository.selectById(id);
        if (existingNotice == null) {
            throw new ResourceNotFoundException("Notice not found");
        }
        
        existingNotice.setTitle(request.getTitle());
        existingNotice.setContent(request.getContent());
        existingNotice.setStatus(NoticeStatus.valueOf(request.getStatus().toUpperCase()));
        if (request.getTargetAudience() != null) {
            existingNotice.setTargetAudience(request.getTargetAudience());
        }
        
        if (request.getAuthorId() != null) {
            User author = userRepository.selectById(request.getAuthorId());
            if (author == null) {
                throw new ResourceNotFoundException("Author not found");
            }
            existingNotice.setAuthorId(author.getId());
        }
        
        existingNotice.setUpdatedAt(LocalDateTime.now());
        noticeRepository.updateById(existingNotice);
        return convertToResponse(existingNotice);
    }
    
    public void deleteNotice(Long id) {
        Notice existingNotice = noticeRepository.selectById(id);
        if (existingNotice == null) {
            throw new ResourceNotFoundException("Notice not found");
        }
        noticeRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<NoticeResponse> getPublishedNotices() {
        List<Notice> notices = noticeRepository.selectList(
                new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getStatus, NoticeStatus.PUBLISHED)
                        .orderByDesc(Notice::getCreatedAt)
                        .last("LIMIT 5")
        );
        return notices.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<NoticeResponse> getPublishedNoticesByRole(String role) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<Notice>()
                .eq(Notice::getStatus, NoticeStatus.PUBLISHED)
                .and(w -> w.eq(Notice::getTargetAudience, "all")
                        .or().eq(Notice::getTargetAudience, role))
                .orderByDesc(Notice::getCreatedAt)
                .last("LIMIT 5");
        
        List<Notice> notices = noticeRepository.selectList(wrapper);
        return notices.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private NoticeResponse convertToResponse(Notice notice) {
        NoticeResponse response = new NoticeResponse();
        response.setId(notice.getId());
        response.setTitle(notice.getTitle());
        response.setContent(notice.getContent());
        response.setStatus(notice.getStatus().name().toLowerCase());
        response.setTargetAudience(notice.getTargetAudience());
        response.setAuthorId(notice.getAuthorId());
        response.setCreatedAt(notice.getCreatedAt());
        response.setUpdatedAt(notice.getUpdatedAt());
        return response;
    }
}
