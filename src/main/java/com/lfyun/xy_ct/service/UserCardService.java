package com.lfyun.xy_ct.service;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.UserCardEntity;

public interface UserCardService extends IService<UserCardEntity> {

	/**
	 * 添加卡券
	 * @param userId
	 * @param cardId
	 * @return
	 */
	boolean addUserCard(Long userId, Long cardId);
	/**
	 * 使用卡券
	 * @param userId
	 * @param userCardId
	 * @return
	 */
	boolean useCard(Long userId, Long userCardId);
}
