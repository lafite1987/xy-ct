package com.lfyun.xy_ct.service;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.entity.OrderEntity;

public interface OrderService extends IService<OrderEntity> {

	void pay(OrderDTO orderDTO);
	
	OrderDTO getByOrderId(String orderId);
}
