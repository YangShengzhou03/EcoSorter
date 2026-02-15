package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingRepository extends BaseMapper<Booking> {
    
    @Select("SELECT * FROM bookings WHERE user_id = #{userId}")
    List<Booking> findByUserId(Long userId);
    
    @Select("SELECT * FROM bookings")
    List<Booking> findAll();
    
    default Booking save(Booking booking) {
        if (booking.getId() == null) {
            insert(booking);
        } else {
            updateById(booking);
        }
        return booking;
    }
}
