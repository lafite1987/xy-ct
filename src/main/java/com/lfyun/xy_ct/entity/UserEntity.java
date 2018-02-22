package com.lfyun.xy_ct.entity;

import lombok.Data;

@Data
public class UserEntity {

	private Long id;
	
	private String username;
	
	private String password;
	
	private Long createTime;
	
	private Long updateTime;
}
