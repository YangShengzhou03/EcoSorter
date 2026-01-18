package com.ecosorter.service;

import com.ecosorter.dto.ProductRequest;
import com.ecosorter.dto.ProductResponse;
import com.ecosorter.model.Product;
import com.ecosorter.repository.OrderRepository;
import com.ecosorter.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    
    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    
    public Page<ProductResponse> getAllProducts(int page, int pageSize, String category, String status) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage;
        
        if (category != null && !category.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
            productPage = productRepository.findByStatusAndCategory(status, category, pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            productPage = productRepository.findByCategory(category, pageable);
        } else if (status != null && !status.trim().isEmpty()) {
            productPage = productRepository.findByStatus(status, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        
        return productPage.map(this::convertToResponse);
    }
    
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Product not found"));
        return convertToResponse(product);
    }
    
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setImages(request.getImages());
        product.setPoints(request.getPoints());
        product.setStock(request.getStock());
        product.setMaxPurchase(request.getMaxPurchase());
        product.setStatus(request.getStatus());
        product.setCategory(request.getCategory());
        
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }
    
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Product not found"));
        
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setImages(request.getImages());
        existingProduct.setPoints(request.getPoints());
        existingProduct.setStock(request.getStock());
        existingProduct.setMaxPurchase(request.getMaxPurchase());
        existingProduct.setStatus(request.getStatus());
        existingProduct.setCategory(request.getCategory());
        
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToResponse(updatedProduct);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new com.ecosorter.exception.ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
    
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new com.ecosorter.exception.ResourceNotFoundException("Product not found"));
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
    
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setImages(product.getImages());
        response.setPoints(product.getPoints());
        response.setStock(product.getStock());
        response.setMaxPurchase(product.getMaxPurchase());
        response.setStatus(product.getStatus());
        response.setCategory(product.getCategory());
        response.setTotalPurchased(orderRepository.getTotalPurchasedByProductId(product.getId()));
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
