package com.ecosorter.service;

import com.ecosorter.dto.NoticeRequest;
import com.ecosorter.dto.NoticeResponse;
import com.ecosorter.model.Notice;
import com.ecosorter.model.User;
import com.ecosorter.repository.NoticeRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }
    
    public Page<NoticeResponse> getAllNotices(int page, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notice> noticePage;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Notice> notices = noticeRepository.searchNotices(keyword);
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, notices.size());
            List<Notice> pagedNotices = start < notices.size() ? notices.subList(start, end) : List.of();
            noticePage = new org.springframework.data.domain.PageImpl<>(pagedNotices, pageable, notices.size());
        } else {
            noticePage = noticeRepository.findAll(pageable);
        }
        
        return noticePage.map(this::convertToResponse);
    }
    
    public NoticeResponse getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Notice not found"));
        return convertToResponse(notice);
    }
    
    public NoticeResponse createNotice(NoticeRequest request) {
        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setStatus(request.getStatus());
        
        if (request.getAuthorId() != null) {
            User author = userRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Author not found"));
            notice.setAuthor(author);
        }
        
        Notice savedNotice = noticeRepository.save(notice);
        return convertToResponse(savedNotice);
    }
    
    public NoticeResponse updateNotice(Long id, NoticeRequest request) {
        Notice existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Notice not found"));
        
        existingNotice.setTitle(request.getTitle());
        existingNotice.setContent(request.getContent());
        existingNotice.setStatus(request.getStatus());
        
        if (request.getAuthorId() != null) {
            User author = userRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Author not found"));
            existingNotice.setAuthor(author);
        }
        
        Notice updatedNotice = noticeRepository.save(existingNotice);
        return convertToResponse(updatedNotice);
    }
    
    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new com.ecosorter.exception.ResourceNotFoundException("Notice not found");
        }
        noticeRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<NoticeResponse> getPublishedNotices() {
        List<Notice> notices = noticeRepository.findByStatus("published");
        return notices.stream()
                .limit(5)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public Integer getUnreadCount(Long userId) {
        return 0;
    }
    
    private NoticeResponse convertToResponse(Notice notice) {
        NoticeResponse response = new NoticeResponse();
        response.setId(notice.getId());
        response.setTitle(notice.getTitle());
        response.setContent(notice.getContent());
        response.setStatus(notice.getStatus());
        response.setAuthorId(notice.getAuthor() != null ? notice.getAuthor().getId() : null);
        response.setCreatedAt(notice.getCreatedAt());
        response.setUpdatedAt(notice.getUpdatedAt());
        return response;
    }
}
