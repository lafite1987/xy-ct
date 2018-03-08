package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 用户收益实体
 * @author honeyleo
 *
 */
@Data
@TableName("t_user_earning")
public class UserEarningEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	/**
	 * 金额
	 */
	private Double amount;
	/**
	 * 1-收益；2-提现；
	 */
	private Integer type;
	
	private Long userId;
	/**
	 * 关联订单号：type=1时，此字段有效，否则为0
	 */
	private Long orderId;
	/**
	 * 收益来源用户Id，type=2时，此字段为0
	 */
	private Long fromUserId;
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
