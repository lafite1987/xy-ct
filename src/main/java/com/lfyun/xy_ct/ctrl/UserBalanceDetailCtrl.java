package com.lfyun.xy_ct.ctrl;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserBalanceDetailService;
import com.lfyun.xy_ct.service.UserService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class UserBalanceDetailCtrl {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserBalanceDetailService userBalanceDetailService;
	
	@Autowired
    private WxMpService wxMpService;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
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
			//1.配置微信公众号信息
			String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/user/rechargerecord.htm";
	        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
			return "redirect:" + redirectUrl;
		}
		UserEntity userEntity = userService.selectById(user.getId());
		List<UserBalanceDetailEntity> list = userBalanceDetailService.list(user.getId());
		model.addAttribute("balanceDetailList", list);
		model.addAttribute("balance", userEntity.getBalance());
		return "rechargerecord";
	}
}
