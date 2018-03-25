package com.lfyun.xy_ct.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.CardEntity;
import com.lfyun.xy_ct.entity.UserCardEntity;
import com.lfyun.xy_ct.mapper.UserCardMapper;
import com.lfyun.xy_ct.service.CardService;
import com.lfyun.xy_ct.service.UserCardService;

@Service
public class UserCardServiceImpl extends ServiceImpl<UserCardMapper,UserCardEntity> implements UserCardService {

	@Autowired
	private CardService cardService;
	
	@Override
	public boolean addUserCard(Long userId, Long cardId) {
		if(cardId == null || cardId <= 0) {
			return false;
		}
		UserCardEntity userCardEntity = new UserCardEntity();
		userCardEntity.setUserId(userId);
		userCardEntity.setCardId(cardId);
		userCardEntity.setState(1);
		userCardEntity.setOwnCount(1);
		userCardEntity.setUsedCount(0);
		boolean result = this.insert(userCardEntity);
		return result;
	}

	@Override
	public boolean useCard(Long userId, Long userCardId) {
		UserCardEntity userCardEntity = this.selectById(userCardId);
		if(userCardEntity != null && userCardEntity.getState() == 1 && userCardEntity.getUserId() == userId) {
			CardEntity cardEntity = cardService.selectById(userCardEntity.getCardId());
			long time = System.currentTimeMillis();
			if(cardEntity.getEffectiveBeginTime() <= time && cardEntity.getEffectiveEndTime() >= time) {
				userCardEntity.setUsedCount(1);
				userCardEntity.setState(2);
				userCardEntity.setUseTime(System.currentTimeMillis());
				this.updateById(userCardEntity);
				return true;
			}
		}
		return false;
	}

}
