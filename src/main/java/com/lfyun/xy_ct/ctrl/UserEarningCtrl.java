package com.lfyun.xy_ct.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.entity.UserEarningEntity;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserEarningService;

@Controller
public class UserEarningCtrl {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private UserEarningService userEarningService;
	
	@RequestMapping("/front/userEarning/list")
	@ResponseBody
	public Result<List<UserEarningEntity>> frontUserBalanceDetailList(HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		Result<List<UserEarningEntity>> result = Result.success();
		List<UserEarningEntity> list = userEarningService.list(user.getId());
		result.setData(list);
		return result;
	}
}
