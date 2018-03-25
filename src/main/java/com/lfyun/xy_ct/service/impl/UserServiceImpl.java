package com.lfyun.xy_ct.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.wx.WeiXinResultXMLBean;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.entity.UserEarningEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.exception.AppException;
import com.lfyun.xy_ct.mapper.UserMapper;
import com.lfyun.xy_ct.service.UserBalanceDetailService;
import com.lfyun.xy_ct.service.UserEarningService;
import com.lfyun.xy_ct.service.UserService;
import com.lfyun.xy_ct.service.WeixinFirmPaymentService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity> implements UserService {

	@Autowired
	private UserEarningService userEarningService;
	
	@Autowired
	private UserBalanceDetailService userBalanceDetailService;
	
	@Autowired
	private WeixinFirmPaymentService weixinFirmPaymentService;
	
	@Override
	public UserEntity getByOpenid(String openid) {
		UserEntity entity = new UserEntity();
		entity.setOpenid(openid);
		return baseMapper.selectOne(entity);
	}

	@Override
	public void addUserEarning(Long userId, Double earning) {
		this.baseMapper.addUserEarning(userId, earning);
	}
	@Override
	@Transactional
	public void addUserBalance(Long userId, Double amount, Long orderId) {
		UserEntity userEntity = this.selectById(userId);
		UserBalanceDetailEntity userBalanceDetailEntity = new UserBalanceDetailEntity();
		userBalanceDetailEntity.setUserId(userId);
		userBalanceDetailEntity.setBalance(userEntity.getBalance() + amount);
		userBalanceDetailEntity.setAmount(amount);
		userBalanceDetailEntity.setOrderId(orderId);
		userBalanceDetailEntity.setType(1);
		userBalanceDetailEntity.setBusinessType(1);
		userBalanceDetailEntity.setState(1);
		userBalanceDetailService.insert(userBalanceDetailEntity);
		this.baseMapper.addUserBalance(userId, amount);
		
	}

	@Override
	@Transactional
	public void withdraw(Long userId) {
		UserEntity userEntity = selectById(userId);
		if(userEntity != null && userEntity.getEarning() > 0) {
			int withdraw = this.baseMapper.withdraw(userId);
			if(withdraw > 0) {
				UserEarningEntity userEarningEntity = new UserEarningEntity();
				userEarningEntity.setUserId(userId);
				userEarningEntity.setType(2);
				userEarningEntity.setAmount(userEntity.getEarning());
				userEarningEntity.setState(1);
				userEarningService.insert(userEarningEntity);
				String tradeNo = String.valueOf(userEarningEntity.getId());
				WeiXinResultXMLBean weiXinResultXMLBean = weixinFirmPaymentService.firmPay(userEntity.getOpenid(), userEntity.getEarning(), "收益提现", tradeNo);
				if("SUCCESS".equals(weiXinResultXMLBean.getReturn_code()) && "SUCCESS".equals(weiXinResultXMLBean.getResult_code())) {
					UserEarningEntity updateWithdrawEntity = new UserEarningEntity();
					updateWithdrawEntity.setId(userEarningEntity.getId());
					updateWithdrawEntity.setState(3);
					userEarningService.updateById(updateWithdrawEntity);
				} else {
					UserEarningEntity updateWithdrawEntity = new UserEarningEntity();
					updateWithdrawEntity.setId(userEarningEntity.getId());
					updateWithdrawEntity.setState(4);
					userEarningService.updateById(updateWithdrawEntity);
					throw new AppException(ExceptionCodeEnums.USER_WITHDRAW_FAILED);
				}
			}
		}
	}

	@Override
	public Map<Long, UserEntity> getByIds(List<Long> userIds) {
		if(userIds == null || userIds.isEmpty()) {
			return Maps.newHashMap();
		}
		List<UserEntity> selectBatchIds = this.selectBatchIds(userIds);
		Map<Long, UserEntity> map = Maps.newHashMap();
		for(UserEntity userEntity : selectBatchIds) {
			map.put(userEntity.getId(), userEntity);
		}
		return map;
	}

	public boolean consume(Long userId, Double amount) {
		int result = this.baseMapper.consume(userId, amount);
		return result > 0 ? true : false;
	}
}
