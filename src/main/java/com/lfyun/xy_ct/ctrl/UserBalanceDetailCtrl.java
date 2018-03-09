package com.lfyun.xy_ct.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserBalanceDetailService;

@Controller
public class UserBalanceDetailCtrl {

	@Autowired
	private SessionManager sessionManager;
	
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
}
