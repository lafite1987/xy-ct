package com.lfyun.xy_ct.dto;

import lombok.Data;

@Data
public class UserShareDTO {

	private Long userId;
	
	private String title;
	
	private String description;
	
	private String link;
	
	private String imageUrl;
}
