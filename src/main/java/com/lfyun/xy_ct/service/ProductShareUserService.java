package com.lfyun.xy_ct.service;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.dto.InviteDTO;
import com.lfyun.xy_ct.dto.MyInviteDTO;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;

public interface ProductShareUserService extends IService<ProductShareUserEntity> {

	void createRelation(Long productId, Long parentUserId, Long userId);
	/**
	 * 
	 * @param id
	 * @param earnign
	 */
	void addEarning(Long id, Double earning);
	
	InviteDTO inviteList(Long userId, Long productId);
	
	/**
	 * 查询用户的邀请概况
	 * @param productId
	 * @param userId
	 * @return
	 */
	MyInviteDTO getByUserId(Long productId, Long userId);
}
