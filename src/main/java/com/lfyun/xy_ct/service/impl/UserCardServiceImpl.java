package com.lfyun.xy_ct.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.CardEntity;
import com.lfyun.xy_ct.entity.ProductCardEntity;
import com.lfyun.xy_ct.entity.UserCardEntity;
import com.lfyun.xy_ct.mapper.UserCardMapper;
import com.lfyun.xy_ct.service.CardService;
import com.lfyun.xy_ct.service.ProductCardService;
import com.lfyun.xy_ct.service.UserCardService;

@Service
public class UserCardServiceImpl extends ServiceImpl<UserCardMapper,UserCardEntity> implements UserCardService {

	@Autowired
	private CardService cardService;
	
	@Autowired
	private ProductCardService productCardService;
	
	@Override
	public boolean addUserCardByProductId(Long userId, Long productId) {
		if(productId == null || productId <= 0) {
			return false;
		}
		ProductCardEntity productCardEntity = new ProductCardEntity();
		productCardEntity.setProductId(productId);
		EntityWrapper<ProductCardEntity> wrapper = new EntityWrapper<ProductCardEntity>(productCardEntity);
		List<ProductCardEntity> selectList = productCardService.selectList(wrapper);
		boolean result = true;
		for(ProductCardEntity entity : selectList) {
			CardEntity cardEntity = cardService.selectById(entity.getCardId());
			long time = System.currentTimeMillis();
			if(cardEntity != null && cardEntity.getState() == 1 
					&& cardEntity.getBeginIssuedTime() <= time && time <= cardEntity.getEndIssuedTime()) {
				for(int i = 0; i < entity.getCount(); i++) {
					UserCardEntity userCardEntity = new UserCardEntity();
					userCardEntity.setUserId(userId);
					userCardEntity.setCardId(entity.getCardId());
					userCardEntity.setState(1);
					userCardEntity.setOwnCount(1);
					userCardEntity.setUsedCount(0);
					boolean ret = this.insert(userCardEntity);
					result = result && ret;
				}
			}
		}
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
