package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.OrderResponse;
import com.ecosorter.enums.OrderStatus;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.model.Order;
import com.ecosorter.model.Product;
import com.ecosorter.model.User;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.repository.ProductRepository;
import com.ecosorter.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    
    public IPage<OrderResponse> getUserOrders(Long userId, int page, int pageSize, String status) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Page<Order> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt);
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Order::getStatus, OrderStatus.valueOf(status.toUpperCase()));
        }

        IPage<Order> orderPage = orderRepository.selectPage(mpPage, wrapper);
        return toResponsePage(orderPage);
    }
    
    public IPage<OrderResponse> getAllOrders(int page, int pageSize, String status) {
        Page<Order> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .orderByDesc(Order::getCreatedAt);
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Order::getStatus, OrderStatus.valueOf(status.toUpperCase()));
        }
        IPage<Order> orderPage = orderRepository.selectPage(mpPage, wrapper);
        return toResponsePage(orderPage);
    }
    
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.selectById(id);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found");
        }
        return convertToResponse(order);
    }
    
    @Transactional
    public OrderResponse createOrder(Long userId, Order order) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (order == null || order.getProductId() == null) {
            throw new BadRequestException("Product is required");
        }
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        Product product = productRepository.selectById(order.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        
        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Integer totalPoints = product.getPoints() * order.getQuantity();
        
        Integer currentPoints = pointService.getUserTotalPoints(userId);
        if (currentPoints < totalPoints) {
            throw new RuntimeException("Insufficient points");
        }

        Integer purchasedQuantity = getPurchasedQuantity(userId, product.getId());
        
        if (product.getMaxPurchase() != null && purchasedQuantity + order.getQuantity() > product.getMaxPurchase()) {
            throw new RuntimeException("Purchase limit exceeded. Maximum purchase: " + product.getMaxPurchase());
        }
        
        product.setStock(product.getStock() - order.getQuantity());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.updateById(product);

        order.setUserId(user.getId());
        order.setProductId(product.getId());
        order.setTotalPoints(totalPoints);
        order.setStatus(OrderStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        orderRepository.insert(order);

        pointService.deductPoints(userId, totalPoints, "order", order.getId(),
            "兑换商品: " + product.getName() + " x" + order.getQuantity());
        
        return convertToResponse(order);
    }
    
    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order existingOrder = orderRepository.selectById(id);
        if (existingOrder == null) {
            throw new ResourceNotFoundException("Order not found");
        }
        
        existingOrder.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        existingOrder.setUpdatedAt(LocalDateTime.now());
        orderRepository.updateById(existingOrder);
        return convertToResponse(existingOrder);
    }
    
    @Transactional
    public OrderResponse updateTrackingNumber(Long id, String trackingNumber) {
        Order existingOrder = orderRepository.selectById(id);
        if (existingOrder == null) {
            throw new ResourceNotFoundException("Order not found");
        }
        
        existingOrder.setTrackingNumber(trackingNumber);
        existingOrder.setUpdatedAt(LocalDateTime.now());
        orderRepository.updateById(existingOrder);
        return convertToResponse(existingOrder);
    }
    
    private Integer getPurchasedQuantity(Long userId, Long productId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(SUM(quantity), 0) as purchasedQuantity")
                .eq("user_id", userId)
                .eq("product_id", productId);
        List<Map<String, Object>> rows = orderRepository.selectMaps(wrapper);
        if (rows.isEmpty()) {
            return 0;
        }
        Object value = rows.get(0).get("purchasedQuantity");
        return value == null ? 0 : ((Number) value).intValue();
    }

    private IPage<OrderResponse> toResponsePage(IPage<Order> orderPage) {
        List<Long> productIds = orderPage.getRecords().stream()
                .map(Order::getProductId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Product> productsById = productIds.isEmpty()
                ? Map.of()
                : productRepository.selectBatchIds(productIds).stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));

        Page<OrderResponse> responsePage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        responsePage.setRecords(orderPage.getRecords().stream()
                .map(order -> convertToResponse(order, productsById.get(order.getProductId())))
                .collect(Collectors.toList()));
        return responsePage;
    }

    private OrderResponse convertToResponse(Order order) {
        Product product = order.getProductId() == null ? null : productRepository.selectById(order.getProductId());
        return convertToResponse(order, product);
    }

    private OrderResponse convertToResponse(Order order, Product product) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setProductId(order.getProductId());
        response.setProductName(product != null ? product.getName() : null);
        response.setProductImageUrl(product != null ? product.getImageUrl() : null);
        response.setQuantity(order.getQuantity());
        response.setTotalPoints(order.getTotalPoints());
        response.setContactName(order.getContactName());
        response.setContactPhone(order.getContactPhone());
        response.setShippingAddress(order.getShippingAddress());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setStatus(order.getStatus().name().toLowerCase());
        response.setRemark(order.getRemark());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
