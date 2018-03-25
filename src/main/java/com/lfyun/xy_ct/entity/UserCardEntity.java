package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("t_user_card")
public class UserCardEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private Long userId;
	
	private Long cardId;
	/**
	 * 拥有卡券数量
	 */
	private Integer ownCount;
	/**
	 * 已使用数量
	 */
	private Integer usedCount;
	/**
	 * 使用时间
	 */
	private Long useTime;
	/**
	 * 1-未使用；2-已使用
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
