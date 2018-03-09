package com.lfyun.xy_ct.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.UserEarningEntity;

public interface UserEarningService extends IService<UserEarningEntity> {

	/**
	 * 给用户的邀请人、邀请人的邀请人、邀请人的邀请人的邀请人添加收益
	 * @param orderId
	 */
	void addEarning(Long orderId);
	
	List<UserEarningEntity> list(Long userId);
}
