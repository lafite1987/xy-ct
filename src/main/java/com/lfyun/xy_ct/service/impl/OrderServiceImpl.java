package com.lfyun.xy_ct.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.entity.OrderEntity;
import com.lfyun.xy_ct.entity.ProductEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.mapper.OrderMapper;
import com.lfyun.xy_ct.service.OrderService;
import com.lfyun.xy_ct.service.ProductService;
import com.lfyun.xy_ct.service.UserCardService;
import com.lfyun.xy_ct.service.UserEarningService;
import com.lfyun.xy_ct.service.UserService;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,OrderEntity> implements OrderService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserEarningService userEarningService;
	
	@Autowired
	private UserCardService userCardService;
	
	@Autowired
	private ProductService productService;
	
	@Override
	@Transactional
	public void pay(OrderDTO orderDTO) {
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(Long.parseLong(orderDTO.getOrderId()));
		orderEntity.setOutTradeNo(orderDTO.getOutTradeNo());
		orderEntity.setPayFinishTime(orderDTO.getPayFinishTime());
		orderEntity.setPayStatus(PayStatusEnums.FINISH.getCode());
		this.updateById(orderEntity);
		OrderEntity order = selectById(orderEntity.getId());
		userService.addUserBalance(order.getUserId(), orderDTO.getOrderAmount().doubleValue(), order.getId());
		userEarningService.addEarning(orderEntity.getId());
		ProductEntity productEntity = productService.selectById(order.getProductId());
		if(productEntity != null) {
			userCardService.addUserCardByProductId(order.getUserId(), productEntity.getId());
		}
	}

	@Override
	public OrderDTO getByOrderId(String orderId) {
		OrderEntity entity = new OrderEntity();
		entity.setId(Long.parseLong(orderId));
		OrderEntity orderEntity = baseMapper.selectOne(entity);
		if(orderEntity != null) {
			UserEntity userEntity = userService.selectById(orderEntity.getUserId());
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setOrderId(orderId);
			orderDTO.setOrderDesc(orderEntity.getProductName());
			orderDTO.setBuyerOpenid(userEntity.getOpenid());
			orderDTO.setOrderAmount(new BigDecimal(orderEntity.getAmount()));
			orderDTO.setPayStatus(orderEntity.getPayStatus());
			orderDTO.setProductId(orderEntity.getProductId());
			return orderDTO;
		}
		return null;
	}

}
