package com.lfyun.xy_ct.ctrl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lfyun.xy_ct.common.DataWrapper;
import com.lfyun.xy_ct.common.QueryDTO;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Maps;

@Controller
@RequestMapping("/user")
public class UserCtrl {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionManager sessionManager;
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<UserEntity>> list(@RequestBody QueryDTO<UserEntity> query) {
		UserEntity userEntity = query.getQeury();
		EntityWrapper<UserEntity> wrapper = new EntityWrapper<UserEntity>(userEntity);
		Page<UserEntity> page = userService.selectPage(query.toPage(), wrapper);
		DataWrapper<UserEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<UserEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Result<Void> update(UserEntity form) {
		Result<Void> result = Result.success();
		userService.updateById(form);
		return result;
	}
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> withdraw(HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		Result<Void> result = Result.success();
		if(user != null) {
			userService.withdraw(user.getId());
		}
		return result;
	}
	
	@RequestMapping(value = "/getUserInfo.json")
	@ResponseBody
	public Object getUserInfo() {
		Map<String, Object> userInfo = Maps.newHashMap();
		userInfo.put("id", 24);
		userInfo.put("name", "超级管理员");
		return userInfo;
	}
		
}
