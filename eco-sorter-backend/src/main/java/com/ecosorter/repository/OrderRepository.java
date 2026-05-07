package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Order;
import com.ecosorter.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderRepository extends BaseMapper<Order> {
    
    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Order> findByUserId(Long userId);
    
    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Long countByStatus(OrderStatus status);
    
    default Optional<Order> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }
    
    default Order save(Order order) {
        if (order.getId() == null) {
            insert(order);
        } else {
            updateById(order);
        }
        return order;
    }
    
    default void deleteById(Long id) {
        BaseMapper.super.deleteById(id);
    }
}
