package com.lfyun.xy_ct.common;

import com.baomidou.mybatisplus.plugins.Page;

import lombok.Data;

@Data
public class QueryDTO<T> {

	@Data
	public static class PageInfo {
		private int currentPage = 1;
		private int pageSize = 15;
		private int totalNum;
	}
	
	private PageInfo page;
	private T query;
	
	public Page<T> toPage() {
		Page<T> page = new Page<>(this.page.currentPage, this.page.pageSize);
		return page;
	}
}
