package com.lfyun.xy_ct.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lfyun.xy_ct.entity.UserEntity;

@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

	/**
	 * 给用户余额加钱
	 * @param userId
	 * @param balance
	 */
	int addUserBalance(@Param("userId")Long userId, @Param("balance")Double balance);
	/**
	 * 给用户添加收益
	 * @param userId
	 * @param earning
	 */
	void addUserEarning(@Param("userId")Long userId, @Param("earning")Double earning);
	/**
	 * 消费
	 * @param userId
	 * @param amount
	 * @return
	 */
	int consume(@Param("userId")Long userId, @Param("amount")Double amount);
	
	/**
	 * 退款
	 * @param userId
	 * @param amount
	 */
	int refund(@Param("userId")Long userId, @Param("amount")Double amount);
}
