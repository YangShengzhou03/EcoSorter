package com.ecosorter.config;

import com.ecosorter.model.TrashcanData;
import com.ecosorter.model.User;
import com.ecosorter.repository.TrashcanDataRepository;
import com.ecosorter.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final String BEARER_PREFIX = "Bearer ";
    
    private final UserRepository userRepository;
    private final TrashcanDataRepository trashcanDataRepository;
    private final JwtUtil jwtUtil;
    
    public JwtAuthenticationFilter(UserRepository userRepository, 
                                  TrashcanDataRepository trashcanDataRepository,
                                  JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.trashcanDataRepository = trashcanDataRepository;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            
            try {
                if (jwtUtil.validateToken(token)) {
                    String tokenType = jwtUtil.getTokenType(token);
                    
                    if ("device".equals(tokenType)) {
                        authenticateDevice(token, request);
                    } else {
                        authenticateUser(token, request);
                    }
                }
            } catch (Exception e) {
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private void authenticateUser(String token, HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        User user = userRepository.findById(userId).orElse(null);
        
        if (user != null && user.getIsActive()) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
    
    private void authenticateDevice(String token, HttpServletRequest request) {
        String deviceId = jwtUtil.getDeviceIdFromToken(token);
        
        TrashcanData trashcan = trashcanDataRepository.findByDeviceId(deviceId);
        
        if (trashcan != null && "online".equals(trashcan.getStatus())) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    trashcan,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEVICE"))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            trashcan.setLastActive(java.time.LocalDateTime.now());
            trashcanDataRepository.updateById(trashcan);
        }
    }
}
