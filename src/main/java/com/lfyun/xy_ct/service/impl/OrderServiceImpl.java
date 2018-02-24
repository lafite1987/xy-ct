package com.lfyun.xy_ct.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.entity.OrderEntity;
import com.lfyun.xy_ct.mapper.OrderMapper;
import com.lfyun.xy_ct.service.OrderService;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,OrderEntity> implements OrderService {

	@Override
	public void pay(OrderDTO orderDTO) {
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(Long.parseLong(orderDTO.getOrderId()));
		orderEntity.setPayStatus(PayStatusEnums.FINISH.getCode());
		this.updateById(orderEntity);
	}

	@Override
	public OrderDTO getByOrderId(String orderId) {
		OrderEntity entity = new OrderEntity();
		entity.setId(Long.parseLong(orderId));
		OrderEntity orderEntity = baseMapper.selectOne(entity);
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(orderId);
		orderDTO.setOrderDesc(orderEntity.getProductName());
		orderDTO.setBuyerOpenid(orderEntity.getOpenid());
		orderDTO.setOrderAmount(new BigDecimal(orderEntity.getAmount()));
		return orderDTO;
	}

}
