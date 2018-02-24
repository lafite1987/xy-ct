package com.lfyun.xy_ct.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lfyun.xy_ct.common.User;

@Component
public class SessionManager {

	private static LoadingCache<String, User> CACHE = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS).maximumSize(100000)
			.build(new CacheLoader<String, User>(){
		@Override
		public User load(String key) throws Exception {
			
			return null;
		}});
	
	public void save(User user, HttpServletResponse response) {
		String token = UUID.randomUUID().toString();
		response.setHeader("token", token);
		Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        CACHE.put(token, user);
	}
	
	public User getUser(HttpServletRequest request) {
		String token = request.getHeader("token");
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
		if(token != null) {
			User user = CACHE.getIfPresent(token);
			return user;
		}
		return null;
	}
}
