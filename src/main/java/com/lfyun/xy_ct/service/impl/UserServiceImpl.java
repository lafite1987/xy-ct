package com.lfyun.xy_ct.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.lfyun.xy_ct.common.wx.WeiXinResultXMLBean;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.entity.UserEarningEntity;
import com.lfyun.xy_ct.mapper.UserMapper;
import com.lfyun.xy_ct.service.UserService;
import com.lfyun.xy_ct.service.WeixinFirmPaymentService;
import com.lfyun.xy_ct.service.UserEarningService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity> implements UserService {

	@Autowired
	private UserEarningService withdrawService;
	
	@Autowired
	private WeixinFirmPaymentService weixinFirmPaymentService;
	
	@Override
	public UserEntity getByOpenid(String openid) {
		UserEntity entity = new UserEntity();
		entity.setOpenid(openid);
		return baseMapper.selectOne(entity);
	}

	@Override
	public void addUserBalance(Long userId, Double balance) {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(userId);
		userEntity.setBalance(balance);
		baseMapper.updateById(userEntity);
		
	}

	@Override
	public void withdraw(Long userId) {
		UserEntity userEntity = selectById(userId);
		if(userEntity != null && userEntity.getEarning() > 0) {
			UserEarningEntity withdrawEntity = new UserEarningEntity();
			withdrawEntity.setUserId(userId);
			withdrawEntity.setAmount(userEntity.getEarning());
			withdrawEntity.setState(1);
			withdrawService.insert(withdrawEntity);
			String tradeNo = String.valueOf(withdrawEntity.getId());
			WeiXinResultXMLBean weiXinResultXMLBean = weixinFirmPaymentService.firmPay(userEntity.getOpenid(), userEntity.getEarning(), "收益提现", tradeNo);
			if("SUCCESS".equals(weiXinResultXMLBean.getReturn_code()) && "SUCCESS".equals(weiXinResultXMLBean.getResult_code())) {
				UserEarningEntity updateWithdrawEntity = new UserEarningEntity();
				updateWithdrawEntity.setId(withdrawEntity.getId());
				updateWithdrawEntity.setState(3);
				withdrawService.updateById(updateWithdrawEntity);
			} else {
				UserEarningEntity updateWithdrawEntity = new UserEarningEntity();
				updateWithdrawEntity.setId(withdrawEntity.getId());
				updateWithdrawEntity.setState(4);
				withdrawService.updateById(updateWithdrawEntity);
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
	
}
