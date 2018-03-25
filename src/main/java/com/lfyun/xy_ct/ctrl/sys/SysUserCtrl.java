package com.lfyun.xy_ct.ctrl.sys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lfyun.xy_ct.common.DataWrapper;
import com.lfyun.xy_ct.common.QueryDTO;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Maps;

@Controller
@RequestMapping("/sys/user")
public class SysUserCtrl {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<UserEntity>> list(@RequestBody QueryDTO<UserEntity> query) {
		UserEntity userEntity = query.getQuery();
		Page<UserEntity> page2 = query.toPage();
		page2.setOrderByField("createTime");
		page2.setAsc(false);
		EntityWrapper<UserEntity> wrapper = new EntityWrapper<UserEntity>(userEntity);
		Page<UserEntity> page = userService.selectPage(page2, wrapper);
		DataWrapper<UserEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<UserEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
	
	@RequestMapping(value = "/update.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> update(@RequestBody UserEntity form) {
		Result<Void> result = Result.success();
		UserEntity userEntity = new UserEntity();
		userEntity.setId(form.getId());
		userEntity.setUserType(form.getUserType());
		userService.updateById(userEntity);
		return result;
	}
	
	@RequestMapping(value = "/id/{id}/detail.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<UserEntity> detail(@PathVariable Long id, HttpServletRequest request) {
		Result<UserEntity> result = Result.success();
		UserEntity userEntity = userService.selectById(id);
		result.setData(userEntity);
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
