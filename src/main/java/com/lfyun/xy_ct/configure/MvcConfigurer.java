package com.lfyun.xy_ct.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lfyun.xy_ct.common.SysSessionInterceptor;

@Configuration  
public class MvcConfigurer extends WebMvcConfigurerAdapter {  
  
//    @Override  
//    public void addViewControllers(ViewControllerRegistry registry) {  
//        registry.addViewController("/recharge.htm").setViewName("recharge.html");  
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  
//    }  
  
    @Override  
    public void configurePathMatch(PathMatchConfigurer configurer) {  
        super.configurePathMatch(configurer);  
        configurer.setUseSuffixPatternMatch(false);  
    }  
  
  
    @Autowired
	private SysSessionInterceptor sessionInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/sys/**");
	}
}  
