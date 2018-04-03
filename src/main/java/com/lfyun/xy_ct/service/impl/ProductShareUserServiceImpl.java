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
import com.lfyun.xy_ct.entity.ProductUserRelationEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.mapper.ProductShareUserMapper;
import com.lfyun.xy_ct.service.ProductShareUserService;
import com.lfyun.xy_ct.service.ProductUserRelationService;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Lists;

@Service
public class ProductShareUserServiceImpl extends ServiceImpl<ProductShareUserMapper,ProductShareUserEntity> implements ProductShareUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductShareUserServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductUserRelationService productUserRelationService;
	
	/**
	 * 创建分销关系：
	 * @param productId
	 * @param parentUserId
	 * @param userId
	 */
	public void createRelation(Long productId, Long parentUserId, Long userId) {
		if(productId == null || productId == 0L || userId == null || userId == 0L) {
			LOGGER.warn("传入参数非法：productId:{} userId:{}", productId, userId);
			return;
		}
		ProductUserRelationEntity productUserRelationEntity = new ProductUserRelationEntity();
		productUserRelationEntity.setProductId(productId);
		productUserRelationEntity.setUserId(userId);
		EntityWrapper<ProductUserRelationEntity> wrapper = new EntityWrapper<ProductUserRelationEntity>(productUserRelationEntity);
		ProductUserRelationEntity productUserRelationEntity2 = productUserRelationService.selectOne(wrapper);
		if(productUserRelationEntity2 != null) {
			return ;
		}
		if(parentUserId == null) {
			parentUserId = 0L;
		}
		productUserRelationEntity.setUserId(parentUserId);
		ProductUserRelationEntity userParent = productUserRelationService.selectOne(wrapper);
		if(userParent == null) {
			parentUserId = 0L;
		}
		productUserRelationEntity2 = new ProductUserRelationEntity();
		productUserRelationEntity2.setProductId(productId);
		productUserRelationEntity2.setUserId(userId);
		productUserRelationEntity2.setParentUserId(parentUserId);
		productUserRelationService.insert(productUserRelationEntity2);
		if(parentUserId == 0L) {
			return;
		}
		
		//一级分销
		int level1 = 1;
		if(userParent == null) {
			return;
		}
		ProductShareUserEntity productShareUserEntity = new ProductShareUserEntity();
		productShareUserEntity.setProductId(productId);
		productShareUserEntity.setParentUserId(userParent.getUserId());
		productShareUserEntity.setLevel(level1);
		productShareUserEntity.setUserId(userId);
		insert(productShareUserEntity);
		
		if(userParent.getParentUserId() == 0L) {
			return;
		}
		//二级分销
		int level2 = 2;
		productUserRelationEntity.setUserId(userParent.getParentUserId());
		ProductUserRelationEntity userGrandfather = productUserRelationService.selectOne(wrapper);
		if(userGrandfather == null) {
			return;
		}
		ProductShareUserEntity productShareUserEntity2 = new ProductShareUserEntity();
		productShareUserEntity2.setProductId(productId);
		productShareUserEntity2.setParentUserId(userGrandfather.getUserId());
		productShareUserEntity2.setLevel(level2);
		productShareUserEntity2.setUserId(userId);
		insert(productShareUserEntity2);
		
		if(userGrandfather.getParentUserId() == 0L) {
			return;
		}
		//三级分销
		productUserRelationEntity.setUserId(userGrandfather.getParentUserId());
		ProductUserRelationEntity userGreatGrandfather = productUserRelationService.selectOne(wrapper);
		if(userGreatGrandfather == null) {
			return ;
		}
		int level3 = 3;
		ProductShareUserEntity productShareUserEntity3 = new ProductShareUserEntity();
		productShareUserEntity3.setProductId(productId);
		productShareUserEntity3.setParentUserId(userGreatGrandfather.getUserId());
		productShareUserEntity3.setLevel(level3);
		productShareUserEntity3.setUserId(userId);
		insert(productShareUserEntity3);
	}

	@Override
	public void addEarning(Long id, Double earning) {
		this.baseMapper.addEarning(id, earning);
	}
	
	public InviteDTO inviteList(Long userId, Long productId) {
		ProductShareUserEntity entity = new ProductShareUserEntity();
		entity.setLevel(1);
		entity.setParentUserId(userId);
		entity.setProductId(productId);
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
