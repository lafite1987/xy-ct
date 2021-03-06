package com.lfyun.xy_ct.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;

public interface UserBalanceDetailService extends IService<UserBalanceDetailEntity> {

	List<UserBalanceDetailEntity> list(Long userId);
	
	void chargeOff(Long accountId, Double amount, String data);
	
	void refund(Long accountId, Double amount, String data);
}
