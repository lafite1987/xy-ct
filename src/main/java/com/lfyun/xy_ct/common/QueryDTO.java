package com.lfyun.xy_ct.common;

import com.baomidou.mybatisplus.plugins.Page;

import lombok.Data;

@Data
public class QueryDTO<T> {

	private int currentPage = 1;
	private int pageSize = 15;
	private int totalNum;
	
	private T qeury;
	
	public Page<T> toPage() {
		Page<T> page = new Page<>(currentPage, pageSize);
		return page;
	}
}
