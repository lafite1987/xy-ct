package com.lfyun.xy_ct.service;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.entity.OrderEntity;

public interface OrderService extends IService<OrderEntity> {

	void pay(OrderDTO orderDTO);
	
	OrderDTO getByOrderId(String orderId);
	
	/**
	 * 判断用户是否充值
	 * @param Long productId
	 * @param userId
	 * @return
	 */
	boolean isRecharge(Long productId, Long userId);
}
