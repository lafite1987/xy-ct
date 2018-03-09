package com.lfyun.xy_ct.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.mapper.UserBalanceDetailMapper;
import com.lfyun.xy_ct.service.UserBalanceDetailService;

@Service
public class UserBalanceDetailServiceImpl extends ServiceImpl<UserBalanceDetailMapper,UserBalanceDetailEntity> implements UserBalanceDetailService {

	@Override
	public List<UserBalanceDetailEntity> list(Long userId) {
		UserBalanceDetailEntity entity = new UserBalanceDetailEntity();
		entity.setUserId(userId);
		EntityWrapper<UserBalanceDetailEntity> wrapper = new EntityWrapper<UserBalanceDetailEntity>(entity);
		List<UserBalanceDetailEntity> list = this.baseMapper.selectList(wrapper);
		return list;
	}

}
