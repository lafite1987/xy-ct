package com.lfyun.xy_ct.ctrl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.common.util.QRCodes;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.dto.UserShareDTO;
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
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<UserEntity>> list(@RequestBody QueryDTO<UserEntity> query) {
		UserEntity userEntity = query.getQuery();
		EntityWrapper<UserEntity> wrapper = new EntityWrapper<UserEntity>(userEntity);
		Page<UserEntity> page = userService.selectPage(query.toPage(), wrapper);
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
	
	@RequestMapping(value = "/myInviteList.htm", method = RequestMethod.GET)
	public String inventlist(Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		user = new User();
		user.setId(5L);
		UserEntity userEntity = userService.selectById(user.getId());
		model.addAttribute("earning", userEntity.getEarning());
		model.addAttribute("totalEarning", userEntity.getTotalEarning());
		return "inventlist";
	}
	
	@RequestMapping(value = "/myQRCode.htm", method = RequestMethod.GET)
	public String myQRCode(Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			user = new User();
			user.setId(5L);
		}
		String createUserQCodeData = JwtToken.createUserQCodeData(user.getId());
		System.out.println("qrCode:" + createUserQCodeData);
		String qrCode = QRCodes.createQRCode(createUserQCodeData, 200, "1");
		model.addAttribute("qrCode", qrCode);
		return "myQRCode";
	}
	
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public Result<UserShareDTO> share(Long productId, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		UserShareDTO userShareDTO = new UserShareDTO();
		userShareDTO.setUserId(user.getId());
		userShareDTO.setImageUrl("");
		userShareDTO.setLink(projectUrlConfig.getXyct() + "?from=" + user.getId() + "&productId=" + productId);
		Result<UserShareDTO> result = Result.success();
		result.setData(userShareDTO);
		return result;
	}
}
