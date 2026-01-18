package com.ecosorter.service;

import com.ecosorter.dto.OrderResponse;
import com.ecosorter.model.Order;
import com.ecosorter.model.Product;
import com.ecosorter.model.User;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.repository.ProductRepository;
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
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PointService pointService;
    
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, PointService pointService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pointService = pointService;
    }
    
    public Page<OrderResponse> getUserOrders(Long userId, int page, int pageSize, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Order> orderPage;
        if (status != null && !status.trim().isEmpty()) {
            orderPage = orderRepository.findByUserAndStatus(user, status, pageable);
        } else {
            orderPage = orderRepository.findByUser(user, pageable);
        }
        
        return orderPage.map(this::convertToResponse);
    }
    
    public Page<OrderResponse> getAllOrders(int page, int pageSize, String status) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Order> orderPage;
        if (status != null && !status.trim().isEmpty()) {
            orderPage = orderRepository.findByStatus(status, pageable);
        } else {
            orderPage = orderRepository.findAll(pageable);
        }
        
        return orderPage.map(this::convertToResponse);
    }
    
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Order not found"));
        return convertToResponse(order);
    }
    
    @Transactional
    public OrderResponse createOrder(Long userId, Order order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("User not found"));
        
        Product product = productRepository.findById(order.getProduct().getId())
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Product not found"));
        
        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Integer totalPoints = product.getPoints() * order.getQuantity();
        
        Integer currentPoints = pointService.getUserTotalPoints(userId);
        if (currentPoints < totalPoints) {
            throw new RuntimeException("Insufficient points");
        }
        
        List<Order> existingOrders = orderRepository.findByUserAndProduct(user, product);
        Integer purchasedQuantity = existingOrders.stream()
                .mapToInt(Order::getQuantity)
                .sum();
        
        if (product.getMaxPurchase() != null && purchasedQuantity + order.getQuantity() > product.getMaxPurchase()) {
            throw new RuntimeException("Purchase limit exceeded. Maximum purchase: " + product.getMaxPurchase());
        }
        
        product.setStock(product.getStock() - order.getQuantity());
        productRepository.save(product);
        
        order.setUser(user);
        order.setProduct(product);
        order.setTotalPoints(totalPoints);
        order.setStatus("pending");
        
        Order savedOrder = orderRepository.save(order);
        
        pointService.deductPoints(userId, totalPoints, "order", savedOrder.getId(), 
            "兑换商品: " + product.getName() + " x" + order.getQuantity());
        
        return convertToResponse(savedOrder);
    }
    
    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Order not found"));
        
        existingOrder.setStatus(status);
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToResponse(updatedOrder);
    }
    
    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setProductId(order.getProduct().getId());
        response.setProductName(order.getProduct().getName());
        response.setProductImageUrl(order.getProduct().getImageUrl());
        response.setQuantity(order.getQuantity());
        response.setTotalPoints(order.getTotalPoints());
        response.setContactName(order.getContactName());
        response.setContactPhone(order.getContactPhone());
        response.setShippingAddress(order.getShippingAddress());
        response.setStatus(order.getStatus());
        response.setRemark(order.getRemark());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
