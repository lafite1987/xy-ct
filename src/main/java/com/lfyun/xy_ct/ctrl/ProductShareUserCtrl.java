package com.lfyun.xy_ct.ctrl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.dto.InviteDTO;
import com.lfyun.xy_ct.dto.InviteUserDTO;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.ProductShareUserService;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Lists;

@RestController
public class ProductShareUserCtrl {

	@Autowired
	private ProductShareUserService productShareUserService;
	
	@Autowired
	private SessionManager SessionManager;
	
	@Autowired
	private UserService userService;
	
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
	
	@RequestMapping("/inviteList")
	@ResponseBody
	public Result<InviteDTO> inviteList(HttpServletRequest request) {
		Result<InviteDTO> result = Result.success();
		User user = SessionManager.getUser(request);
		if(user == null) {
			
		}
		Long userId = user.getId();
		ProductShareUserEntity entity = new ProductShareUserEntity();
		entity.setLevel(1);
		entity.setParentUserId(userId);
		EntityWrapper<ProductShareUserEntity> wrapper = new EntityWrapper<ProductShareUserEntity>(entity);
		List<ProductShareUserEntity> selectList1 = productShareUserService.selectList(wrapper);
		entity.setLevel(2);
		List<ProductShareUserEntity> selectList2 = productShareUserService.selectList(wrapper);
		entity.setLevel(3);
		List<ProductShareUserEntity> selectList3 = productShareUserService.selectList(wrapper);
		List<Long> allUserIdList = Lists.newArrayList();
		parse(selectList1, allUserIdList);
		parse(selectList2, allUserIdList);
		parse(selectList3, allUserIdList);
		Map<Long, UserEntity> map = userService.getByIds(allUserIdList);
		List<InviteUserDTO> level1 = convert(selectList1, map);
		List<InviteUserDTO> level2 = convert(selectList2, map);
		List<InviteUserDTO> level3 = convert(selectList3, map);
		InviteDTO inviteDTO = new InviteDTO();
		inviteDTO.setLevel1(level1);
		inviteDTO.setLevel2(level2);
		inviteDTO.setLevel3(level3);
		result.setData(inviteDTO);
		return result;
	}
	
	private void parse(List<ProductShareUserEntity> list, List<Long> all) {
		for(ProductShareUserEntity entity : list) {
			all.add(entity.getUserId());
		}
	}
	private List<InviteUserDTO> convert(List<ProductShareUserEntity> list, Map<Long, UserEntity> map) {
		List<InviteUserDTO> levelList = Lists.newArrayList();
		if(list != null) {
			for(ProductShareUserEntity entity : list) {
				UserEntity userEntity = map.get(entity.getUserId());
				InviteUserDTO inviteUserDTO = new InviteUserDTO(entity.getUserId(), userEntity.getAvatar());
				levelList.add(inviteUserDTO);
			}
		}
		return levelList;
	}
}
