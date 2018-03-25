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
import com.lfyun.xy_ct.entity.UserEarningEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserEarningService;
import com.lfyun.xy_ct.service.UserService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class UserEarningCtrl {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserEarningService userEarningService;
	
	@Autowired
    private WxMpService wxMpService;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	@RequestMapping("/front/userEarning/list")
	@ResponseBody
	public Result<List<UserEarningEntity>> frontUserBalanceDetailList(HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		Result<List<UserEarningEntity>> result = Result.success();
		List<UserEarningEntity> list = userEarningService.list(user.getId());
		result.setData(list);
		return result;
	}
	
	@RequestMapping("/user/withdrawrecord.htm")
	public String withdrawrecord(Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			//1.配置微信公众号信息
			String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/user/withdrawrecord.htm";
	        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
			return "redirect:" + redirectUrl;
		}
		UserEntity userEntity = userService.selectById(user.getId());
		List<UserEarningEntity> list = userEarningService.list(user.getId());
		model.addAttribute("earningList", list);
		model.addAttribute("earning", userEntity.getEarning());
		return "withdrawrecord";
	}
}
