package com.lfyun.xy_ct.ctrl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.dto.InviteDTO;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;
import com.lfyun.xy_ct.service.ProductShareUserService;
import com.lfyun.xy_ct.service.SessionManager;

@RestController
public class ProductShareUserCtrl {

	@Autowired
	private ProductShareUserService productShareUserService;
	
	@Autowired
	private SessionManager SessionManager;
	
	@RequestMapping("/createRelation")
	public Object createRelation(Long productId, Long from, Long userId) {
		Result<Void> result = Result.success();
		productShareUserService.createRelation(productId, from, userId);
		return result;
	}
	@RequestMapping("/query")
	public Object query(Long userId) {
		Map<String, Object> result = Maps.newHashMap();
		ProductShareUserEntity entity = new ProductShareUserEntity();
		entity.setLevel(1);
		entity.setParentUserId(userId);
		EntityWrapper<ProductShareUserEntity> wrapper = new EntityWrapper<ProductShareUserEntity>(entity);
		List<ProductShareUserEntity> selectList = productShareUserService.selectList(wrapper);
		result.put("level1", selectList);
		entity.setLevel(2);
		List<ProductShareUserEntity> selectList2 = productShareUserService.selectList(wrapper);
		result.put("level2", selectList2);
		entity.setLevel(3);
		List<ProductShareUserEntity> selectList3 = productShareUserService.selectList(wrapper);
		result.put("level3", selectList3);
		return result;
	}
	
	@RequestMapping(value = "/user/inviteList", method = RequestMethod.GET)
	@ResponseBody
	public Result<InviteDTO> inviteList(HttpServletRequest request) {
		Result<InviteDTO> result = Result.success();
		User user = SessionManager.getUser(request);
		if(user == null) {
			user = new User();
			user.setId(5L);
		}
//		if(user == null) {
//			return result;
//		}
		Long userId = user.getId();
		InviteDTO inviteDTO = productShareUserService.inviteList(userId);
		result.setData(inviteDTO);
		return result;
	}
}
