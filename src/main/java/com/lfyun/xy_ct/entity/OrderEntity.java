package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("t_order")
public class OrderEntity {

	@TableId(value="id",type=IdType.ID_WORKER)
    private Long id;
	
	private Long productId;
	
	private String productName;
	
	/**
	 * 订单金额
	 */
	private Double amount;
	
	/**
	 * 支付状态：1-创建；2-待支付；3-支付成功
	 */
	private Integer payStatus;
	/**
	 * 用户唯一标识
	 */
	private Long userId;
	/**
	 * 微信订单号
	 */
	private String outTradeNo;
	/**
	 * 支付完成时间精确到秒
	 */
	private Long payFinishTime;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
	
	private transient Long startTime;
	
	private transient Long endTime;
}
