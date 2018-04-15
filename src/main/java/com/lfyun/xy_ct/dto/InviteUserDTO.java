package com.lfyun.xy_ct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InviteUserDTO {

	private Long id;
	
	private String avatar;
	
	private Double earning;
	
	private Boolean recharge;
}
