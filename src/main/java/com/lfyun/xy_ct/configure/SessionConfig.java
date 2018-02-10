package com.lfyun.xy_ct.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lfyun.xy_ct.common.SessionInterceptor;

@Configuration
public class SessionConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private SessionInterceptor sessionInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
	}

}
