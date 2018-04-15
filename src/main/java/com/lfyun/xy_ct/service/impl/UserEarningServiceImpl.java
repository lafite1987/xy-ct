package com.lfyun.xy_ct.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.OrderEntity;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;
import com.lfyun.xy_ct.entity.UserEarningEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.mapper.UserEarningMapper;
import com.lfyun.xy_ct.service.OrderService;
import com.lfyun.xy_ct.service.ProductShareUserService;
import com.lfyun.xy_ct.service.UserEarningService;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class UserEarningServiceImpl extends ServiceImpl<UserEarningMapper,UserEarningEntity> implements UserEarningService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserEarningServiceImpl.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductShareUserService productShareUserService;
	
	@Autowired
	private UserService userService;
	
	private static Map<String, EarningConfigure> earningConfigureCache = Maps.newConcurrentMap();
	@Getter
	@AllArgsConstructor
	public static class EarningConfigure {
		private double level1;
		private double level2;
		private double level3;
		
	}
	static {
		earningConfigureCache.put("dev_1", new EarningConfigure(0.01, 0.01, 0.01));
		earningConfigureCache.put("dev_2", new EarningConfigure(0.01, 0.01, 0));
		earningConfigureCache.put("prod_1", new EarningConfigure(20, 5, 3));
		earningConfigureCache.put("prod_2", new EarningConfigure(5, 3, 0));
	}
	
	@Transactional
	public void addEarning(Long orderId) {
		String env = System.getProperty("earning.configure.env", "dev");
		OrderEntity orderEntity = orderService.selectById(orderId);
		if(orderEntity == null) {
			LOGGER.warn("订单号【{}】没有找到", orderId);
			return;
		}
		Long fromUserId = orderEntity.getUserId();
		ProductShareUserEntity productShareUserEntity = new ProductShareUserEntity();
		productShareUserEntity.setProductId(orderEntity.getProductId());
		
		//给fromUserId的邀请人发收益
		productShareUserEntity.setUserId(fromUserId);
		productShareUserEntity.setLevel(1);
		EntityWrapper<ProductShareUserEntity> wrapper = new EntityWrapper<ProductShareUserEntity>(productShareUserEntity);
		ProductShareUserEntity parent = productShareUserService.selectOne(wrapper);
		if(parent == null) {
			LOGGER.warn("未找到用户【id={}】的邀请人", fromUserId);
			return;
		}
		if(isAdd(parent.getParentUserId(), orderId)) {
			UserEntity userEntity = userService.selectById(parent.getParentUserId());
			if(userEntity != null) {
				EarningConfigure earningConfigure = earningConfigureCache.get(env + "_" + userEntity.getUserType());
				addEarningRecord(parent.getParentUserId(), fromUserId, orderId, earningConfigure.getLevel1());
				productShareUserService.addEarning(parent.getId(), earningConfigure.getLevel1());
			}
		}
		
		//给fromUserId的邀请人的邀请人发收益
		productShareUserEntity.setUserId(fromUserId);
		productShareUserEntity.setLevel(2);
		ProductShareUserEntity grandfather = productShareUserService.selectOne(wrapper);
		if(grandfather == null) {
			LOGGER.warn("未找到用户【id={}】的邀请人【id={}】的邀请人", fromUserId, parent.getParentUserId());
			return;
		}
		if(isAdd(grandfather.getParentUserId(), orderId)) {
			UserEntity userEntity = userService.selectById(grandfather.getParentUserId());
			if(userEntity != null) {
				EarningConfigure earningConfigure = earningConfigureCache.get(env + "_" + userEntity.getUserType());
				addEarningRecord(grandfather.getParentUserId(), fromUserId, orderId, earningConfigure.getLevel2());
				productShareUserService.addEarning(grandfather.getId(), earningConfigure.getLevel2());
			}
		}
		
		//给fromUserId的邀请人的邀请人的邀请人发收益
		productShareUserEntity.setUserId(fromUserId);
		productShareUserEntity.setLevel(3);
		ProductShareUserEntity greatGrandfather = productShareUserService.selectOne(wrapper);
		if(greatGrandfather == null) {
			LOGGER.warn("未找到用户【id={}】的邀请人【id={}】的邀请人", fromUserId, grandfather.getParentUserId());
			return;
		}
		if(isAdd(greatGrandfather.getParentUserId(), orderId)) {
			UserEntity userEntity = userService.selectById(greatGrandfather.getParentUserId());
			if(userEntity != null) {
				EarningConfigure earningConfigure = earningConfigureCache.get(env + "_" + userEntity.getUserType());
				if(userEntity.getUserType().intValue() != 2) {
					addEarningRecord(greatGrandfather.getParentUserId(), fromUserId, orderId, earningConfigure.getLevel3());
					productShareUserService.addEarning(greatGrandfather.getId(), earningConfigure.getLevel3());
				}
				
			}
			
		}
	}
	
	private void addEarningRecord(Long userId, Long fromUserId, Long orderId, Double amount) {
		UserEarningEntity userEarningEntity = new UserEarningEntity();
		userEarningEntity.setUserId(userId);
		userEarningEntity.setFromUserId(fromUserId);
		userEarningEntity.setOrderId(orderId);
		userEarningEntity.setAmount(amount);
		userEarningEntity.setType(1);
		userEarningEntity.setState(0);
		this.insert(userEarningEntity);
		userService.addUserEarning(userId, amount);
	}
	
	private boolean isAdd(Long userId, Long orderId) {
		UserEarningEntity entity = new UserEarningEntity();
		entity.setUserId(userId);
		entity.setOrderId(orderId);
		EntityWrapper<UserEarningEntity> wrapper = new EntityWrapper<UserEarningEntity>(entity);
		UserEarningEntity userEarningEntity = this.selectOne(wrapper);
		return userEarningEntity == null ? true : false;
	}

	@Override
	public List<UserEarningEntity> list(Long userId) {
		UserEarningEntity entity = new UserEarningEntity();
		entity.setUserId(userId);
		EntityWrapper<UserEarningEntity> wrapper = new EntityWrapper<UserEarningEntity>(entity);
		List<UserEarningEntity> list = this.baseMapper.selectList(wrapper);
		return list;
	}
}
