package com.lfyun.xy_ct.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.exception.AppException;

@Component
public class SessionManager {

	@Autowired
	private UserService userService;
	
	private LoadingCache<Long, User> CACHE = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS).maximumSize(100000)
			.build(new CacheLoader<Long, User>(){
		@Override
		public User load(Long userId) throws Exception {
			User user = getByUserId(userId);
			return user;
		}});
	
	public User getByUserId(Long userId) {
		UserEntity userEntity = userService.selectById(userId);
		if(userEntity != null) {
			User user = new User();
			user.setOpenid(userEntity.getOpenid());
			user.setId(userId);
			user.setAvatar(userEntity.getAvatar());
			user.setNickname(userEntity.getNickname());
			return user;
		}
		return null;
	}
	public void save(User user, HttpServletResponse response) {
//		String token = UUID.randomUUID().toString();
		String token = JwtToken.createToken(user.getId());
		response.setHeader("token", token);
		Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        CACHE.put(user.getId(), user);
	}
	
	public User getUser(HttpServletRequest request) {
		
		String token = request.getParameter("token");
		if(token == null || "".equals(token)) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if("token".equals(cookie.getName())) {
						token = cookie.getValue();
					}
				}
			}
		}
		if(StringUtils.isNotBlank(token)) {
			Long userId = JwtToken.verify(token);
			if(userId == null) {
				return null;
			}
			User user = null;
			try {
				user = CACHE.get(userId);
			} catch (ExecutionException e) {
				throw new AppException(ExceptionCodeEnums.LOGIN_FAIL);
			}
			return user;
		}
		return null;
	}
}
