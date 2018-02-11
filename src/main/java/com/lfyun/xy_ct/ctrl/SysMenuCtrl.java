package com.lfyun.xy_ct.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lfyun.xy_ct.common.MockData;

@RestController
public class SysMenuCtrl {

	@RequestMapping("sys/menu/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("db/id/32/detail.json")
	@ResponseBody
	public Object list1(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("db/list.json")
	@ResponseBody
	public Object list2(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("db/getConfig.json")
	@ResponseBody
	public Object list3(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("project/1/detail.json")
	@ResponseBody
	public Object list4(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("validator/rules/list.json")
	@ResponseBody
	public Object list5(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("sys/getUserInfo.json")
	@ResponseBody
	public Object list6(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
	
	@RequestMapping("project/list.json")
	@ResponseBody
	public Object list7(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(6);
		Object byUri = MockData.getByUri(uri);
		return byUri;
	}
}
