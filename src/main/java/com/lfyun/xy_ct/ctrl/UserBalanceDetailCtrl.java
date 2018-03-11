package com.lfyun.xy_ct.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserBalanceDetailService;
import com.lfyun.xy_ct.service.UserService;

@Controller
public class UserBalanceDetailCtrl {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserBalanceDetailService userBalanceDetailService;
	
	@RequestMapping("/front/userBalanceDetail/list")
	@ResponseBody
	public Result<List<UserBalanceDetailEntity>> frontUserBalanceDetailList(HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		Result<List<UserBalanceDetailEntity>> result = Result.success();
		List<UserBalanceDetailEntity> list = userBalanceDetailService.list(user.getId());
		result.setData(list);
		return result;
	}
	
	@RequestMapping("/user/rechargerecord.htm")
	public String rechargerecord(Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			user = new User();
			user.setId(6L);
		}
		UserEntity userEntity = userService.selectById(user.getId());
		List<UserBalanceDetailEntity> list = userBalanceDetailService.list(user.getId());
		model.addAttribute("balanceDetailList", list);
		model.addAttribute("balance", userEntity.getBalance());
		return "rechargerecord";
	}
}
