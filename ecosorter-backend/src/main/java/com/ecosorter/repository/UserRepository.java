package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository extends BaseMapper<User> {
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(String username);
    
    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(String email);
    
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    boolean existsByUsername(String username);
    
    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);
    
    @Select("SELECT * FROM users WHERE role = #{role}")
    List<User> findByRole(String role);
    
    @Select("SELECT * FROM users")
    List<User> findAll();
    
    default Optional<User> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }
    
    default User save(User user) {
        if (user.getId() == null) {
            insert(user);
        } else {
            updateById(user);
        }
        return user;
    }
    
    default void delete(User user) {
        deleteById(user.getId());
    }
    
    default long count() {
        return selectCount(null);
    }
}
