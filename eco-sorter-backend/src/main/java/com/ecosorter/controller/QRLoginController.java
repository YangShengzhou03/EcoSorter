package com.ecosorter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecosorter.dto.QRCodeScanRequest;
import com.ecosorter.dto.QRCodeSessionResponse;
import com.ecosorter.model.QRLoginSession;
import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.QRLoginSessionRepository;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import com.ecosorter.config.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/qr-login")
public class QRLoginController {
    
    private final QRLoginSessionRepository qrLoginSessionRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    
    public QRLoginController(QRLoginSessionRepository qrLoginSessionRepository,
                             TrashcanDataRepository trashcanDataRepository,
                             UserRepository userRepository,
                             JwtUtil jwtUtil) {
        this.qrLoginSessionRepository = qrLoginSessionRepository;
        this.trashcanDataRepository = trashcanDataRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/create")
    public ResponseEntity<QRCodeSessionResponse> createQRSession(
            @AuthenticationPrincipal TrashcanData trashcan) {
        String qrCode = UUID.randomUUID().toString();
        
        QRLoginSession session = new QRLoginSession();
        session.setQrCode(qrCode);
        session.setTrashcanId(trashcan.getId());
        session.setStatus("pending");
        session.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        
        qrLoginSessionRepository.insert(session);
        
        return ResponseEntity.ok(new QRCodeSessionResponse(qrCode, "pending"));
    }
    
    @GetMapping("/status/{qrCode}")
    public ResponseEntity<QRCodeSessionResponse> checkStatus(
            @PathVariable String qrCode,
            @AuthenticationPrincipal TrashcanData trashcan) {
        QRLoginSession session = qrLoginSessionRepository.selectOne(
            new LambdaQueryWrapper<QRLoginSession>()
                .eq(QRLoginSession::getQrCode, qrCode)
                .eq(QRLoginSession::getTrashcanId, trashcan.getId())
        );
        
        if (session == null) {
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "expired", "二维码不存在"));
        }
        
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("expired");
            qrLoginSessionRepository.updateById(session);
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "expired", "二维码已过期"));
        }
        
        if ("confirmed".equals(session.getStatus())) {
            User user = userRepository.selectById(session.getUserId());
            if (user != null) {
                String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
                return ResponseEntity.ok(new QRCodeSessionResponse(token, "confirmed", "登录成功"));
            }
        }
        
        return ResponseEntity.ok(new QRCodeSessionResponse(null, session.getStatus()));
    }
    
    @PostMapping("/scan")
    public ResponseEntity<QRCodeSessionResponse> scanQRCode(
            @Valid @RequestBody QRCodeScanRequest request,
            @AuthenticationPrincipal User user) {
        QRLoginSession session = qrLoginSessionRepository.selectOne(
            new LambdaQueryWrapper<QRLoginSession>()
                .eq(QRLoginSession::getQrCode, request.getQrCode())
        );
        
        if (session == null) {
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "expired", "二维码不存在"));
        }
        
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("expired");
            qrLoginSessionRepository.updateById(session);
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "expired", "二维码已过期"));
        }
        
        if (!"pending".equals(session.getStatus())) {
            return ResponseEntity.ok(new QRCodeSessionResponse(null, session.getStatus()));
        }
        
        session.setUserId(user.getId());
        session.setScannedAt(LocalDateTime.now());
        qrLoginSessionRepository.updateById(session);
        
        return ResponseEntity.ok(new QRCodeSessionResponse(null, "scanned", "请确认登录"));
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<QRCodeSessionResponse> confirmLogin(
            @Valid @RequestBody QRCodeScanRequest request,
            @AuthenticationPrincipal User user) {
        QRLoginSession session = qrLoginSessionRepository.selectOne(
            new LambdaQueryWrapper<QRLoginSession>()
                .eq(QRLoginSession::getQrCode, request.getQrCode())
        );
        
        if (session == null) {
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "expired", "二维码不存在"));
        }
        
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("expired");
            qrLoginSessionRepository.updateById(session);
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "expired", "二维码已过期"));
        }
        
        if (!"scanned".equals(session.getStatus())) {
            return ResponseEntity.ok(new QRCodeSessionResponse(null, session.getStatus()));
        }
        
        if (!session.getUserId().equals(user.getId())) {
            return ResponseEntity.ok(new QRCodeSessionResponse(null, "error", "用户不匹配"));
        }
        
        session.setStatus("confirmed");
        session.setConfirmedAt(LocalDateTime.now());
        qrLoginSessionRepository.updateById(session);
        
        User loggedInUser = userRepository.selectById(session.getUserId());
        if (loggedInUser != null) {
            String token = jwtUtil.generateToken(loggedInUser.getId(), loggedInUser.getUsername(), loggedInUser.getRole().name());
            return ResponseEntity.ok(new QRCodeSessionResponse(token, "confirmed", "登录成功"));
        }
        
        return ResponseEntity.ok(new QRCodeSessionResponse(null, "error", "登录失败"));
    }
}
