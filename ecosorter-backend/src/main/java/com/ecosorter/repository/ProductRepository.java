package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductRepository extends BaseMapper<Product> {
    
    default Product save(Product product) {
        if (product.getId() == null) {
            insert(product);
        } else {
            updateById(product);
        }
        return product;
    }
    
    default void deleteById(Long id) {
        BaseMapper.super.deleteById(id);
    }
}
