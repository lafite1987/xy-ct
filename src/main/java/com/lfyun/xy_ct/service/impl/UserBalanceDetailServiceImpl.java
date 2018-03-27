package com.lfyun.xy_ct.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.exception.AppException;
import com.lfyun.xy_ct.mapper.UserBalanceDetailMapper;
import com.lfyun.xy_ct.mapper.UserMapper;
import com.lfyun.xy_ct.service.UserBalanceDetailService;
import com.lfyun.xy_ct.service.UserService;

@Service
public class UserBalanceDetailServiceImpl extends ServiceImpl<UserBalanceDetailMapper,UserBalanceDetailEntity> implements UserBalanceDetailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserBalanceDetailServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<UserBalanceDetailEntity> list(Long userId) {
		UserBalanceDetailEntity entity = new UserBalanceDetailEntity();
		entity.setUserId(userId);
		EntityWrapper<UserBalanceDetailEntity> wrapper = new EntityWrapper<UserBalanceDetailEntity>(entity);
		List<UserBalanceDetailEntity> list = this.baseMapper.selectList(wrapper);
		return list;
	}
	
	@Transactional
	public void chargeOff(Long accountId, Double amount, String data) {
		Long userId = JwtToken.verify(data);
		if(userId == 0) {
			throw new AppException(ExceptionCodeEnums.USER_DATA_ERROR);
		}
		LOGGER.info("消费 accountId:{} userId:{} amount:{}", accountId, userId, amount);
		boolean result = userService.consume(userId, amount);
		if(result) {
			UserEntity userEntity = userService.selectById(userId);
			UserBalanceDetailEntity userBalanceDetailEntity = new UserBalanceDetailEntity();
			userBalanceDetailEntity.setUserId(userId);
			userBalanceDetailEntity.setAmount(amount);
			userBalanceDetailEntity.setBalance(userEntity.getBalance());
			userBalanceDetailEntity.setType(2);
			userBalanceDetailEntity.setBusinessType(2);
			userBalanceDetailEntity.setState(1);
			
			this.baseMapper.insert(userBalanceDetailEntity);
		} else {
			throw new AppException(ExceptionCodeEnums.USER_BALANCE_NOT_ENOUGH);
		}
	}
	
	@Autowired
	private UserMapper userMapper;
	
	@Transactional
	public void refund(Long accountId, Double amount, String data) {
		Long userId = JwtToken.verify(data);
		LOGGER.info("退款 accountId:{} userId:{} amount:{}", accountId, userId, amount);
		if(userId != 0) {
			int ret = userMapper.refund(userId, amount);
			if(ret > 0) {
				UserEntity userEntity = userService.selectById(userId);
				UserBalanceDetailEntity userBalanceDetailEntity = new UserBalanceDetailEntity();
				userBalanceDetailEntity.setUserId(userId);
				userBalanceDetailEntity.setAmount(amount);
				userBalanceDetailEntity.setBalance(userEntity.getBalance());
				userBalanceDetailEntity.setType(1);
				userBalanceDetailEntity.setBusinessType(3);
				userBalanceDetailEntity.setState(1);
				
				this.baseMapper.insert(userBalanceDetailEntity);
			}
		}
	}

}
