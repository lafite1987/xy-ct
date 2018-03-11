package com.lfyun.xy_ct.dto;

import lombok.Data;

@Data
public class UserToken {

	private Long id;
	private String username;
	private String phone;
	private Long createTime;
	private String token;
}
