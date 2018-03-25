package com.lfyun.xy_ct.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserCardService;

@Controller
public class UserCardCtrl {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private UserCardService userCardService;
	
	@RequestMapping(value = "/user/useCard", method=RequestMethod.GET)
	@ResponseBody
	public Result<Void> useCard(Long userCardId, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		userCardService.useCard(user.getId(), userCardId);
		Result<Void> result = Result.success();
		return result;
	}
}
