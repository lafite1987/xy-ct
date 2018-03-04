package com.lfyun.xy_ct.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.UserEntity;

public interface UserService extends IService<UserEntity> {

	UserEntity getByOpenid(String openid);
	
	void addUserBalance(Long userId, Double balance);
	
	void withdraw(Long userId);
	
	Map<Long, UserEntity> getByIds(List<Long> userIds);
}
