package com.lfyun.xy_ct.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.dto.UserToken;
import com.lfyun.xy_ct.entity.AccountEntity;

public interface AccountService extends IService<AccountEntity> {

	/**
	 * APP登录
	 * @param username
	 * @param password
	 * @return
	 */
	UserToken login(String username, String password);
	
	Map<Long, AccountEntity> getByBatchId(List<Long> userIdList);
}
