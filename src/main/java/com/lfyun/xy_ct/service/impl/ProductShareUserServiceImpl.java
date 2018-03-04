package com.lfyun.xy_ct.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;
import com.lfyun.xy_ct.mapper.ProductShareUserMapper;
import com.lfyun.xy_ct.service.ProductShareUserService;

@Service
public class ProductShareUserServiceImpl extends ServiceImpl<ProductShareUserMapper,ProductShareUserEntity> implements ProductShareUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductShareUserServiceImpl.class);
	
	private ProductShareUserEntity getByUserIdAndProductId(Long userId, Integer level, Long productId) {
		ProductShareUserEntity entity = new ProductShareUserEntity();
		entity.setLevel(level);
		entity.setUserId(userId);
		entity.setProductId(productId);
		EntityWrapper<ProductShareUserEntity> wrapper = new EntityWrapper<ProductShareUserEntity>(entity);
		ProductShareUserEntity productShareUserEntity = selectOne(wrapper);
		return productShareUserEntity;
	}
	
	/**
	 * 创建分销关系：
	 * @param productId
	 * @param parentUserId
	 * @param userId
	 */
	public void createRelation(Long productId, Long parentUserId, Long userId) {
		if(parentUserId == null || parentUserId == 0L || productId == null 
				|| productId == 0L || userId == null || userId == 0L) {
			LOGGER.warn("传入参数非法：productId:{} parentUserId:{} userId:{}", productId, parentUserId, userId);
			return;
		}
		//一级分销
		int level1 = 1;
		ProductShareUserEntity productShareUserEntity = getByUserIdAndProductId(userId, level1, productId);
		if(productShareUserEntity == null) {
			productShareUserEntity = new ProductShareUserEntity();
			productShareUserEntity.setProductId(productId);
			productShareUserEntity.setParentUserId(parentUserId);
			productShareUserEntity.setLevel(level1);
			productShareUserEntity.setUserId(userId);
			insert(productShareUserEntity);
		}
		
		//二级分销
		int level2 = 2;
		ProductShareUserEntity productShareUserEntity2 = getByUserIdAndProductId(userId, level2, productId);
		if(productShareUserEntity2 == null) {
			ProductShareUserEntity parent = getByUserIdAndProductId(parentUserId, level1, productId);
			if(parent != null) {
				productShareUserEntity2 = new ProductShareUserEntity();
				productShareUserEntity2.setProductId(productId);
				productShareUserEntity2.setParentUserId(parent.getParentUserId());
				productShareUserEntity2.setLevel(level2);
				productShareUserEntity2.setUserId(userId);
				insert(productShareUserEntity2);
			}
		}
		
		//三级分销
		if(productShareUserEntity2 != null) {
			int level3 = 3;
			ProductShareUserEntity productShareUserEntity3 = getByUserIdAndProductId(userId, level3, productId);
			if(productShareUserEntity3 == null) {
				ProductShareUserEntity parent = getByUserIdAndProductId(productShareUserEntity2.getParentUserId(), level1, productId);
				if(parent != null) {
					productShareUserEntity3 = new ProductShareUserEntity();
					productShareUserEntity3.setProductId(productId);
					productShareUserEntity3.setParentUserId(parent.getParentUserId());
					productShareUserEntity3.setLevel(level3);
					productShareUserEntity3.setUserId(userId);
					insert(productShareUserEntity3);
				}
			}
		}
	}

}
