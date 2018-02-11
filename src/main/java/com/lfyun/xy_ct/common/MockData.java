package com.lfyun.xy_ct.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jersey.repackaged.com.google.common.collect.Lists;

public class MockData {

	static ObjectMapper mapper = new ObjectMapper();
	static List<String> lines = Lists.newArrayList();
	static {
		init();
	}
	
	private static void init() {
		try {
			InputStream inputStream = MockData.class.getClassLoader().getResourceAsStream("front.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = null;
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Object getByUri(String uri) {
		if(lines.isEmpty()) {
			init();
		}
		Iterator<String> iterator = lines.iterator();
		StringBuilder sb = new StringBuilder();
		while(iterator.hasNext()) {
			String line = iterator.next();
			if(uri.equals(line)) {
				while(iterator.hasNext()) {
					line = iterator.next();
					if("".equals(line)) {
						break;
					}
					sb.append(line);
				}
			}
		}
		try {
			JsonNode jsonNode = mapper.readValue(sb.toString(), JsonNode.class);
			return jsonNode;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
