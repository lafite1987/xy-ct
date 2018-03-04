package com.lfyun.xy_ct.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DataWrapper<T> {

	private Page page;
	
	private List<T> list = new ArrayList<T>();
	
	@Data
	public static class Page {
		private int currentPage;
		private int pageSize;
		private int totalNum;
		
		public Page(int currentPage, int pageSize, int totalNum) {
			this.currentPage = currentPage;
			this.pageSize = pageSize;
			this.totalNum = totalNum;
		}
	}
	
	
}
