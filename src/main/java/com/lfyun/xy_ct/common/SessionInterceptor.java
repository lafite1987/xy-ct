package com.lfyun.xy_ct.common;

import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.configure.wx.WechatAccountConfig;
import com.lfyun.xy_ct.service.SessionManager;

import jersey.repackaged.com.google.common.collect.Sets;

//@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
    private WechatAccountConfig wechatAccountConfig;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	private static Set<String> ignore = Sets.newHashSet();
	
	static {
		ignore.add("/xyct/wechat/userInfo");
		ignore.add("/xyct/wechat/authorize");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if(ignore.contains(uri)) {
			return super.preHandle(request, response, handler);
		}
		User user = sessionManager.getUser(request);
		if(user == null) {
			String service = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + uri;
			String redirect_uri = projectUrlConfig.getMpAuthorizeUrl() + "/xyct/wechat/userInfo";
			String redirect = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wechatAccountConfig.getMpAppId() + 
					"&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=" + service + "#wechat_redirect";
			response.sendRedirect(redirect);
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				System.out.println(cookie.getName() + ":" + cookie.getValue());
			}
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}
