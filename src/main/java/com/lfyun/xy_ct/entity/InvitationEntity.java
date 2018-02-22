package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

@Data
@TableName("t_invitation")
public class InvitationEntity {

	private Long id;
	
	private Long parentId;
	
	private String openid;
	
	private String nickname;
	
	/**
	 * 父节点路径：$、$1$、
	 */
	private String parentPath;
	
	private Integer state;
	
}
