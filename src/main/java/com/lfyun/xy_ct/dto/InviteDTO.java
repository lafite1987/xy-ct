package com.lfyun.xy_ct.dto;

import java.util.List;

import lombok.Data;

@Data
public class InviteDTO {

	private Double level1Earning = 0D;
	private List<InviteUserDTO> level1;
	private Double level2Earning = 0D;
	private List<InviteUserDTO> level2;
	private Double level3Earning = 0D;
	private List<InviteUserDTO> level3;
}
