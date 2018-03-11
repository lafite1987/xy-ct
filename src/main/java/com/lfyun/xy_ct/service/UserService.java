package com.lfyun.xy_ct.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.UserEntity;

public interface UserService extends IService<UserEntity> {

	UserEntity getByOpenid(String openid);
	/**
	 * 给用户余额加钱
	 * @param userId
	 * @param balance
	 */
	void addUserBalance(Long userId, Double balance, Long orderId);
	/**
	 * 给用户添加收益
	 * @param userId
	 * @param earning
	 */
	void addUserEarning(Long userId, Double earning);
	
	void withdraw(Long userId);
	
	Map<Long, UserEntity> getByIds(List<Long> userIds);
	/**
	 * 消费
	 * @param userId
	 * @param amount
	 * @return
	 */
	boolean consume(Long userId, Double amount);
}
