package com.lfyun.xy_ct.ctrl.sys;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuCtrl {

	private static ObjectMapper mapper = new ObjectMapper();
	private static Object MENU;
	private static void init() {
		try {
			StringBuilder lines = new StringBuilder();
			InputStream inputStream = SysMenuCtrl.class.getClassLoader().getResourceAsStream("menu.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = null;
			while((line = reader.readLine()) != null) {
				lines.append(line);
			}
			JsonNode jsonNode = mapper.readValue(lines.toString(), JsonNode.class);
			MENU = jsonNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/list.json")
	@ResponseBody
	public Object list(HttpServletRequest request) {
		if(MENU == null) {
			init();
		}
		return MENU;
	}
}
