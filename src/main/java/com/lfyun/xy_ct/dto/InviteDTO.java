package com.lfyun.xy_ct.dto;

import java.util.List;

import lombok.Data;

@Data
public class InviteDTO {

	private List<InviteUserDTO> level1;
	private List<InviteUserDTO> level2;
	private List<InviteUserDTO> level3;
}
