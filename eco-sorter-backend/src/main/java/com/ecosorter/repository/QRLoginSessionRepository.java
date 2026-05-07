package com.ecosorter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecosorter.model.QRLoginSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QRLoginSessionRepository extends BaseMapper<QRLoginSession> {
}
