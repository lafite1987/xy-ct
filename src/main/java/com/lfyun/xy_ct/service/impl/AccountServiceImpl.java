package com.lfyun.xy_ct.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.common.util.MessageDigestUtil;
import com.lfyun.xy_ct.dto.UserToken;
import com.lfyun.xy_ct.entity.AccountEntity;
import com.lfyun.xy_ct.mapper.AccountMapper;
import com.lfyun.xy_ct.service.AccountService;

import jersey.repackaged.com.google.common.collect.Maps;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper,AccountEntity> implements AccountService {

	@Override
	public UserToken login(String username, String password) {
		AccountEntity entity = new AccountEntity();
		entity.setUsername(username);
		EntityWrapper<AccountEntity> wrapper = new EntityWrapper<AccountEntity>(entity);
		AccountEntity accountEntity = selectOne(wrapper);
		if(accountEntity == null) {
			return null;
		}
		if(!accountEntity.getPassword().equals(MessageDigestUtil.getSHA256(password))) {
			return null;
		}
		String token = JwtToken.createToken(accountEntity.getId());
		UserToken userToken = new UserToken();
		userToken.setId(accountEntity.getId());
		userToken.setUsername(username);
		userToken.setPhone(accountEntity.getPhone());
		userToken.setToken(token);
		userToken.setCreateTime(accountEntity.getCreateTime());
		return userToken;
	}

	@Override
	public Map<Long, AccountEntity> getByBatchId(List<Long> userIdList) {
		if(userIdList == null || userIdList.isEmpty()) {
			return Maps.newHashMap();
		}
		List<AccountEntity> selectBatchIds = selectBatchIds(userIdList);
		Map<Long, AccountEntity> map = Maps.newHashMap();
		for(AccountEntity entity : selectBatchIds) {
			map.put(entity.getId(), entity);
		}
		return map;
	}
}
