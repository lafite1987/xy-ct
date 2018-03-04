package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 收益提现实体
 * @author honeyleo
 *
 */
@Data
@TableName("t_user_withdraw")
public class WithdrawEntity {

	@TableId(value="id",type=IdType.ID_WORKER)
    private Long id;
	/**
	 * 金额
	 */
	private Double amount;
	
	private Long userId;
	
	/**
	 * 1-开始提现；2-待审核；3-提现成功；4-提现失败
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
