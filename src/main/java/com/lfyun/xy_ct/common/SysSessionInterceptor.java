package com.lfyun.xy_ct.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.lfyun.xy_ct.common.enums.ResultCodeEnums;
import com.lfyun.xy_ct.common.util.JwtToken;

@Component
public class SysSessionInterceptor extends HandlerInterceptorAdapter {

	private String getAuthorization(HttpServletRequest request) {
		String token = request.getParameter("token");
		if(StringUtils.isBlank(token)) {
			token = request.getHeader("Authorization");
			if(token != null && !"".equals(token)) {
				token = token.substring(6);
			}
		}
		return token;
	}
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = getAuthorization(request);
		Long accountId = JwtToken.verify(token);
		if(accountId == null || accountId == 0L) {
			response.setContentType("application/json;charset=UTF-8");
			Result<String> result = Result.<String>fail(ResultCodeEnums.NO_LOGIN.getCode()).setMessage(ResultCodeEnums.NO_LOGIN.getMsg()).setRedirect("/#/login").setData("");
			String json = JSON.toJSONString(result);
			response.getWriter().write(json);
			response.getWriter().flush();
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}
