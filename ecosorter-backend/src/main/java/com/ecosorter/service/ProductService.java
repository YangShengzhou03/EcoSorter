package com.ecosorter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecosorter.dto.ProductRequest;
import com.ecosorter.dto.ProductResponse;
import com.ecosorter.enums.ProductStatus;
import com.ecosorter.exception.ResourceNotFoundException;
import com.ecosorter.exception.BadRequestException;
import com.ecosorter.model.Product;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    
    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    
    public IPage<ProductResponse> getAllProducts(int page, int pageSize, String category, String status, Long userId) {
        Page<Product> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .orderByDesc(Product::getCreatedAt);

        if (category != null && !category.trim().isEmpty()) {
            wrapper.eq(Product::getCategory, category.trim());
        }
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Product::getStatus, status.trim());
        }

        IPage<Product> productPage = productRepository.selectPage(mpPage, wrapper);

        List<Long> productIds = productPage.getRecords().stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> totalPurchasedByProductId = getTotalPurchasedByProductIds(productIds);
        Map<Long, Integer> userPurchasedByProductId = userId != null 
                ? getUserPurchasedByProductIds(productIds, userId) 
                : Map.of();

        Page<ProductResponse> responsePage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        responsePage.setRecords(
                productPage.getRecords().stream()
                        .map(product -> convertToResponse(
                            product, 
                            totalPurchasedByProductId.getOrDefault(product.getId(), 0),
                            userPurchasedByProductId.getOrDefault(product.getId(), 0)))
                        .collect(Collectors.toList())
        );
        return responsePage;
    }
    
    public ProductResponse getProductById(Long id, Long userId) {
        Product product = productRepository.selectById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        Map<Long, Integer> totalPurchasedByProductId = getTotalPurchasedByProductIds(List.of(product.getId()));
        int userPurchased = 0;
        if (userId != null) {
            Map<Long, Integer> userPurchasedByProductId = getUserPurchasedByProductIds(List.of(product.getId()), userId);
            userPurchased = userPurchasedByProductId.getOrDefault(product.getId(), 0);
        }
        return convertToResponse(product, totalPurchasedByProductId.getOrDefault(product.getId(), 0), userPurchased);
    }
    
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setPoints(request.getPoints());
        product.setStock(request.getStock());
        product.setMaxPurchase(request.getMaxPurchase());
        if (request.getStatus() != null) {
            product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase()));
        }
        product.setCategory(request.getCategory());
        
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        productRepository.insert(product);
        return convertToResponse(product, 0, 0);
    }
    
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = productRepository.selectById(id);
        if (existingProduct == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setPoints(request.getPoints());
        existingProduct.setStock(request.getStock());
        existingProduct.setMaxPurchase(request.getMaxPurchase());
        if (request.getStatus() != null) {
            existingProduct.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase()));
        }
        existingProduct.setCategory(request.getCategory());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        productRepository.updateById(existingProduct);
        Map<Long, Integer> totalPurchasedByProductId = getTotalPurchasedByProductIds(List.of(existingProduct.getId()));
        return convertToResponse(existingProduct, totalPurchasedByProductId.getOrDefault(existingProduct.getId(), 0), 0);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.selectById(id);
        if (existingProduct == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
    
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.selectById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Invalid quantity");
        }
        product.setStock(product.getStock() - quantity);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.updateById(product);
    }
    
    private Map<Long, Integer> getTotalPurchasedByProductIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Map.of();
        }

        QueryWrapper<com.ecosorter.model.Order> wrapper = new QueryWrapper<>();
        wrapper.select("product_id as productId", "IFNULL(SUM(quantity), 0) as totalPurchased")
                .in("product_id", productIds)
                .ne("status", "cancelled")
                .groupBy("product_id");

        List<Map<String, Object>> rows = orderRepository.selectMaps(wrapper);
        Map<Long, Integer> result = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Object productIdObj = row.get("productId");
            Object totalPurchasedObj = row.get("totalPurchased");
            if (productIdObj == null) {
                continue;
            }
            Long productId = ((Number) productIdObj).longValue();
            int totalPurchased = totalPurchasedObj == null ? 0 : ((Number) totalPurchasedObj).intValue();
            result.put(productId, totalPurchased);
        }
        return result;
    }

    private Map<Long, Integer> getUserPurchasedByProductIds(List<Long> productIds, Long userId) {
        if (productIds == null || productIds.isEmpty() || userId == null) {
            return Map.of();
        }

        QueryWrapper<com.ecosorter.model.Order> wrapper = new QueryWrapper<>();
        wrapper.select("product_id as productId", "IFNULL(SUM(quantity), 0) as userPurchased")
                .in("product_id", productIds)
                .eq("user_id", userId)
                .ne("status", "cancelled")
                .groupBy("product_id");

        List<Map<String, Object>> rows = orderRepository.selectMaps(wrapper);
        Map<Long, Integer> result = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Object productIdObj = row.get("productId");
            Object userPurchasedObj = row.get("userPurchased");
            if (productIdObj == null) {
                continue;
            }
            Long productId = ((Number) productIdObj).longValue();
            int userPurchased = userPurchasedObj == null ? 0 : ((Number) userPurchasedObj).intValue();
            result.put(productId, userPurchased);
        }
        return result;
    }

    private ProductResponse convertToResponse(Product product, Integer totalPurchased, Integer userPurchased) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setPoints(product.getPoints());
        response.setStock(product.getStock());
        response.setMaxPurchase(product.getMaxPurchase());
        response.setStatus(product.getStatus() != null ? product.getStatus().name().toLowerCase() : null);
        response.setCategory(product.getCategory());
        response.setTotalPurchased(totalPurchased != null ? totalPurchased : 0);
        response.setUserPurchased(userPurchased != null ? userPurchased : 0);
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
