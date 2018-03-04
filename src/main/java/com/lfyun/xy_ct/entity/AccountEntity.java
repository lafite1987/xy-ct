package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 后台管理账号
 * @author honeyleo
 *
 */
@Data
@TableName("t_account")
public class AccountEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private String username;
	
	private String password;
	
	private String phone;
	
	/**
	 * 1-正常；2-封号；
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
