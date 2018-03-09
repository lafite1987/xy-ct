package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("t_user")
public class UserEntity {

	/**
	 * 用户Id
	 */
	@TableId(value="id",type=IdType.AUTO)
	private Long id;
	/**
	 * 余额
	 */
	private Double balance;
	/**
	 * 收益
	 */
	private Double earning;
	/**
	 * 1-用户；2-员工；
	 */
	private Integer userType;
	/**
	 * 微信用户唯一标识
	 */
	private String openid;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像
	 */
	private String avatar;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	/**
	 * 1-已经删除；0-未删除
	 */
	@TableLogic
    private Integer deleted;
}
