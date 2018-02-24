package com.lfyun.xy_ct.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.mapper.UserMapper;
import com.lfyun.xy_ct.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity> implements UserService {

	@Override
	public UserEntity getByOpenid(String openid) {
		UserEntity entity = new UserEntity();
		entity.setOpenid(openid);
		return baseMapper.selectOne(entity);
	}

	
}
