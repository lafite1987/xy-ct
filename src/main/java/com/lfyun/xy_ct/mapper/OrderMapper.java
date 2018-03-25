package com.lfyun.xy_ct.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lfyun.xy_ct.entity.OrderEntity;

@Repository
public interface OrderMapper extends BaseMapper<OrderEntity> {

	Double getAmountByTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
	Double getTotalAmount();
}
