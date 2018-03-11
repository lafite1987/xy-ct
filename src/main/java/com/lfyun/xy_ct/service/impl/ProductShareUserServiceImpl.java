package com.lfyun.xy_ct.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.dto.InviteDTO;
import com.lfyun.xy_ct.dto.InviteUserDTO;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.mapper.ProductShareUserMapper;
import com.lfyun.xy_ct.service.ProductShareUserService;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Lists;

@Service
public class ProductShareUserServiceImpl extends ServiceImpl<ProductShareUserMapper,ProductShareUserEntity> implements ProductShareUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductShareUserServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
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

	@Override
	public void addEarning(Long id, Double earning) {
		this.baseMapper.addEarning(id, earning);
	}
	
	public InviteDTO inviteList(Long userId) {
		ProductShareUserEntity entity = new ProductShareUserEntity();
		entity.setLevel(1);
		entity.setParentUserId(userId);
		EntityWrapper<ProductShareUserEntity> wrapper = new EntityWrapper<ProductShareUserEntity>(entity);
		List<ProductShareUserEntity> selectList1 = this.selectList(wrapper);
		entity.setLevel(2);
		List<ProductShareUserEntity> selectList2 = this.selectList(wrapper);
		entity.setLevel(3);
		List<ProductShareUserEntity> selectList3 = this.selectList(wrapper);
		List<Long> allUserIdList = Lists.newArrayList();
		parse(selectList1, allUserIdList);
		parse(selectList2, allUserIdList);
		parse(selectList3, allUserIdList);
		Map<Long, UserEntity> map = userService.getByIds(allUserIdList);
		Wrapper wrapper1 = convert(selectList1, map);
		Wrapper wrapper2 = convert(selectList2, map);
		Wrapper wrapper3 = convert(selectList3, map);
		InviteDTO inviteDTO = new InviteDTO();
		inviteDTO.setLevel1Earning(wrapper1.earning);
		inviteDTO.setLevel1(wrapper1.level);
		inviteDTO.setLevel2Earning(wrapper2.earning);
		inviteDTO.setLevel2(wrapper2.level);
		inviteDTO.setLevel3Earning(wrapper3.earning);
		inviteDTO.setLevel3(wrapper3.level);
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel1().add(inviteDTO.getLevel1().get(0));
		
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		inviteDTO.getLevel2().add(inviteDTO.getLevel1().get(0));
		
		inviteDTO.getLevel3().addAll(inviteDTO.getLevel1());
		inviteDTO.getLevel3().addAll(inviteDTO.getLevel2());
		return inviteDTO;
	}
	
	private static class Wrapper {
		Double earning;
		List<InviteUserDTO> level;
	}
	private void parse(List<ProductShareUserEntity> list, List<Long> all) {
		for(ProductShareUserEntity entity : list) {
			all.add(entity.getUserId());
		}
	}
	private Wrapper convert(List<ProductShareUserEntity> list, Map<Long, UserEntity> map) {
		Wrapper wrapper = new Wrapper();
		List<InviteUserDTO> levelList = Lists.newArrayList();
		Double earning = 0D;
		if(list != null) {
			for(ProductShareUserEntity entity : list) {
				UserEntity userEntity = map.get(entity.getUserId());
				InviteUserDTO inviteUserDTO = new InviteUserDTO(entity.getUserId(), userEntity.getAvatar(), entity.getEarning());
				levelList.add(inviteUserDTO);
				earning += entity.getEarning();
			}
		}
		wrapper.level = levelList;
		wrapper.earning = earning;
		return wrapper;
	}

}
