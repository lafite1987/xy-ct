package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 用户余额明细
 * @author honeyleo
 *
 */
@Data
@TableName("t_user_balance_detail")
public class UserBalanceDetailEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private Long userId;
	
	private transient String nickname;
	/**
	 * 金额
	 */
	private Double amount;
	/**
	 * 用户收入或支出后的剩余的余额
	 */
	private Double balance;
	/**
	 * 1-收入；2-支出
	 */
	private Integer type;
	/**
	 * 业务类型：1-充值；2-到店消费；3-退款
	 */
	private Integer businessType;
	/**
	 * 1-消费成功；2-消费失败；3-退款成功；4-退款失败；
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
